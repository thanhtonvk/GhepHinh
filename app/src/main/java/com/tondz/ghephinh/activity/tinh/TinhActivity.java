package com.tondz.ghephinh.activity.tinh;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tondz.ghephinh.R;
import com.tondz.ghephinh.activity.GhepHinhActivity;
import com.tondz.ghephinh.adapters.KiHieuTextAdapter;
import com.tondz.ghephinh.databinding.ActivityTinhBinding;
import com.tondz.ghephinh.models.Entity;
import com.tondz.ghephinh.utils.Common;

import java.util.ArrayList;
import java.util.List;

public class TinhActivity extends AppCompatActivity {

    ActivityTinhBinding binding;
    DatabaseReference reference;
    FirebaseDatabase database;
    List<Entity> entityList = new ArrayList<>();
    TinhAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTinhBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        loadData();
        onClick();
    }

    private void onClick() {
        binding.btnGhepHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child("Tinh").child(Common.idKhuVuc).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.e("TAG", "dataget " + snapshot.toString());
                        Common.entityList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot :
                                snapshot.getChildren()) {
                            Entity entity = dataSnapshot.getValue(Entity.class);
                            Common.entityList.add(entity);
                        }
                        reference.child("KhuVuc").child(Common.idQuocGia).child(Common.idKhuVuc).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Common.entity = snapshot.getValue(Entity.class);
                                startActivity(new Intent(getApplicationContext(), GhepHinhActivity.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        binding.btnGhepKiHieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child("KhuVuc").child(Common.idQuocGia).child(Common.idKhuVuc).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Common.entity = snapshot.getValue(Entity.class);
                        dialogKiHieu();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    private void dialogKiHieu() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ki_hieu);
        KiHieuTextAdapter kiHieuTextAdapter = new KiHieuTextAdapter(dialog.getContext(), Common.kiHieuList);
        RecyclerView recyclerView = dialog.findViewById(R.id.kiHieuRecyclerView);
        recyclerView.setAdapter(kiHieuTextAdapter);
        dialog.show();


    }

    private void init() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        adapter = new TinhAdapter(this, entityList);
        binding.recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        reference.child("Tinh").child(Common.idKhuVuc).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                entityList.clear();
                for (DataSnapshot dataSnapshot :
                        snapshot.getChildren()) {
                    Entity entity = dataSnapshot.getValue(Entity.class);
                    entityList.add(entity);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}