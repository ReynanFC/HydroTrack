package com.reynanfc.hydrotrack.ui.home.components;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.reynanfc.hydrotrack.R;
import com.reynanfc.hydrotrack.databinding.ComponentTodayRecordsBinding;
import com.reynanfc.hydrotrack.model.HomeUiState;
import com.reynanfc.hydrotrack.ui.home.HomeViewModel;
import com.reynanfc.hydrotrack.ui.home.utils.VolumeFormatter;


public class TodayRecordsFragment extends Fragment {

    private ComponentTodayRecordsBinding binding;
    private HomeViewModel viewModel;
    private HydrationRecordAdapter adapter;

    public TodayRecordsFragment() {
        super(R.layout.component_today_records);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = ComponentTodayRecordsBinding.bind(view);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        setupRecyclerView();
        viewModel.getUiState().observe(getViewLifecycleOwner(), this::render);
    }

    private void setupRecyclerView() {
        adapter = new HydrationRecordAdapter(position -> viewModel.removeRecord(position));
        binding.recyclerRecords.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerRecords.setAdapter(adapter);
        binding.recyclerRecords.setNestedScrollingEnabled(false);
    }

    private void render(HomeUiState state) {
        int count = state.records.size();
        boolean isEmpty = count == 0;

        binding.containerEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.containerFilledState.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

        binding.textRecordCount.setText(
                getResources().getQuantityString(R.plurals.records_count, count, count));
        binding.textTotalAmount.setText(
                getString(R.string.volume_ml, VolumeFormatter.format(state.consumedMl)));

        adapter.submitList(state.records);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
