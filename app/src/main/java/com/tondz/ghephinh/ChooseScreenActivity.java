package com.tondz.ghephinh;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tondz.ghephinh.adapters.AreaAdapter;
import com.tondz.ghephinh.databinding.ActivityChooseScreenBinding;
import com.tondz.ghephinh.models.Entity;

import java.util.ArrayList;
import java.util.List;

public class ChooseScreenActivity extends AppCompatActivity {
    ActivityChooseScreenBinding binding;
    AreaAdapter adapter;
    List<Entity> entityList = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        loadData();

    }

    private void loadData() {
        reference.addValueEventListener(new ValueEventListener() {
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

    private void init() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("data");
        adapter = new AreaAdapter(this, entityList);
        binding.recyclerView.setAdapter(adapter);

    }
}