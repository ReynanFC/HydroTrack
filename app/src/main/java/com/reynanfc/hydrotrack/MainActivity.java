package com.reynanfc.hydrotrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.reynanfc.hydrotrack.ui.home.HomeDateFormatter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        setupWindowInsets();
        setupHeader();
        setupDailyGoalEditor();
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupHeader() {
        TextView view = findViewById(R.id.text_today);
        view.setText(HomeDateFormatter.formatToday());
    }

    private void setupDailyGoalEditor() {
        ImageButton editDailyGoal = findViewById(R.id.button_edit_daily_goal);
        editDailyGoal.setOnClickListener(view -> showDailyGoalDialog());
    }

    private void showDailyGoalDialog() {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.component_edit_goal, null, false);

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .create();

        dialogView.findViewById(R.id.button_close_edit_goal)
                .setOnClickListener(view -> dialog.dismiss());

        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int horizontalMargin = (int) (48 * getResources().getDisplayMetrics().density);
            int maxDialogWidth = (int) (384 * getResources().getDisplayMetrics().density);
            int dialogWidth = Math.min(maxDialogWidth, screenWidth - horizontalMargin);
            window.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }
}
