package com.tondz.ghephinh.activity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;
import com.tondz.ghephinh.R;
import com.tondz.ghephinh.adapters.KiHieuAdapter;
import com.tondz.ghephinh.databinding.ActivityGhepKiHieuBinding;
import com.tondz.ghephinh.utils.Common;

import java.util.ArrayList;
import java.util.List;

public class GhepKiHieuActivity extends AppCompatActivity {
    ActivityGhepKiHieuBinding binding;
    private ScaleGestureDetector scaleGestureDetector;
    private float scale = 1f;
    private ImageView currentImageView;
    private float lastTouchX, lastTouchY;
    private List<ImageView> imageViewList = new ArrayList<>();
    int currentIndex = -1;
    KiHieuAdapter adapter;
    private float scaleFactor = 1.0f;
    private float minScaleFactor = 1.0f;  // Tỷ lệ zoom out tối thiểu (kích thước ban đầu)
    private float maxScaleFactor = 3.0f;  // Tỷ lệ zoom in tối đa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGhepKiHieuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        onClick();
        init();
        Picasso.get().load(Common.entity.getSingle_image_url()).into(binding.imgBackground);
    }

    private void init() {
        adapter = new KiHieuAdapter(this, Common.kiHieu.getMultiple_image_urls());
        binding.recyclerView.setAdapter(adapter);
        binding.titile.setText(Common.entity.getName());
    }

    private void showImageInfoDialog(String title, String imageUrl, String info) {
        // Tạo AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(GhepKiHieuActivity.this);

        // Inflate layout cho Dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_image_info, null);
        builder.setView(dialogView);

        // Ánh xạ các thành phần trong layout
        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        ImageView dialogImage = dialogView.findViewById(R.id.dialog_image);
        TextView dialogInfo = dialogView.findViewById(R.id.dialog_info);

        // Thiết lập dữ liệu
        dialogTitle.setText(title);
        dialogInfo.setText(info);

        // Sử dụng Picasso để tải hình ảnh từ URL
        Picasso.get()
                .load(imageUrl)
                .into(dialogImage);

        // Tạo Dialog
        AlertDialog dialog = builder.create();

        // Hiển thị Dialog
        dialog.show();
    }


    @SuppressLint("ClickableViewAccessibility")
    void onClick() {
        binding.btnZoomIn.setOnClickListener(v -> {
            scaleFactor += 0.1f;
            scaleFactor = Math.min(scaleFactor, maxScaleFactor); // Giới hạn tỷ lệ zoom in tối đa
            binding.frameLayout.setScaleX(scaleFactor);
            binding.frameLayout.setScaleY(scaleFactor);
        });

        // Sự kiện nút Zoom Out
        binding.btnZoomOut.setOnClickListener(v -> {
            scaleFactor -= 0.1f;
            scaleFactor = Math.max(scaleFactor, minScaleFactor); // Giới hạn tỷ lệ zoom out tối thiểu (kích thước ban đầu)
            binding.frameLayout.setScaleX(scaleFactor);
            binding.frameLayout.setScaleY(scaleFactor);
        });
        binding.info.setOnClickListener(v -> {
            showImageInfoDialog(Common.entity.getName(), Common.entity.getSingle_image_url(), Common.entity.getInfo());
        });
        binding.frameLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // Xử lý khi bắt đầu kéo
                        return true;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        // Xử lý khi ảnh đang ở trong khu vực thả
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        // Xử lý khi ảnh đang được kéo di chuyển
                        return true;
                    case DragEvent.ACTION_DROP:
                        final float dropX = event.getX();
                        final float dropY = event.getY();
                        ClipData.Item item = event.getClipData().getItemAt(0);
                        Integer chooseIdx = Integer.parseInt(item.getText().toString());
                        ImageView imageView = new ImageView(GhepKiHieuActivity.this);
                        Picasso.get().load(Common.kiHieu.getMultiple_image_urls().get(chooseIdx)).into(imageView);
                        imageView.setScaleX(0.5f);
                        imageView.setScaleY(0.5f);

                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        params.leftMargin = (int) dropX; // Tinh chỉnh vị trí x
                        params.topMargin = (int) dropY;  // Tinh chỉnh vị trí y
                        imageView.setLayoutParams(params);
                        binding.frameLayout.addView(imageView);
                        setTouchListener(imageView);
                        imageViewList.add(imageView);

                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;

                    default:
                        break;
                }
                return false;
            }
        });
        scaleGestureDetector = new ScaleGestureDetector(this, new GhepKiHieuActivity.ScaleListener());
        binding.frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchX = event.getRawX();
                        lastTouchY = event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (event.getPointerCount() == 1) {
                            float dX = event.getRawX() - lastTouchX;
                            float dY = event.getRawY() - lastTouchY;
                            moveAllImages(dX, dY);
                            lastTouchX = event.getRawX();
                            lastTouchY = event.getRawY();
                            break;
                        }

                }


                return true;
            }
        });
    }


    private void moveAllImages(float dX, float dY) {
        for (int i = 0; i < binding.frameLayout.getChildCount(); i++) {
            View child = binding.frameLayout.getChildAt(i);
            if (child instanceof ImageView) {
                float newX = child.getX() + dX;
                float newY = child.getY() + dY;
                child.animate().x(newX).y(newY).setDuration(0).start();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(ImageView imageView) {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            private float lastTouchX;
            private float lastTouchY;
            private float dX, dY;
            private static final long DOUBLE_CLICK_TIMEOUT = 300; // thời gian cho phép giữa hai lần click (300ms)
            private int clickCount = 0;
            private long lastClickTime = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        currentImageView = imageView;
                        lastTouchX = event.getRawX();
                        lastTouchY = event.getRawY();
                        dX = v.getX() - lastTouchX;
                        dY = v.getY() - lastTouchY;
                        int index = imageViewList.indexOf(v);
                        if (index != -1) {
                            currentIndex = index;
                        }

                        // Kiểm tra double click
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastClickTime < DOUBLE_CLICK_TIMEOUT) {
                            clickCount++;
                        } else {
                            clickCount = 1; // Reset click count nếu thời gian giữa các click quá lâu
                        }
                        lastClickTime = currentTime;


                        break;

                    case MotionEvent.ACTION_MOVE:
                        float newX = event.getRawX() + dX;
                        float newY = event.getRawY() + dY;
                        v.setX(newX);
                        v.setY(newY);
                        break;
                }
                return true;
            }
        });
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (currentImageView != null) {
                scale *= detector.getScaleFactor();
                scale = Math.max(0.1f, Math.min(scale, 5.0f)); // Giới hạn scale từ 0.1x đến 5x
                ViewGroup.LayoutParams params = null;
                params = currentImageView.getLayoutParams();
                params.width = (int) (currentImageView.getDrawable().getIntrinsicWidth() * scale);
                params.height = (int) (currentImageView.getDrawable().getIntrinsicHeight() * scale);
                currentImageView.setLayoutParams(params);
                imageViewList.set(currentIndex, currentImageView);
            }

            return true;
        }
    }
}