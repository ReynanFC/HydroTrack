package com.reynanfc.hydrotrack;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.reynanfc.hydrotrack.ui.home.HomeViewModel;
import com.reynanfc.hydrotrack.ui.home.utils.HomeDateFormatter;

public class MainActivity extends AppCompatActivity {

    private HomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setupWindowInsets();
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.refresh();
        updateHeaderDate();
    }

    private void updateHeaderDate() {
        TextView textToday = findViewById(R.id.text_today);
        textToday.setText(HomeDateFormatter.formatToday());
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
