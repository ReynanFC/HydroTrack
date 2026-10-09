package com.reynanfc.hydrotrack;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.reynanfc.hydrotrack.ui.home.EditDailyGoalDialogFragment;
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
        editDailyGoal.setOnClickListener(view -> new EditDailyGoalDialogFragment()
                .show(getSupportFragmentManager(), EditDailyGoalDialogFragment.TAG));
    }
}
