package com.tondz.ghephinh.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tondz.ghephinh.R;
import com.tondz.ghephinh.activity.GhepKiHieuActivity;
import com.tondz.ghephinh.models.KiHieu;
import com.tondz.ghephinh.utils.Common;

import java.util.List;

public class KiHieuTextAdapter extends RecyclerView.Adapter<KiHieuTextAdapter.ViewHolder> {
    Context context;
    List<KiHieu> kiHieuList;

    public KiHieuTextAdapter(Context context, List<KiHieu> kiHieuList) {
        this.context = context;
        this.kiHieuList = kiHieuList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ki_hieu_text, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KiHieu kiHieu = kiHieuList.get(position);
        holder.tvName.setText(kiHieu.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Common.kiHieu = kiHieu;
                context.startActivity(new Intent(context, GhepKiHieuActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return kiHieuList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
