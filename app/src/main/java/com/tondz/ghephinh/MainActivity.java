package com.tondz.ghephinh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import java.util.HashSet;

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
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        startActivity(new Intent(getApplicationContext(), TheGioiActivity.class));
        finish();

    }

    private void load() {
        reference.child("KiHieu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Common.loaiKiHieuList = new HashSet<>();
                Log.e("TAG", snapshot.toString());
                for (DataSnapshot dataSnapshot :
                        snapshot.getChildren()) {
                    KiHieu kiHieu = dataSnapshot.getValue(KiHieu.class);
                    Common.loaiKiHieuList.add(kiHieu.getGroup());
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
