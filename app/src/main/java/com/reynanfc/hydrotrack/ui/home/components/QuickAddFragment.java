package com.reynanfc.hydrotrack.ui.home.components;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.reynanfc.hydrotrack.R;
import com.reynanfc.hydrotrack.databinding.ComponentQuickAddBinding;
import com.reynanfc.hydrotrack.ui.home.HomeViewModel;

public class QuickAddFragment extends Fragment {

    private static final int SMALL_GLASS_ML = 200;
    private static final int LARGE_GLASS_ML = 300;
    private static final int BOTTLE_ML = 500;

    private ComponentQuickAddBinding binding;
    private HomeViewModel viewModel;

    public QuickAddFragment() {
        super(R.layout.component_quick_add);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = ComponentQuickAddBinding.bind(view);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.actionAdd200Ml.setOnClickListener(v -> viewModel.addWater(SMALL_GLASS_ML));
        binding.actionAdd300Ml.setOnClickListener(v -> viewModel.addWater(LARGE_GLASS_ML));
        binding.actionAdd500Ml.setOnClickListener(v -> viewModel.addWater(BOTTLE_ML));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
