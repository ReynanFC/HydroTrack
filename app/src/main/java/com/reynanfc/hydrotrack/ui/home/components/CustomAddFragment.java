package com.reynanfc.hydrotrack.ui.home.components;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.reynanfc.hydrotrack.R;
import com.reynanfc.hydrotrack.databinding.ComponentCustomAddBinding;
import com.reynanfc.hydrotrack.ui.home.HomeViewModel;

public class CustomAddFragment extends Fragment {

    private ComponentCustomAddBinding binding;
    private HomeViewModel viewModel;

    public CustomAddFragment() {
        super(R.layout.component_custom_add);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = ComponentCustomAddBinding.bind(view);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        setupListener();
    }

    private void setupListener() {
        binding.buttonAddCustomAmount.setOnClickListener(v -> submitCustomAmount());
    }

    private void submitCustomAmount() {
        String input = binding.editTextCustomAmount.getText().toString().trim();

        if (!validateInput(input)) {
            return;
        }

        int amountMl = Integer.parseInt(input);
        viewModel.addWater(amountMl);

        clearInputAndHideKeyboard();
    }

    private boolean validateInput(String input) {
        if (input.isEmpty()) {
            binding.editTextCustomAmount.setError(getString(R.string.custom_add_error_empty));
            return false;
        }

        try {
            int amount = Integer.parseInt(input);
            if (amount <= 0) {
                binding.editTextCustomAmount.setError(getString(R.string.custom_add_error_not_positive));
                return false;
            }

        } catch (NumberFormatException e) {
            binding.editTextCustomAmount.setError(getString(R.string.custom_add_error_too_large));
            return false;
        }

        return true;
    }

    private void clearInputAndHideKeyboard() {
        binding.editTextCustomAmount.setText("");
        binding.editTextCustomAmount.clearFocus();

        View view = getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}