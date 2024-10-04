package com.tondz.ghephinh.activity.quocgia;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.tondz.ghephinh.activity.quocgia.QuocGiaAdapter;
import com.tondz.ghephinh.adapters.KiHieuTextAdapter;
import com.tondz.ghephinh.databinding.ActivityChauLucBinding;
import com.tondz.ghephinh.databinding.ActivityQuocGiaBinding;
import com.tondz.ghephinh.models.Entity;
import com.tondz.ghephinh.utils.Common;

import java.util.ArrayList;
import java.util.List;

public class QuocGiaActivity extends AppCompatActivity {

    ActivityQuocGiaBinding binding;
    DatabaseReference reference;
    FirebaseDatabase database;
    List<Entity> entityList = new ArrayList<>();
    QuocGiaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuocGiaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        loadData();
        onClick();
    }

    private void onClick() {
        binding.btnGhepHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child("QuocGia").child(Common.idChauLuc).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Common.entityList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot :
                                snapshot.getChildren()) {
                            Entity entity = dataSnapshot.getValue(Entity.class);
                            Common.entityList.add(entity);
                        }
                        reference.child("ChauLuc").child(Common.idTheGioi).child(Common.idChauLuc).addValueEventListener(new ValueEventListener() {
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
                reference.child("ChauLuc").child(Common.idTheGioi).child(Common.idChauLuc).addValueEventListener(new ValueEventListener() {
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
        adapter = new QuocGiaAdapter(this, entityList);
        binding.recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        reference.child("QuocGia").child(Common.idChauLuc).addValueEventListener(new ValueEventListener() {
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