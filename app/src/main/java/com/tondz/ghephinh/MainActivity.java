package com.tondz.ghephinh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tondz.ghephinh.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    ActivityMainBinding binding;
    private ScaleGestureDetector scaleGestureDetector;
    private float scale = 1f;
    private ImageView currentImageView;
    private float lastTouchX, lastTouchY;
    private List<Collider> customColliders = new ArrayList<>();
    private List<ImageView> imageViewList = new ArrayList<>();
    private List<Sprite> spriteList = new ArrayList<>();
    int currentIndex = -1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        onClick();
    }

    @SuppressLint("ClickableViewAccessibility")
    void onClick() {
        binding.btnAdd.setOnClickListener(v -> openGallery());
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        // Gán OnTouchListener cho FrameLayout
        binding.frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                boolean flag = false;
                for (int i = 0; i < binding.frameLayout.getChildCount(); i++) {
                    ImageView imageView = (ImageView) binding.frameLayout.getChildAt(i);
                    if (isPointInsideView(event.getX(), event.getY(), imageView)) {
                        flag = true;
                    }
                }
                if (!flag) {
                    currentImageView = null;
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            lastTouchX = event.getRawX();
                            lastTouchY = event.getRawY();
                            break;

                        case MotionEvent.ACTION_MOVE:
                            if (event.getPointerCount() == 1) {
                                float dX = event.getRawX() - lastTouchX;
                                float dY = event.getRawY() - lastTouchY;

                                // Kiểm tra xem có hình ảnh nào đang được chạm vào không
                                if (currentImageView != null) {
                                    // Nếu có hình ảnh đang được chạm vào, di chuyển nó
                                    float newX = currentImageView.getX() + dX;
                                    float newY = currentImageView.getY() + dY;
                                    currentImageView.animate().x(newX).y(newY).setDuration(0).start();
                                } else {
                                    // Nếu không có hình ảnh đang được chạm vào, di chuyển tất cả các hình ảnh
                                    moveAllImages(dX, dY);
                                }

                                lastTouchX = event.getRawX();
                                lastTouchY = event.getRawY();
                                break;
                            }

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

    private boolean isPointInsideView(float x, float y, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        float viewX = location[0];
        float viewY = location[1];

        return (x >= viewX && x <= viewX + view.getWidth() &&
                y >= viewY && y <= viewY + view.getHeight());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                float aspectRatio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
                int newHeight = Math.round(500 * aspectRatio); // Chiều cao mới dựa trên chiều rộng 200 pixel
                bitmap = Bitmap.createScaledBitmap(bitmap, 500, newHeight, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Tạo một ImageView mới để hiển thị hình ảnh
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bitmap);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT));

            binding.frameLayout.addView(imageView);
            setTouchListener(imageView);
            customColliders.add(new Collider(bitmap));
            imageViewList.add(imageView);

            spriteList.add(new Sprite(bitmap, (int) imageView.getX(), (int) imageView.getY()));

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(ImageView imageView) {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            private float lastTouchX;
            private float lastTouchY;
            private float dX, dY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        currentImageView = imageView;
                        lastTouchX = event.getRawX();
                        lastTouchY = event.getRawY();
                        dX = v.getX() - lastTouchX;
                        dY = v.getY() - lastTouchY;
                        int index = imageViewList.indexOf(v); // Lấy index của ImageView
                        if (index != -1) {
                            currentIndex = index;
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float newX = event.getRawX() + dX;
                        float newY = event.getRawY() + dY;
                        boolean flag = isCollision();
                        Log.e("TAG", "collision: " + flag);

                        v.animate()
                                .x(newX)
                                .y(newY)
                                .setDuration(0)
                                .start();


                        break;
                }
                return true;
            }
        });
    }

    private boolean isCollision() {
        boolean flag = false;
        for (int i = 0; i < spriteList.size(); i++) {
            if (i == currentIndex) continue;
            else {
                Sprite sprite = spriteList.get(i);
                Log.e("TAG", "check collision: ");
                Log.d("Collision", "Sprite A: " + sprite.getBoundingBox().toShortString() +
                        " Sprite B: " + spriteList.get(currentIndex).getBoundingBox().toShortString());
                if (sprite.isColliding(spriteList.get(currentIndex))) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    private void checkCollisions() {
        for (int i = 0; i < spriteList.size(); i++) {
            Sprite spriteA = spriteList.get(i);
            for (int j = i + 1; j < spriteList.size(); j++) {
                Sprite spriteB = spriteList.get(j);
                if (spriteA.isColliding(spriteB)) {

                }
            }
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            scale *= detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 5.0f)); // Giới hạn scale từ 0.1x đến 5x
            ViewGroup.LayoutParams params = null;
            params = currentImageView.getLayoutParams();
            params.width = (int) (currentImageView.getDrawable().getIntrinsicWidth() * scale);
            params.height = (int) (currentImageView.getDrawable().getIntrinsicHeight() * scale);
            currentImageView.setLayoutParams(params);
            imageViewList.set(currentIndex, currentImageView);
            Bitmap bm = ((BitmapDrawable) currentImageView.getDrawable()).getBitmap();
            Collider collider = new Collider(bm);
            customColliders.set(currentIndex, collider);

            return true;
        }
    }
}
