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
import com.tondz.ghephinh.models.HinhNen;
import com.tondz.ghephinh.models.Preview;
import com.tondz.ghephinh.utils.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                Common.hinhGhepList.clear();
                Common.cauHoiArrayList = new ArrayList<>();
                Common.previewList.clear();
                for (DataSnapshot dataSnapshot :
                        snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equalsIgnoreCase("CauHoi")) {
                        for (DataSnapshot cauHoiSnapshot : dataSnapshot.getChildren()
                        ) {
                            Map<String, Object> map = (Map<String, Object>) cauHoiSnapshot.getValue();
                            CauHoi cauHoi = new CauHoi(map.get("id").toString(), map.get("cauhoi").toString(), map.get("a").toString(), map.get("b").toString(), map.get("c").toString(), map.get("d").toString(), map.get("dapan").toString());
                            Common.cauHoiArrayList.add(cauHoi);
                        }
                    } else if (dataSnapshot.getKey().equalsIgnoreCase("HinhNen")) {

                        for (DataSnapshot snapShotCauHoi : dataSnapshot.getChildren()
                        ) {
                            Common.hinhGhepList.add(snapShotCauHoi.getValue(HinhNen.class));
                        }

                    } else if (dataSnapshot.getKey().equalsIgnoreCase("Preview")) {
                        for (DataSnapshot previewSnapshot : dataSnapshot.getChildren()
                        ) {
                            Common.previewList.add(previewSnapshot.getValue(Preview.class));
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
                entity.setSingle_image_url("https://firebasestorage.googleapis.com/v0/b/baucucanbo.appspot.com/o/z6057632794328_62598c90f97c1a51b0b29254bfe0fc45.jpg?alt=media&token=a1bd7a97-be5b-49ae-9e7b-d4573fe3e38d");
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