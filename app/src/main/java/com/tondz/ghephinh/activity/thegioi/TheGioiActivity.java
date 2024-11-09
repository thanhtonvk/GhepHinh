package com.tondz.ghephinh.activity.thegioi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tondz.ghephinh.adapters.AreaAdapter;
import com.tondz.ghephinh.databinding.ActivityTheGioiBinding;
import com.tondz.ghephinh.models.Entity;

import java.util.ArrayList;
import java.util.List;

public class TheGioiActivity extends AppCompatActivity {
    ActivityTheGioiBinding binding;
    DatabaseReference reference;
    FirebaseDatabase database;
    List<Entity> entityList = new ArrayList<>();
    TheGioiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTheGioiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        loadData();
    }

    private void init() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        adapter = new TheGioiAdapter(TheGioiActivity.this, entityList);
        binding.recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        reference.child("TheGioi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                entityList.clear();
                for (DataSnapshot dataSnapshot :
                        snapshot.getChildren()) {
                    Entity entity = dataSnapshot.getValue(Entity.class);
                    entityList.add(entity);
                }
                loadVietNam();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadVietNam() {
        reference.child("QuocGia").child("0").child("41").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Entity entity = snapshot.getValue(Entity.class);
                entityList.add(entity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}