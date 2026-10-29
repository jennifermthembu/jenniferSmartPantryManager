package com.richfield.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.richfield.smartpantry.R;
import com.richfield.smartpantry.model.PantryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom {@link RecyclerView.Adapter} that binds the list of pantry items to
 * the screen (brief Section 3.1 requires a RecyclerView with a custom adapter).
 * Exposes click + delete callbacks so the hosting Fragment can react.
 */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    public interface OnItemActionListener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private final List<PantryItem> items = new ArrayList<>();
    private final OnItemActionListener listener;

    public PantryAdapter(OnItemActionListener listener) {
        this.listener = listener;
    }

    public void setItems(List<PantryItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = items.get(position);
        holder.name.setText(item.getName());

        String qty = trimNumber(item.getQuantity()) + " "
                + (item.getUnit() == null ? "" : item.getUnit());
        holder.quantity.setText(qty.trim());

        if (item.getExpiryDate() == null || item.getExpiryDate().isEmpty()) {
            holder.expiry.setVisibility(View.GONE);
        } else {
            holder.expiry.setVisibility(View.VISIBLE);
            holder.expiry.setText(holder.expiry.getContext()
                    .getString(R.string.expires_on, item.getExpiryDate()));
        }

        holder.itemView.setOnClickListener(v -> listener.onEdit(item));
        holder.delete.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /** Shows 3 instead of 3.0 but keeps 2.5 as-is. */
    private String trimNumber(double value) {
        if (value == Math.floor(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView quantity;
        final TextView expiry;
        final View delete;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_name);
            quantity = itemView.findViewById(R.id.text_quantity);
            expiry = itemView.findViewById(R.id.text_expiry);
            delete = itemView.findViewById(R.id.button_delete);
        }
    }
}
