package com.reynanfc.hydrotrack.ui.home.components;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.reynanfc.hydrotrack.R;
import com.reynanfc.hydrotrack.databinding.ComponentDailySummaryBinding;
import com.reynanfc.hydrotrack.model.HomeUiState;
import com.reynanfc.hydrotrack.ui.home.HomeViewModel;
import com.reynanfc.hydrotrack.ui.home.utils.VolumeFormatter;

public class DailySummaryFragment extends Fragment {

    private ComponentDailySummaryBinding binding;

    private HomeViewModel viewModel;


    public DailySummaryFragment() {
        super(R.layout.component_daily_summary);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = ComponentDailySummaryBinding.bind(view);

        viewModel = new ViewModelProvider(requireActivity())
                .get(HomeViewModel.class);

        setupListeners();
    }

    private void setupListeners() {
        viewModel.getUiState().observe(getViewLifecycleOwner(), this::updateSummary);

        binding.buttonEditDailyGoal.setOnClickListener(v -> {
            new EditDailyGoalDialogFragment()
                    .show(getParentFragmentManager(),
                            EditDailyGoalDialogFragment.TAG);
        });
    }

    private void updateSummary(HomeUiState state) {
        binding.textConsumedToday.setText(VolumeFormatter.format(state.consumedMl));
        binding.textDailyGoal.setText(VolumeFormatter.format(state.goalMl));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
