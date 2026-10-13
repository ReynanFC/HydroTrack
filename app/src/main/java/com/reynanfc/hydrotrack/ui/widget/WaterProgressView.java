package com.reynanfc.hydrotrack.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.os.BundleCompat;

import com.reynanfc.hydrotrack.R;

/**
 * Componente gráfico customizado que mostra o consumo de água em relação à meta diária.
 *
 * <h3>Como funciona</h3>
 * <ul>
 *   <li><b>Desenho:</b> nada vem de XML. Tudo é desenhado em {@link #onDraw(Canvas)} com
 *   {@link Canvas}: um brilho radial ao fundo, o trilho (parte restante da meta), o arco de
 *   progresso (parte já consumida, limitado a uma volta mesmo acima de 100%) e o
 *   círculo central com ícone, título, texto principal e texto de apoio.</li>
 *   <li><b>Progresso:</b> {@link #setProgress(int)} recebe o percentual da meta (pode passar de
 *   100) e anima o valor desenhado com um {@link ValueAnimator}. A cada quadro da animação
 *   o valor intermediário é guardado em {@code animatedPercent} e {@link #invalidate()} pede
 *   um novo {@code onDraw}.</li>
 *   <li><b>Alerta de meta:</b> quando o percentual chega a 100%, o anel e o texto trocam para
 *   {@code alertColor} (dourado). Acima de 100%, o anel fica cheio e os textos mostram o
 *   total acumulado (ex.: "115%" ou "2300/2000 ml").
 *   A condição usa o valor recebido, não o animado, então reflete os dados salvos.</li>
 *   <li><b>Interação:</b> cada clique alterna entre modo percentual ("68%") e modo absoluto
 *   ("1360/2000 ml"). O modo é salvo em {@link #onSaveInstanceState()} (a view precisa de id).</li>
 *   <li><b>Customização:</b> {@code app:progressColor}, {@code app:textColor},
 *   {@code app:alertColor} e {@code app:maxValue} são declarados em {@code attrs.xml} e lidos
 *   com {@link TypedArray} no construtor. Também podem ser alterados em tempo de execução.</li>
 * </ul>
 */
public class WaterProgressView extends View {

    private static final int DEFAULT_MAX_VALUE = 2000;
    private static final int DEFAULT_SIZE_DP = 280;
    private static final long ANIMATION_DURATION_MS = 700;

    /** Ângulo inicial do arco: -90° começa no topo do círculo (12 horas). */
    private static final float START_ANGLE = -90f;

    private static final String STATE_SUPER = "super_state";
    private static final String STATE_SHOW_ABSOLUTE = "show_absolute";

    // ---------- Pincéis (configurados uma vez, reutilizados em todo onDraw) ----------
    private final Paint glowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint valuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint subtitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /** Retângulo que delimita o anel; recalculados apenas quando o tamanho muda. */
    private final RectF ringBounds = new RectF();

    @Nullable
    private final Drawable dropIcon;

    // ---------- Geometria (calculada em onSizeChanged) ----------
    private float centerX;
    private float centerY;
    private float ringStroke;
    private float centerRadius;
    /** Cor usada no gradiente atual do brilho; recriado só quando a cor ativa muda. */
    @ColorInt private int glowColor;

    // ---------- Cores configuráveis ----------
    @ColorInt private int progressColor;
    @ColorInt private int textColor;
    @ColorInt private int alertColor;
    @ColorInt private final int mutedTextColor;

    // ---------- Estado ----------
    private int maxValue;
    private int consumedMl;
    /** Percentual "real" informado pelo app. */
    private int progressPercent;
    /** Percentual sendo desenhado no momento (muda durante a animação). */
    private float animatedPercent;
    private boolean showAbsolute;

    @Nullable
    private ValueAnimator progressAnimator;

    public WaterProgressView(Context context) {
        this(context, null);
    }

    public WaterProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Lê os atributos personalizados do XML; o que não for informado usa o padrão do app
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaterProgressView, defStyleAttr, 0);
        try {
            progressColor = a.getColor(R.styleable.WaterProgressView_progressColor,
                    ContextCompat.getColor(context, R.color.hydro_primary));
            textColor = a.getColor(R.styleable.WaterProgressView_textColor,
                    ContextCompat.getColor(context, R.color.hydro_card_foreground));
            alertColor = a.getColor(R.styleable.WaterProgressView_alertColor,
                    ContextCompat.getColor(context, R.color.hydro_goal_reached));
            maxValue = Math.max(1, a.getInt(R.styleable.WaterProgressView_maxValue, DEFAULT_MAX_VALUE));
        } finally {
            a.recycle();
        }

        mutedTextColor = ContextCompat.getColor(context, R.color.hydro_muted_foreground);
        dropIcon = ContextCompat.getDrawable(context, R.drawable.ic_water_drop);

        setupPaints(context);

        // Clicável para alternar o modo de exibição (ver performClick)
        setClickable(true);
        setFocusable(true);
        updateContentDescription();
    }

    private void setupPaints(Context context) {
        trackPaint.setStyle(Paint.Style.STROKE);
        trackPaint.setStrokeCap(Paint.Cap.ROUND);

        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        centerPaint.setStyle(Paint.Style.FILL);
        centerPaint.setColor(ContextCompat.getColor(context, R.color.hydro_card));

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        titlePaint.setColor(mutedTextColor);

        valuePaint.setTextAlign(Paint.Align.CENTER);
        valuePaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));

        subtitlePaint.setTextAlign(Paint.Align.CENTER);
        subtitlePaint.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    }

    // =====================================================================================
    // API pública
    // =====================================================================================

    /**
     * Atualiza o progresso com animação suave.
     *
     * @param value consumo acumulado em relação à meta, em percentual. Pode ser maior que 100.
     */
    public void setProgress(int value) {
        consumedMl = Math.round(value * maxValue / 100f);
        animateTo(Math.max(0, value));
    }

    public int getProgress() {
        return progressPercent;
    }

    /**
     * Atalho para o app: informa o volume consumido em ml. Calcula o percentual sobre a meta
     * atual e delega para a mesma animação de {@link #setProgress(int)}, mantendo o valor
     * exato em ml para o modo absoluto.
     */
    public void setConsumed(int ml) {
        consumedMl = Math.max(0, ml);
        animateTo(percentOf(consumedMl));
    }

    /** Altera a meta diária em tempo de execução, sem recriar a interface. */
    public void setMaxValue(int maxValueMl) {
        maxValue = Math.max(1, maxValueMl);
        animateTo(percentOf(consumedMl));
    }

    public int getMaxValue() {
        return maxValue;
    }

    /** Meta atingida ou ultrapassada, com base no valor informado (não no animado). */
    public boolean isGoalReached() {
        return progressPercent >= 100;
    }

    public void setProgressColor(@ColorInt int color) {
        progressColor = color;
        invalidate();
    }

    public void setTextColor(@ColorInt int color) {
        textColor = color;
        invalidate();
    }

    public void setAlertColor(@ColorInt int color) {
        alertColor = color;
        invalidate();
    }

    public boolean isShowingAbsolute() {
        return showAbsolute;
    }

    public void setShowAbsolute(boolean showAbsolute) {
        this.showAbsolute = showAbsolute;
        updateContentDescription();
        invalidate();
    }

    /** Clique (toque ou acessibilidade) alterna entre percentual e valor absoluto. */
    @Override
    public boolean performClick() {
        setShowAbsolute(!showAbsolute);
        return super.performClick();
    }

    // Animação

    private int percentOf(int ml) {
        return Math.round(ml * 100f / maxValue);
    }

    /**
     * Anima {@code animatedPercent} do valor atual até o novo percentual. Se uma animação
     * estiver em andamento, ela é cancelada e a nova parte de onde a anterior parou.
     */
    private void animateTo(int targetPercent) {
        progressPercent = targetPercent;
        updateContentDescription();

        if (progressAnimator != null) {
            progressAnimator.cancel();
        }

        progressAnimator = ValueAnimator.ofFloat(animatedPercent, targetPercent);
        progressAnimator.setDuration(ANIMATION_DURATION_MS);
        progressAnimator.setInterpolator(new DecelerateInterpolator());
        progressAnimator.addUpdateListener(animation -> {
            animatedPercent = (float) animation.getAnimatedValue();
            invalidate(); // pede um novo onDraw com o valor intermediário
        });
        progressAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (progressAnimator != null) {
            progressAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }

    // Medidas e geometria

    /** O componente é sempre quadrado; com wrap_content usa um tamanho padrão. */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultSize = Math.round(dp(DEFAULT_SIZE_DP));
        int width = resolveSize(defaultSize, widthMeasureSpec);
        int height = resolveSize(defaultSize, heightMeasureSpec);
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    /**
     * Calcula as posições uma única vez por mudança de tamanho, deixando o onDraw leve.
     * Todas as medidas são proporcionais ao tamanho, então a view se adapta a qualquer dimensão.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float size = Math.min(w, h);
        centerX = w / 2f;
        centerY = h / 2f;

        ringStroke = size * 0.055f;
        float ringRadius = size / 2f - ringStroke / 2f - size * 0.04f;
        ringBounds.set(centerX - ringRadius, centerY - ringRadius,
                centerX + ringRadius, centerY + ringRadius);

        centerRadius = ringRadius - ringStroke * 1.6f;

        trackPaint.setStrokeWidth(ringStroke);
        progressPaint.setStrokeWidth(ringStroke);

        updateGlow(isGoalReached() ? alertColor : progressColor);

        // Sombra suave do círculo central
        centerPaint.setShadowLayer(size * 0.03f, 0, size * 0.01f, withAlpha(Color.BLACK, 0x26));

        titlePaint.setTextSize(centerRadius * 0.11f);
        subtitlePaint.setTextSize(centerRadius * 0.13f);
    }

    // Desenho
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int accent = isGoalReached() ? alertColor : progressColor;

        drawGlow(canvas, accent);
        drawRing(canvas, accent);
        drawCenter(canvas, accent);
    }

    private void drawGlow(Canvas canvas, int accent) {
        if (accent != glowColor) updateGlow(accent);
        canvas.drawCircle(centerX, centerY, getWidth() / 2f, glowPaint);
    }

    private void updateGlow(int color) {
        glowColor = color;
        glowPaint.setShader(new RadialGradient(centerX, centerY, Math.max(1f, getWidth() / 2f),
                withAlpha(color, 0x80), withAlpha(color, 0x00), Shader.TileMode.CLAMP));
    }

    /**
     * Trilho = parte restante da meta (cor de destaque transparente).
     * Arco = parte já consumida, proporcional ao percentual animado, limitado a uma volta.
     */
    private void drawRing(Canvas canvas, int accent) {
        trackPaint.setColor(withAlpha(accent, 0x40));
        canvas.drawArc(ringBounds, 0, 360, false, trackPaint);

        float sweep = Math.min(animatedPercent, 100f) / 100f * 360f;
        if (sweep > 0) {
            progressPaint.setColor(accent);
            canvas.drawArc(ringBounds, START_ANGLE, sweep, false, progressPaint);
        }
    }

    /** Círculo central com ícone, título, valor principal e texto de apoio. */
    private void drawCenter(Canvas canvas, int accent) {
        canvas.drawCircle(centerX, centerY, centerRadius, centerPaint);

        if (dropIcon != null) {
            int half = Math.round(centerRadius * 0.14f);
            int iconCenterY = Math.round(centerY - centerRadius * 0.55f);
            dropIcon.setBounds(Math.round(centerX) - half, iconCenterY - half,
                    Math.round(centerX) + half, iconCenterY + half);
            dropIcon.setTint(accent);
            dropIcon.draw(canvas);
        }

        canvas.drawText(getContext().getString(R.string.water_progress_label),
                centerX, centerY - centerRadius * 0.22f, titlePaint);

        String value = showAbsolute
                ? getContext().getString(R.string.water_progress_absolute, consumedMl, maxValue)
                : getContext().getString(R.string.water_progress_default_percentage,
                        Math.round(animatedPercent));
        valuePaint.setColor(isGoalReached() ? alertColor : textColor);
        fitTextSize(valuePaint, value, centerRadius * 0.42f, centerRadius * 1.6f);
        canvas.drawText(value, centerX, centerY + centerRadius * 0.2f, valuePaint);

        String subtitle = showAbsolute
                ? getContext().getString(R.string.water_progress_percent_of_goal, progressPercent)
                : getContext().getString(R.string.water_progress_volume_of_goal, consumedMl, maxValue);
        subtitlePaint.setColor(accent);
        canvas.drawText(subtitle, centerX, centerY + centerRadius * 0.48f, subtitlePaint);
    }

    /** Usa o tamanho desejado, reduzindo-o se o texto não couber na largura disponível. */
    private static void fitTextSize(Paint paint, String text, float desiredSize, float maxWidth) {
        paint.setTextSize(desiredSize);
        float width = paint.measureText(text);
        if (width > maxWidth) {
            paint.setTextSize(desiredSize * maxWidth / width);
        }
    }

    // Estado salvo e acessibilidade

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState());
        bundle.putBoolean(STATE_SHOW_ABSOLUTE, showAbsolute);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            showAbsolute = bundle.getBoolean(STATE_SHOW_ABSOLUTE);
            super.onRestoreInstanceState(
                    BundleCompat.getParcelable(bundle, STATE_SUPER, Parcelable.class));
            updateContentDescription();
            return;
        }
        super.onRestoreInstanceState(state);
    }

    private void updateContentDescription() {
        setContentDescription(getContext().getString(R.string.water_progress_accessibility_value,
                progressPercent, consumedMl, maxValue));
    }

    // Utilitários

    private float dp(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                getResources().getDisplayMetrics());
    }

    @ColorInt
    private static int withAlpha(@ColorInt int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }
}
