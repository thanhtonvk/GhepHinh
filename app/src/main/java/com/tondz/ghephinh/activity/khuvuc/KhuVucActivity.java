package com.tondz.ghephinh.activity.khuvuc;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tondz.ghephinh.R;
import com.tondz.ghephinh.activity.GhepHinhActivity;
import com.tondz.ghephinh.activity.GhepKiHieuActivity;
import com.tondz.ghephinh.adapters.KiHieuTextAdapter;
import com.tondz.ghephinh.databinding.ActivityKhuVucBinding;
import com.tondz.ghephinh.models.CauHoi;
import com.tondz.ghephinh.models.Entity;
import com.tondz.ghephinh.models.HinhNen;
import com.tondz.ghephinh.models.Preview;
import com.tondz.ghephinh.utils.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KhuVucActivity extends AppCompatActivity {

    ActivityKhuVucBinding binding;
    DatabaseReference reference;
    FirebaseDatabase database;
    List<Entity> entityList = new ArrayList<>();
    KhuVucAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKhuVucBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        loadData();
        onClick();
    }

    private void onClick() {
        binding.btnGhepHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child("KhuVuc").child(Common.idQuocGia).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Common.entityList = new ArrayList<>();
                        Common.cauHoiArrayList = new ArrayList<>();
                        Common.previewList.clear();
                        Common.hinhGhepList.clear();
                        for (DataSnapshot dataSnapshot :
                                snapshot.getChildren()) {
                            if (dataSnapshot.getKey().equalsIgnoreCase("CauHoi")) {
                                for (DataSnapshot cauHoiSnapshot : dataSnapshot.getChildren()
                                ) {
                                    Map<String, Object> map = (Map<String, Object>) cauHoiSnapshot.getValue();
                                    CauHoi cauHoi = new CauHoi(map.get("id").toString(), map.get("cauhoi").toString(), map.get("a").toString(), map.get("b").toString(), map.get("c").toString(), map.get("d").toString(), map.get("dapan").toString());
                                    Common.cauHoiArrayList.add(cauHoi);
                                }
                            } else if (dataSnapshot.getKey().equalsIgnoreCase("Preview")) {
                                for (DataSnapshot previewSnapshot : dataSnapshot.getChildren()
                                ) {
                                    Preview preview = previewSnapshot.getValue(Preview.class);
                                    Common.previewList.add(preview);
                                }
                            } else if (dataSnapshot.getKey().equalsIgnoreCase("HinhNen")) {
                                for (DataSnapshot hinhNenSnapshot : dataSnapshot.getChildren()
                                ) {
                                    Common.hinhGhepList.add(hinhNenSnapshot.getValue(HinhNen.class));
                                }

                            } else {
                                Entity entity = dataSnapshot.getValue(Entity.class);
                                Common.entityList.add(entity);
                            }

                        }
                        reference.child("QuocGia").child(Common.idChauLuc).child(Common.idQuocGia).addValueEventListener(new ValueEventListener() {
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
                reference.child("QuocGia").child(Common.idChauLuc).child(Common.idQuocGia).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Common.entity = snapshot.getValue(Entity.class);
                        loadData();
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

    private void dialogKiHieu() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ki_hieu);
        ListView listView = dialog.findViewById(R.id.listView);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, Common.loaiKiHieuList.toArray());
        listView.setAdapter(adapter);
        dialog.show();
    }

    private void init() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        adapter = new KhuVucAdapter(this, entityList);
        binding.recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        reference.child("KhuVuc").child(Common.idQuocGia).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                entityList.clear();
                Common.hinhGhepList.clear();
                Common.cauHoiArrayList = new ArrayList<>();
                Common.previewList.clear();
                for (DataSnapshot dataSnapshot :
                        snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equalsIgnoreCase("CauHoi")) {
                        for (DataSnapshot snapShotCauHoi : dataSnapshot.getChildren()
                        ) {
                            Map<String, Object> map = (Map<String, Object>) snapShotCauHoi.getValue();
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
                adapter.notifyDataSetChanged();
                adapter.filter("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}