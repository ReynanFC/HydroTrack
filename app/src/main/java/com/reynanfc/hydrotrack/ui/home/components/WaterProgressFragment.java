package com.reynanfc.hydrotrack.ui.home.components;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.reynanfc.hydrotrack.R;
import com.reynanfc.hydrotrack.databinding.ComponentWaterProgressBinding;
import com.reynanfc.hydrotrack.model.HomeUiState;
import com.reynanfc.hydrotrack.ui.home.HomeViewModel;

public class WaterProgressFragment extends Fragment {

    private ComponentWaterProgressBinding binding;
    private HomeViewModel viewModel;

    public WaterProgressFragment() {
        super(R.layout.component_water_progress);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = ComponentWaterProgressBinding.bind(view);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        viewModel.getUiState().observe(getViewLifecycleOwner(), this::render);
    }

    private void render(HomeUiState state) {
        binding.waterProgressView.setMaxValue(state.goalMl);
        binding.waterProgressView.setConsumed(state.consumedMl);

        updateProgressMessage(state);
    }

    private void updateProgressMessage(HomeUiState state) {
        int difference = state.consumedMl - state.goalMl;

        if (difference < 0) {
            binding.textWaterProgressMessage.setText(
                    getString(R.string.water_progress_remaining_message, -difference));
        } else if (difference == 0) {
            binding.textWaterProgressMessage.setText(R.string.water_progress_goal_reached_message);
        } else {
            binding.textWaterProgressMessage.setText(
                    getString(R.string.water_progress_goal_exceeded_message, difference));
        }

        binding.textWaterProgressMessage.setTextColor(ContextCompat.getColor(requireContext(),
                state.isGoalReached() ? R.color.hydro_goal_reached : R.color.hydro_muted_foreground));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
