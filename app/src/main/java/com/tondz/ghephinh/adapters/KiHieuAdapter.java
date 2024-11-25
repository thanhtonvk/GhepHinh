package com.tondz.ghephinh.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
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
import com.tondz.ghephinh.models.Entity;
import com.tondz.ghephinh.models.KiHieu;

import java.util.ArrayList;
import java.util.List;

public class KiHieuAdapter extends RecyclerView.Adapter<KiHieuAdapter.ViewHolder> {
    Context context;
    public List<KiHieu> kiHieuList;

    public KiHieuAdapter(Context context, List<KiHieu> kiHieuList) {
        this.context = context;
        this.kiHieuList = kiHieuList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KiHieu kiHieu = kiHieuList.get(position);
        if (kiHieu != null) {
            if (kiHieu.getSingle_image_url() != null) {
                try {
                    Picasso.get().load(kiHieu.getSingle_image_url()).into(holder.imgView);
                } catch (Exception e) {
                    return;
                }
            }

            holder.tvName.setText(kiHieu.getName());
        }

        holder.imgView.setOnLongClickListener(v -> {
            ClipData.Item item = new ClipData.Item(position + "");
            ClipData dragData = new ClipData(position + "", new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(holder.imgView);

            v.startDragAndDrop(dragData, shadowBuilder, null, 0);
            return true;
        });
    }


    @Override
    public int getItemCount() {
        return kiHieuList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            imgView = itemView.findViewById(R.id.imgView);
        }
    }
}
