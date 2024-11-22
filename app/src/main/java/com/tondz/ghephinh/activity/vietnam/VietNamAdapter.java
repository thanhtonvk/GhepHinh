package com.tondz.ghephinh.activity.vietnam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tondz.ghephinh.R;
import com.tondz.ghephinh.activity.huyen.HuyenActivity;
import com.tondz.ghephinh.models.Entity;
import com.tondz.ghephinh.utils.Common;

import java.util.ArrayList;
import java.util.List;

public class VietNamAdapter extends RecyclerView.Adapter<VietNamAdapter.ViewHolder> {
    Context context;
    List<Entity> entityList;
    private List<Entity> filteredList;

    public VietNamAdapter(Context context, List<Entity> entityList) {
        this.context = context;
        this.entityList = entityList;
        this.filteredList = new ArrayList<>(entityList);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_area, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Entity entity = filteredList.get(position);
        holder.tvName.setText(entity.getName());
        if (!entity.getSingle_image_url().isEmpty()) {
            Picasso.get().load(entity.getSingle_image_url()).into(holder.imgView);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String query) {
        filteredList.clear();
        if (!query.isEmpty()) {
            for (Entity item : entityList) {
                if (String.valueOf(item.getName()).toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        } else {
            filteredList.addAll(entityList);
        }
        notifyDataSetChanged(); // Cập nhật giao diện
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imgView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            imgView = itemView.findViewById(R.id.imgView);
        }
    }
}

