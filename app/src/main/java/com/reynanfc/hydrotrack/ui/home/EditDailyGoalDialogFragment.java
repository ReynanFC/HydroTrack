package com.reynanfc.hydrotrack.ui.home;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.reynanfc.hydrotrack.R;

public class EditDailyGoalDialogFragment extends AppCompatDialogFragment {

    public static final String TAG = "EditDailyGoalDialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.component_edit_goal, null, false);

        Dialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();

        dialogView.findViewById(R.id.button_close_edit_goal)
                .setOnClickListener(view -> dismiss());

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog() == null ? null : getDialog().getWindow();
        if (window == null) {
            return;
        }

        window.setBackgroundDrawableResource(android.R.color.transparent);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int horizontalMargin = (int) (48 * getResources().getDisplayMetrics().density);
        int maxDialogWidth = (int) (384 * getResources().getDisplayMetrics().density);
        int dialogWidth = Math.min(maxDialogWidth, screenWidth - horizontalMargin);
        window.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
