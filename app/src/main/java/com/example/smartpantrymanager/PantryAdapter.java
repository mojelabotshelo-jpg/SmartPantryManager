package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> pantryList;
    private OnItemDeleteListener deleteListener;

    public interface OnItemDeleteListener {
        void onDeleteClick(int id);
    }

    public PantryAdapter(List<PantryItem> pantryList, OnItemDeleteListener deleteListener) {
        this.pantryList = pantryList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvQtyUnit.setText("Quantity: " + item.getQuantity() + " " + item.getUnit());
        holder.tvExpiry.setText("Expiry: " + (item.getExpiryDate() == null || item.getExpiryDate().isEmpty() ? "None" : item.getExpiryDate()));

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(item.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return pantryList.size();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQtyUnit, tvExpiry;
        Button btnDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvIngredientName);
            tvQtyUnit = itemView.findViewById(R.id.tvQuantityUnit);
            tvExpiry = itemView.findViewById(R.id.tvExpiry);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}