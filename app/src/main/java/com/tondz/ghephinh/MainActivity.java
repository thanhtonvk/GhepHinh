package com.tondz.ghephinh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tondz.ghephinh.activity.thegioi.TheGioiActivity;
import com.tondz.ghephinh.databinding.ActivityMainBinding;
import com.tondz.ghephinh.models.KiHieu;
import com.tondz.ghephinh.utils.Common;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        load();
        startActivity(new Intent(getApplicationContext(), TheGioiActivity.class));
    }

    private void load() {
        reference.child("Icon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Common.kiHieuList = new ArrayList<>();
                for (DataSnapshot dataSnapshot :
                        snapshot.getChildren()) {
                    Common.kiHieuList.add(dataSnapshot.getValue(KiHieu.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }
}
