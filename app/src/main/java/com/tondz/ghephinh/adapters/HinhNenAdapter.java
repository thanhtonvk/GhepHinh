package com.tondz.ghephinh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tondz.ghephinh.R;
import com.tondz.ghephinh.models.HinhNen;

import java.util.List;

public class HinhNenAdapter extends RecyclerView.Adapter<HinhNenAdapter.ViewHolder> {
    List<HinhNen> hinhNenList;
    Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String hinhAnh);
    }

    public HinhNenAdapter(List<HinhNen> hinhNenList, Context context, OnItemClickListener listener) {
        this.hinhNenList = hinhNenList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_choose_image, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HinhNen hinhNen = hinhNenList.get(position);
        holder.tvName.setText(hinhNen.getTen());
        if (hinhNen.getHinhAnh()!=null) {
            Picasso.get().load(hinhNen.getHinhAnh()).into(holder.imgView);
        }
        holder.itemView.setOnClickListener(v -> listener.onItemClick(hinhNen.getHinhAnh()));
    }

    @Override
    public int getItemCount() {
        return hinhNenList.size();
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
