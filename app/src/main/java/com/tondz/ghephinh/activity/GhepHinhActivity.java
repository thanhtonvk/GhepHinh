package com.tondz.ghephinh.activity;

import static com.tondz.ghephinh.utils.Common.entityList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tondz.ghephinh.AreaActivity;
import com.tondz.ghephinh.R;
import com.tondz.ghephinh.activity.cauhoi.CauHoiActivity;
import com.tondz.ghephinh.adapters.ImageAdapter;
import com.tondz.ghephinh.databinding.ActivityAreaBinding;
import com.tondz.ghephinh.databinding.ActivityGhepHinhBinding;
import com.tondz.ghephinh.models.Entity;
import com.tondz.ghephinh.utils.Common;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GhepHinhActivity extends AppCompatActivity {
    ActivityGhepHinhBinding binding;
    private ScaleGestureDetector scaleGestureDetector;
    private float scale = 1f;
    private ImageView currentImageView;
    private float lastTouchX, lastTouchY;
    private List<ImageView> imageViewList = new ArrayList<>();
    private List<TextView> textViewList = new ArrayList<>();
    private List<Integer> textSizeList = new ArrayList<>();
    int currentIndex = -1;
    ImageAdapter adapter;
    private float scaleFactor = 1.0f;
    private float minScaleFactor = 1.0f;  // Tỷ lệ zoom out tối thiểu (kích thước ban đầu)
    private float maxScaleFactor = 3.0f;  // Tỷ lệ zoom in tối đa
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000; // 1 phút
    private boolean canEdit = true;
    private List<Integer> idxTemp = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGhepHinhBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        onClick();
        init();
        showInputTimeDialog();
        loadPreview();
    }

    private void dialogPreview() {

        if (!Common.previewList.isEmpty()) {
            Dialog dialog = new Dialog(GhepHinhActivity.this);
            dialog.setContentView(R.layout.dialog_show_image);
            PhotoView img = dialog.findViewById(R.id.imgPreviewBig);

// Load image using Picasso
            Picasso.get().load(Common.previewList.get(0).getUrl()).into(img);

            dialog.show();

        }

    }

    private void loadPreview() {
        if (!Common.previewList.isEmpty()) {
            Picasso.get().load(Common.previewList.get(0).getUrl()).into(binding.preView);
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                binding.countdownText.setText("Hết thời gian");
            }
        }.start();
    }

    private void updateCountdownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        binding.countdownText.setText(timeFormatted);
    }

    private void showInputTimeDialog() {
        // Sử dụng LayoutInflater để nạp layout dialog_input_time.xml
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_input_time, null);

        // Khai báo EditText từ dialog layout
        EditText editTextTime = dialogView.findViewById(R.id.editTextTime);
        editTextTime.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView) // Đặt view cho dialog
                .setTitle("Bạn đã sẵn sàng chưa?")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Xử lý khi người dùng nhấn "OK"
                    String timeInput = editTextTime.getText().toString();
                    if (!timeInput.isEmpty()) {
                        int time = Integer.parseInt(timeInput);
                        timeLeftInMillis = time * 1000; // Chuyển đổi sang mili giây
                        startTimer();
                    }
                })
                .setNegativeButton("Huỷ", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void init() {
        adapter = new ImageAdapter(this, entityList);
        binding.recyclerView.setAdapter(adapter);
        binding.titile.setText(Common.entity.getName());
    }

    private void showImageInfoDialog(Entity entity) {
        // Tạo AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(GhepHinhActivity.this);

        // Inflate layout cho Dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_image_info, null);
        builder.setView(dialogView);

        // Ánh xạ các thành phần trong layout
        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        ImageView dialogImage = dialogView.findViewById(R.id.dialog_image);
        TextView dialogInfo = dialogView.findViewById(R.id.dialog_info);
        ImageCarousel carousel = dialogView.findViewById(R.id.carousel);
        TextView tvYoutube = dialogView.findViewById(R.id.tvVideo);
        carousel.registerLifecycle(getLifecycle());
        List<CarouselItem> list = new ArrayList<>();
        for (String link : entity.getMultiple_image_urls()
        ) {
            list.add(
                    new CarouselItem(
                            link
                    )
            );
        }
        carousel.setData(list);
        dialogTitle.setText(entity.getName());
        dialogInfo.setText(entity.getInfo());
        Picasso.get()
                .load(entity.getSingle_image_url())
                .into(dialogImage);
        tvYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(entity.getLink())));
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showDialogNote() {
        // Sử dụng LayoutInflater để nạp layout dialog_input_time.xml
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_input_time, null);

        // Khai báo EditText từ dialog layout
        EditText editTextTime = dialogView.findViewById(R.id.editTextTime);
        editTextTime.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView) // Đặt view cho dialog
                .setTitle("Nhập nội dung note: ")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Xử lý khi người dùng nhấn "OK"
                    String noteInput = editTextTime.getText().toString();
                    if (!noteInput.isEmpty()) {
                        // Tạo một TextView mới
                        TextView textView = new TextView(GhepHinhActivity.this);
                        textView.setText(noteInput);
                        textView.setTextSize(20);
                        textView.setTextColor(Color.BLACK);

                        // Đặt LayoutParams cho TextView
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.gravity = Gravity.CENTER; // Đặt vị trí của TextView trong FrameLayout

                        // Kiểm tra nếu TextView đã có parent, nếu có thì loại bỏ nó khỏi parent
                        if (textView.getParent() != null) {
                            ((ViewGroup) textView.getParent()).removeView(textView);
                        }

                        // Thêm TextView vào FrameLayout
                        binding.frameLayout.addView(textView, params);

                        setTouchTextView(textView); // Nếu bạn cần touch listener cho hình ảnh, hãy bật lại dòng này
                        textViewList.add(textView);
                        textSizeList.add(20);
                    }
                })
                .setNegativeButton("Huỷ", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private Bitmap captureFullScreen(Activity activity) {
        View rootView = activity.getWindow().getDecorView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private void saveBitmapToGallery(Bitmap bitmap) {
        String savedImagePath = null;
        String imageFileName = "screenshot_" + System.currentTimeMillis() + ".jpg";

        // Thư mục lưu ảnh
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Screenshots");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File imageFile = new File(storageDir, imageFileName);
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
            savedImagePath = imageFile.getAbsolutePath();

            // Thêm vào thư viện
            MediaScannerConnection.scanFile(this, new String[]{savedImagePath}, null, null);

            Toast.makeText(this, "Ảnh đã lưu vào: " + savedImagePath, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    void onClick() {
        binding.preView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPreview();
            }
        });
        binding.btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap screenshot = captureFullScreen(GhepHinhActivity.this);
                saveBitmapToGallery(screenshot);
            }
        });
        binding.btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogNote();
            }
        });
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
            showImageInfoDialog(Common.entity);
        });
        binding.question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CauHoiActivity.class));
            }
        });
        binding.btnZoomTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImageView != null) {
                    float maxScale = 5.0f; // Tỉ lệ phóng to tối đa
                    float minScale = 0.5f; // Tỉ lệ thu nhỏ tối thiểu

                    scaleFactor += 0.1f;
                    scale = Math.max(minScale, Math.min(scaleFactor, maxScale));

                    ViewGroup.LayoutParams params = currentImageView.getLayoutParams();
                    params.width = (int) (currentImageView.getDrawable().getIntrinsicWidth() * scale);
                    params.height = (int) (currentImageView.getDrawable().getIntrinsicHeight() * scale);
                    currentImageView.setLayoutParams(params);

                    imageViewList.set(currentIndex, currentImageView);
                }
                if (currentTextView != null) {
                    int idx = textViewList.indexOf(currentTextView);
                    int currentSize = textSizeList.get(idx);
                    currentSize += 1;
                    textSizeList.set(idx, currentSize);
                    currentTextView.setTextSize(currentSize);
                    textViewList.set(idx, currentTextView);

                }
            }
        });
        binding.btnZoomNho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentImageView != null) {
                    float maxScale = 5.0f; // Tỉ lệ phóng to tối đa
                    float minScale = 0.5f; // Tỉ lệ thu nhỏ tối thiểu

                    scaleFactor -= 0.01f;
                    scale = Math.max(minScale, Math.min(scaleFactor, maxScale));

                    ViewGroup.LayoutParams params = currentImageView.getLayoutParams();
                    params.width = (int) (currentImageView.getDrawable().getIntrinsicWidth() * scale);
                    params.height = (int) (currentImageView.getDrawable().getIntrinsicHeight() * scale);
                    currentImageView.setLayoutParams(params);

                    imageViewList.set(currentIndex, currentImageView);
                }
                if (currentTextView != null) {
                    int idx = textViewList.indexOf(currentTextView);
                    int currentSize = textSizeList.get(idx);
                    currentSize -= 1;
                    textSizeList.set(idx, currentSize);
                    currentTextView.setTextSize(currentSize);
                    textViewList.set(idx, currentTextView);

                }
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

                        String name = item.getText().toString();
                        Log.e("TAG", "onDrag: name " + name);
                        int chooseIdx = getIndex(name);

                        if (chooseIdx >= 0) {
                            ImageView imageView = new ImageView(GhepHinhActivity.this);
                            Picasso.get().load(entityList.get(chooseIdx).getSingle_image_url()).into(imageView);
                            imageView.setScaleX(0.5f);
                            imageView.setScaleY(0.5f);

                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                            params.leftMargin = (int) dropX; // Tinh chỉnh vị trí x
                            params.topMargin = (int) dropY;  // Tinh chỉnh vị trí y
                            imageView.setLayoutParams(params);
                            binding.frameLayout.addView(imageView);
                            setTouchListener(imageView);
                            imageViewList.add(imageView);
                            idxTemp.add(chooseIdx);
                            Log.e("TAG", "onDrag: " + entityList.get(chooseIdx).getName());
                        }

                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;

                    default:
                        break;
                }
                return false;
            }
        });
        scaleGestureDetector = new ScaleGestureDetector(this, new GhepHinhActivity.ScaleListener());
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

    private int getIndex(String name) {
        for (Entity entity : entityList
        ) {
            if (String.valueOf(entity.getName()).equalsIgnoreCase(String.valueOf(name))) {
                return entityList.indexOf(entity);
            }
        }
        return -1;
    }

    private void moveAllImages(float dX, float dY) {
        for (int i = 0; i < binding.frameLayout.getChildCount(); i++) {
            View child = binding.frameLayout.getChildAt(i);
            if (child instanceof ImageView || child instanceof TextView) {
                float newX = child.getX() + dX;
                float newY = child.getY() + dY;
                child.animate().x(newX).y(newY).setDuration(0).start();
            }
        }
    }

    TextView currentTextView;

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchTextView(TextView textView) {
        textView.setOnTouchListener(new View.OnTouchListener() {
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
                        currentImageView = null;
                        currentTextView = textView;
                        lastTouchX = event.getRawX();
                        lastTouchY = event.getRawY();
                        dX = v.getX() - lastTouchX;
                        dY = v.getY() - lastTouchY;
                        int index = textViewList.indexOf(v);
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

                        // Kiểm tra nếu là double click
                        if (clickCount == 2) {
                            int idx = textViewList.indexOf(currentTextView);
                            Log.e("TAG", "onTouch: " + idx);
                            showDialogTextView(idx);
                            clickCount = 0; // Reset sau khi phát hiện double click
                        }

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
                        currentTextView = null;
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

                        // Kiểm tra nếu là double click
                        if (clickCount == 2) {
                            int idx = idxTemp.get(imageViewList.indexOf(currentImageView));
                            Log.e("TAG", "onTouch: " + idx);
                            showDialogArea(idx);
                            clickCount = 0; // Reset sau khi phát hiện double click
                        }

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

    private void showDialogTextView(int idx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GhepHinhActivity.this);
        builder.setTitle(textViewList.get(idx).getText().toString());


        builder.setNegativeButton("Xoá", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.frameLayout.removeView(textViewList.get(idx));
                textViewList.remove(idx);
                textSizeList.remove(idx);
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        // Tạo và hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDialogArea(int idx) {
        Entity entity = entityList.get(idx);

        AlertDialog.Builder builder = new AlertDialog.Builder(GhepHinhActivity.this);
        builder.setTitle(entityList.get(idx).getName());
        builder.setMessage(entityList.get(idx).getInfo());

        // Các tùy chọn khác cho dialog
        builder.setPositiveButton("Xem chi tiết", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showImageInfoDialog(entity);
            }
        });

        builder.setNegativeButton("Xoá", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.frameLayout.removeView(imageViewList.get(currentIndex));
                imageViewList.remove(currentIndex);
                idxTemp.remove(currentIndex);
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        // Tạo và hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (currentImageView != null) {
                scale *= detector.getScaleFactor();
                scale = Math.max(0.1f, Math.min(scale, 10.0f)); // Giới hạn scale từ 0.1x đến 5x
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