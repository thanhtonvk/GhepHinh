package com.tondz.ghephinh.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tondz.ghephinh.R;
import com.tondz.ghephinh.activity.GhepKiHieuActivity;
import com.tondz.ghephinh.models.KiHieu;
import com.tondz.ghephinh.utils.Common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KiHieuTextAdapter extends RecyclerView.Adapter<KiHieuTextAdapter.ViewHolder> {
    Context context;
    Set<String> kiHieuList;
    FirebaseDatabase database;
    DatabaseReference reference;

    public KiHieuTextAdapter(Context context, Set<String> kiHieuList) {
        this.context = context;
        this.kiHieuList = kiHieuList;
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ki_hieu_text, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String loaiKiHieu = kiHieuList.toArray()[position].toString();
        holder.tvName.setText(loaiKiHieu);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child("KiHieu").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Common.kiHieuList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot :
                                snapshot.getChildren()) {
                            KiHieu kiHieu = dataSnapshot.getValue(KiHieu.class);
                            if (kiHieu.getGroup().equals(loaiKiHieu)) {
                                Common.kiHieuList.add(kiHieu);
                            }
                        }
                        context.startActivity(new Intent(context, GhepKiHieuActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
