package com.tondz.ghephinh.adapters;

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

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    Context context;
    List<Entity> entityList;

    public ImageAdapter(Context context, List<Entity> entityList) {
        this.context = context;
        this.entityList = entityList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Entity entity = entityList.get(position);
        if (!entity.getSingle_image_url().isEmpty()) {
            Picasso.get().load(entity.getSingle_image_url()).into(holder.imgView);
        }
        holder.imgView.setOnLongClickListener(v -> {
            ClipData.Item item = new ClipData.Item(position + "");
            ClipData dragData = new ClipData(position + "", new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
            Log.e("TAG", "onBindViewHolder: "+entity.getName() );
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(holder.imgView);
            v.startDragAndDrop(dragData, shadowBuilder, null, 0);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgView;

        public ViewHolder(View itemView) {
            super(itemView);

            imgView = itemView.findViewById(R.id.imgView);
        }
    }
}

