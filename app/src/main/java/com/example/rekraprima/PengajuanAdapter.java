package com.example.rekraprima;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PengajuanAdapter extends RecyclerView.Adapter<PengajuanAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Pengajuan pengajuan);
    }

    private final List<Pengajuan> list;
    private final OnItemClickListener listener;

    public PengajuanAdapter(List<Pengajuan> list, OnItemClickListener listener) {
        this.list     = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pengajuan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pengajuan p = list.get(position);
        holder.tvId.setText(p.id);
        holder.tvJenis.setText(p.jenis);
        holder.tvStatus.setText(p.status);

        // Warna badge status
        switch (p.status) {
            case "Diterima":
                holder.tvStatus.setBackgroundResource(R.drawable.badge_bg_green);
                holder.tvStatus.setTextColor(Color.parseColor("#065F46"));
                break;
            case "Ditolak":
                holder.tvStatus.setBackgroundResource(R.drawable.badge_bg_red);
                holder.tvStatus.setTextColor(Color.parseColor("#991B1B"));
                break;
            default: // Proses
                holder.tvStatus.setBackgroundResource(R.drawable.badge_bg_yellow);
                holder.tvStatus.setTextColor(Color.parseColor("#92400E"));
                break;
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(p));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvJenis, tvStatus;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId     = itemView.findViewById(R.id.tvId);
            tvJenis  = itemView.findViewById(R.id.tvJenis);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
