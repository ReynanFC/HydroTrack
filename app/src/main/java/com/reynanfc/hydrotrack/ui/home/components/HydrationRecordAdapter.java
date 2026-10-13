package com.reynanfc.hydrotrack.ui.home.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.reynanfc.hydrotrack.R;
import com.reynanfc.hydrotrack.databinding.ComponentItemHydrationRecordBinding;
import com.reynanfc.hydrotrack.model.RegisterWater;
import com.reynanfc.hydrotrack.ui.home.utils.HomeDateFormatter;
import com.reynanfc.hydrotrack.ui.home.utils.VolumeFormatter;

import java.time.format.DateTimeFormatter;

public class HydrationRecordAdapter
        extends ListAdapter<RegisterWater, HydrationRecordAdapter.RecordViewHolder> {

    public interface OnDeleteClickListener {
        void onDelete(int position);
    }

    private static final DiffUtil.ItemCallback<RegisterWater> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull RegisterWater oldItem,
                                               @NonNull RegisterWater newItem) {
                    return oldItem.time().equals(newItem.time());
                }

                @Override
                public boolean areContentsTheSame(@NonNull RegisterWater oldItem,
                                                  @NonNull RegisterWater newItem) {
                    return oldItem.equals(newItem);
                }
            };

    private final OnDeleteClickListener onDeleteClickListener;

    public HydrationRecordAdapter(OnDeleteClickListener onDeleteClickListener) {
        super(DIFF_CALLBACK);
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ComponentItemHydrationRecordBinding binding = ComponentItemHydrationRecordBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new RecordViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class RecordViewHolder extends RecyclerView.ViewHolder {

        private final ComponentItemHydrationRecordBinding binding;

        RecordViewHolder(ComponentItemHydrationRecordBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.buttonDeleteRecord.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClickListener.onDelete(position);
                }
            });
        }

        void bind(RegisterWater record) {
            Context context = binding.getRoot().getContext();
            String volume = context.getString(
                    R.string.volume_ml, VolumeFormatter.format(record.consumeMl()));

            binding.imageRecordType.setImageResource(R.drawable.ic_glass_water);
            binding.textRecordAmount.setText(context.getString(R.string.records_amount, volume));
            binding.textRecordTime.setText(record.time().format(HomeDateFormatter.TIME_FORMATTER));
        }
    }
}
