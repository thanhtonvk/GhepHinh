package com.tondz.ghephinh.activity.cauhoi;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tondz.ghephinh.R;
import com.tondz.ghephinh.models.CauHoi;
import com.tondz.ghephinh.utils.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CauHoiActivity extends AppCompatActivity {
    List<Animation> dsHieuUng;
    TextView tv_cauhoi, tv_solanxoan;
    Button btn_da1, btn_da2, btn_da3, btn_da4, btn_trove;
    Random random;
    MediaPlayer wrongSound;
    MediaPlayer correctSound;
    int idxCauHoi = -1;
    int diem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cau_hoi);
        dsHieuUng = new ArrayList<>();
        random = new Random();

        init();
        themHieuUng();
        wrongSound = MediaPlayer.create(this, R.raw.wrong_sound);
        correctSound = MediaPlayer.create(this, R.raw.correct_sound);
        tv_cauhoi.setAnimation(dsHieuUng.get(random.nextInt(3)));
        loadCauHoi();
        nextCauHoi();
        onClick();
    }

    private void loadCauHoi() {
        Common.cauHoiArrayList = new ArrayList<>();
        Common.cauHoiArrayList.add(new CauHoi("1", "1", "1", "1", "1", "1", "1"));
    }

    private void nextCauHoi() {
        if (idxCauHoi == Common.cauHoiArrayList.size() - 1) {
            Toast.makeText(this, "Hết câu hỏi", Toast.LENGTH_SHORT).show();
            return;
        } else {
            idxCauHoi += 1;
            Common.CAU_HOI = Common.cauHoiArrayList.get(idxCauHoi);
            tv_cauhoi.setText(Common.CAU_HOI.getCauhoi());
            btn_da1.setText(Common.CAU_HOI.getA());
            btn_da2.setText(Common.CAU_HOI.getB());
            btn_da3.setText(Common.CAU_HOI.getC());
            btn_da4.setText(Common.CAU_HOI.getD());
        }

    }

    private void onClick() {
        btn_trove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_da1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDapAn(btn_da1.getText().toString(), Common.CAU_HOI)) {
                    correctSound.start();
                } else {
                    wrongSound.start();
                }
                nextCauHoi();
            }
        });
        btn_da2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDapAn(btn_da2.getText().toString(), Common.CAU_HOI)) {
                    correctSound.start();
                } else {

                    wrongSound.start();

                }
                nextCauHoi();
            }
        });
        btn_da3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDapAn(btn_da3.getText().toString(), Common.CAU_HOI)) {

                    correctSound.start();

                } else {

                    wrongSound.start();

                }
                nextCauHoi();
            }
        });
        btn_da4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDapAn(btn_da4.getText().toString(), Common.CAU_HOI)) {

                    correctSound.start();

                } else {

                    wrongSound.start();

                }
                nextCauHoi();
            }
        });

    }

    private void themHieuUng() {
        dsHieuUng.add(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        dsHieuUng.add(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down));
        dsHieuUng.add(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
    }

    private void init() {
        tv_cauhoi = findViewById(R.id.tv_cauhoi);
        btn_da1 = findViewById(R.id.btn_da1);
        btn_da2 = findViewById(R.id.btn_da2);
        btn_da3 = findViewById(R.id.btn_da3);
        btn_da4 = findViewById(R.id.btn_da4);
        btn_trove = findViewById(R.id.btn_trove);
        tv_solanxoan = findViewById(R.id.tv_solanxoan);
    }

    private boolean checkDapAn(String dapanchon, CauHoi cauHoi) {
        if (dapanchon.equalsIgnoreCase(cauHoi.getDapan())) return true;
        else return false;
    }

}