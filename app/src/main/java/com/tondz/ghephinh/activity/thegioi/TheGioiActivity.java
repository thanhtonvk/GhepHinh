package com.tondz.ghephinh.activity.thegioi;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tondz.ghephinh.adapters.AreaAdapter;
import com.tondz.ghephinh.databinding.ActivityTheGioiBinding;
import com.tondz.ghephinh.models.CauHoi;
import com.tondz.ghephinh.models.Entity;
import com.tondz.ghephinh.utils.Common;

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
                entity.setSingle_image_url("https://firebasestorage.googleapis.com/v0/b/baucucanbo.appspot.com/o/z6037342593973_4ec952788714dae8137f0b953d8ba898.gif?alt=media&token=dab222d9-2992-48ed-bebc-8dab1fca915e");
                entityList.add(entity);
                Log.e("TAG", "onDataChange: " + snapshot);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}