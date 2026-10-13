package com.reynanfc.hydrotrack.ui.home.components;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.reynanfc.hydrotrack.R;
import com.reynanfc.hydrotrack.databinding.ComponentEditGoalBinding;
import com.reynanfc.hydrotrack.ui.home.HomeViewModel;

public class EditDailyGoalDialogFragment extends AppCompatDialogFragment {

    public static final String TAG = "EditDailyGoalDialog";

    private HomeViewModel viewModel;
    private ComponentEditGoalBinding binding;

    public EditDailyGoalDialogFragment() {
        super(R.layout.component_edit_goal);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        binding = ComponentEditGoalBinding.inflate(LayoutInflater.from(requireContext()));

        binding.editTextDailyGoal.setText(String.valueOf(viewModel.getGoal()));
        binding.buttonCloseEditGoal.setOnClickListener(v -> dismiss());
        binding.buttonSaveDailyGoal.setOnClickListener(v -> onSaveClicked());

        return new MaterialAlertDialogBuilder(requireContext())
                .setView(binding.getRoot())
                .create();
    }

    private void onSaveClicked() {
        Integer newGoal = parseGoal();
        if (newGoal == null) {
            binding.inputLayoutDailyGoal.setError(getString(R.string.edit_goal_error_invalid));
            return;
        }

        if (newGoal != viewModel.getGoal()) {
            viewModel.updateDailyGoal(newGoal);
        }
        dismiss();
    }

    @Nullable
    private Integer parseGoal() {
        String text = String.valueOf(binding.editTextDailyGoal.getText()).trim();
        if (text.isEmpty()) return null;

        try {
            int value = Integer.parseInt(text);
            return value > 0 ? value : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog() == null ? null : getDialog().getWindow();
        if (window == null) return;

        window.setBackgroundDrawableResource(android.R.color.transparent);

        int margin = getResources().getDimensionPixelSize(R.dimen.dialog_horizontal_margin);
        int maxWidth = getResources().getDimensionPixelSize(R.dimen.dialog_max_width);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        window.setLayout(Math.min(maxWidth, screenWidth - margin), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

