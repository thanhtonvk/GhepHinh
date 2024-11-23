package com.tondz.ghephinh.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    Context context;
    List<Entity> entityList;
    private List<Entity> filteredList;

    public ImageAdapter(Context context, List<Entity> entityList) {
        this.context = context;
        this.entityList = entityList;
        this.filteredList = new ArrayList<>(entityList);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Entity entity = filteredList.get(position);
        holder.tvName.setText(entity.getName());
        if (entity.getSingle_image_url() != null) {
            Picasso.get().load(entity.getSingle_image_url()).into(holder.imgView);
        }
        holder.imgView.setOnLongClickListener(v -> {
            ClipData.Item item = new ClipData.Item(entity.getName());
            ClipData dragData = new ClipData(entity.getName(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
            Log.e("TAG", "onBindViewHolder: " + entity.getName());
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(holder.imgView);
            v.startDragAndDrop(dragData, shadowBuilder, null, 0);
            return true;
        });


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

        ImageView imgView;
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            imgView = itemView.findViewById(R.id.imgView);
        }
    }
}

