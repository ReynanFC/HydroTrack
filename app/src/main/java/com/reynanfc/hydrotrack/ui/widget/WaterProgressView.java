package com.reynanfc.hydrotrack.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.reynanfc.hydrotrack.R;

/**
 * Anel de progresso do consumo de água, desenhado com Canvas.
 * Clique alterna entre modo percentual ("68%") e absoluto ("1360/2000 ml").
 * Ao atingir a meta, anel e textos mudam para a cor de alerta (dourado).
 */
public class WaterProgressView extends View {

    private final Paint trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);    // parte restante
    private final Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG); // parte consumida
    private final Paint centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);   // círculo central
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF arcRect = new RectF();

    private final int progressColor;
    private final int textColor;
    private final int alertColor;
    private final int mutedTextColor;
    private int maxValue;                 // meta diária em ml

    private int consumed = 0;             // consumo acumulado em ml
    private float animatedPercent = 0;    // percentual desenhado durante a animação
    private boolean absoluteMode = false; // false = "%", true = "x/y ml"
    private ValueAnimator animator;       // animação em andamento (para poder cancelar)

    public WaterProgressView(Context context) {
        this(context, null);
    }

    public WaterProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // Lê os atributos definidos no XML (attrs.xml)
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaterProgressView);
        progressColor = a.getColor(R.styleable.WaterProgressView_progressColor,
                ContextCompat.getColor(context, R.color.hydro_primary));
        textColor = a.getColor(R.styleable.WaterProgressView_textColor,
                ContextCompat.getColor(context, R.color.hydro_card_foreground));
        alertColor = a.getColor(R.styleable.WaterProgressView_alertColor,
                ContextCompat.getColor(context, R.color.hydro_goal_reached));
        maxValue = Math.max(1, a.getInt(R.styleable.WaterProgressView_maxValue, 2000));
        a.recycle();

        mutedTextColor = ContextCompat.getColor(context, R.color.hydro_muted_foreground);
        centerPaint.setColor(ContextCompat.getColor(context, R.color.hydro_card));
        trackPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // Ao clicar, alterna entre modo percentual e modo absoluto
        setOnClickListener(v -> {
            absoluteMode = !absoluteMode;
            invalidate();
        });
    }

    // ---------- Métodos públicos ----------

    /** Atualiza o progresso em percentual da meta (pode passar de 100). */
    public void setProgress(int percent) {
        consumed = percent * maxValue / 100;
        animateTo(percent);
    }

    /** Atualiza pelo consumo acumulado em ml. */
    public void setConsumed(int ml) {
        consumed = Math.max(0, ml);
        animateTo(consumed * 100f / maxValue);
    }

    /** Altera a meta em tempo de execução. */
    public void setMaxValue(int ml) {
        if (ml <= 0) return;
        maxValue = ml;
        animateTo(consumed * 100f / maxValue);
    }

    /** Meta atingida? Usa os valores reais, não o valor da animação. */
    public boolean isGoalReached() {
        return consumed >= maxValue;
    }

    /** Anima o percentual até o valor novo e redesenha a cada passo. */
    private void animateTo(float target) {
        if (animator != null) animator.cancel(); // evita duas animações ao mesmo tempo
        animator = ValueAnimator.ofFloat(animatedPercent, target);
        animator.setDuration(700);
        animator.addUpdateListener(anim -> {
            animatedPercent = (float) anim.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    // ---------- Desenho ----------

    /** Sempre quadrada; com wrap_content ocupa 280dp. */
    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int defaultSize = (int) (280 * getResources().getDisplayMetrics().density);
        int size = Math.min(resolveSize(defaultSize, widthSpec), resolveSize(defaultSize, heightSpec));
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        float size = getWidth();
        float center = size / 2f;
        float stroke = size * 0.055f;
        float radius = (size - stroke) / 2f - size * 0.02f;
        float innerRadius = radius - stroke * 1.6f;
        int accent = isGoalReached() ? alertColor : progressColor;

        // Trilho (o que falta) e arco (o que foi consumido). 100% = 360 graus
        arcRect.set(center - radius, center - radius, center + radius, center + radius);
        trackPaint.setStrokeWidth(stroke);
        trackPaint.setColor(accent & 0x00FFFFFF | 0x40000000); // cor de destaque transparente
        progressPaint.setStrokeWidth(stroke);
        progressPaint.setColor(accent);
        canvas.drawArc(arcRect, 0, 360, false, trackPaint);
        canvas.drawArc(arcRect, -90, Math.min(animatedPercent, 100f) * 3.6f, false, progressPaint);

        canvas.drawCircle(center, center, innerRadius, centerPaint);

        // Título
        drawText(canvas, getContext().getString(R.string.water_progress_label),
                center - innerRadius * 0.3f, innerRadius * 0.12f, mutedTextColor, false);

        // Texto principal: percentual ou absoluto
        int percent = Math.round(animatedPercent);
        String value = absoluteMode
                ? getContext().getString(R.string.water_progress_absolute, consumed, maxValue)
                : getContext().getString(R.string.water_progress_default_percentage, percent);
        drawText(canvas, value, center + innerRadius * 0.15f,
                innerRadius * (absoluteMode ? 0.24f : 0.42f), isGoalReached() ? alertColor : textColor, true);

        // Texto de apoio: a informação do outro modo
        String subtitle = absoluteMode
                ? getContext().getString(R.string.water_progress_percent_of_goal, percent)
                : getContext().getString(R.string.water_progress_volume_of_goal, consumed, maxValue);
        drawText(canvas, subtitle, center + innerRadius * 0.45f, innerRadius * 0.13f, accent, false);
    }

    private void drawText(Canvas canvas, String text, float y, float textSize, int color, boolean bold) {
        textPaint.setTextSize(textSize);
        textPaint.setColor(color);
        textPaint.setTypeface(bold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        canvas.drawText(text, getWidth() / 2f, y, textPaint);
    }
}
