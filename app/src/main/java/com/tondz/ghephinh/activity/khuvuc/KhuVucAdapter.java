package com.tondz.ghephinh.activity.khuvuc;

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
import com.tondz.ghephinh.AreaActivity;
import com.tondz.ghephinh.R;
import com.tondz.ghephinh.activity.tinh.TinhActivity;
import com.tondz.ghephinh.models.Entity;
import com.tondz.ghephinh.utils.Common;

import java.util.List;

public class KhuVucAdapter extends RecyclerView.Adapter<KhuVucAdapter.ViewHolder> {
    Context context;
    List<Entity> entityList;

    public KhuVucAdapter(Context context, List<Entity> entityList) {
        this.context = context;
        this.entityList = entityList;
    }


    @NonNull
    @Override
    public KhuVucAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        KhuVucAdapter.ViewHolder viewHolder = new KhuVucAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_area, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull KhuVucAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Entity entity = entityList.get(position);
        holder.tvName.setText(entity.getName());
        if (!entity.getSingle_image_url().isEmpty()) {
            Picasso.get().load(entity.getSingle_image_url()).into(holder.imgView);
        }
        holder.itemView.setOnClickListener(v -> {
            Common.idKhuVuc = entity.getId();
            context.startActivity(new Intent(context, TinhActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return entityList.size();
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

