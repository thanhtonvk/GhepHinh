package com.tondz.ghephinh.activity.chauluc;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tondz.ghephinh.R;
import com.tondz.ghephinh.activity.GhepHinhActivity;
import com.tondz.ghephinh.activity.GhepKiHieuActivity;
import com.tondz.ghephinh.adapters.AreaAdapter;
import com.tondz.ghephinh.adapters.KiHieuTextAdapter;
import com.tondz.ghephinh.databinding.ActivityChauLucBinding;
import com.tondz.ghephinh.models.CauHoi;
import com.tondz.ghephinh.models.Entity;
import com.tondz.ghephinh.utils.Common;

import java.util.ArrayList;
import java.util.List;

public class ChauLucActivity extends AppCompatActivity {

    ActivityChauLucBinding binding;
    DatabaseReference reference;
    FirebaseDatabase database;
    List<Entity> entityList = new ArrayList<>();
    ChauLucAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChauLucBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        loadData();
        onClick();
    }

    private void onClick() {
        binding.btnGhepHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child("ChauLuc").child(Common.idTheGioi).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Common.entityList = new ArrayList<>();
                        Common.cauHoiArrayList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot :
                                snapshot.getChildren()) {
                            if (dataSnapshot.getKey().equalsIgnoreCase("CauHoi")) {
                                for (DataSnapshot cauHoiSnapshot : dataSnapshot.getChildren()
                                ) {
                                    Common.cauHoiArrayList.add(cauHoiSnapshot.getValue(CauHoi.class));
                                }
                            } else {
                                Entity entity = dataSnapshot.getValue(Entity.class);
                                Common.entityList.add(entity);
                            }

                        }


                        reference.child("TheGioi").child(Common.idTheGioi).addValueEventListener(new ValueEventListener() {
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
                reference.child("TheGioi").child(Common.idTheGioi).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Common.entity = snapshot.getValue(Entity.class);
                        startActivity(new Intent(getApplicationContext(), GhepKiHieuActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
    }


    private void init() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        adapter = new ChauLucAdapter(this, entityList);
        binding.recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        reference.child("ChauLuc").child(Common.idTheGioi).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                entityList.clear();
                Common.cauHoiArrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot :
                        snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equalsIgnoreCase("CauHoi")) {
                        for (DataSnapshot snapShotCauHoi : dataSnapshot.getChildren()
                        ) {
                            Common.cauHoiArrayList.add(snapShotCauHoi.getValue(CauHoi.class));
                        }
                    } else {
                        Entity entity = dataSnapshot.getValue(Entity.class);
                        entityList.add(entity);
                    }
                }
                adapter.notifyDataSetChanged();
                adapter.filter("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}