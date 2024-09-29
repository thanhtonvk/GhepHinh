package com.tondz.ghephinh;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class ZoomableImageView extends androidx.appcompat.widget.AppCompatImageView {
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();
    private float[] lastEvent = null;
    private PointF start = new PointF();
    private float minScale = 1f;
    private float maxScale = 5f;
    private float scale = 1f;
    private float[] matrixValues = new float[9];

    public ZoomableImageView(Context context) {
        super(context);
        init(context);
    }

    public ZoomableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        setOnTouchListener((v, event) -> {
            scaleGestureDetector.onTouchEvent(event);
            return handleTouch(event);
        });
    }

    private boolean handleTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start.set(event.getX(), event.getY());
                lastEvent = null;
                break;

            case MotionEvent.ACTION_MOVE:
                if (lastEvent != null) {
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;

                    // Apply translation
                    matrix.postTranslate(dx, dy);
                    setImageMatrix(matrix);
                    start.set(event.getX(), event.getY());
                }
                break;
        }
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            scale = Math.max(minScale, Math.min(scale, maxScale));

            matrix.setScale(scale, scale);
            setImageMatrix(matrix);
            return true;
        }
    }
}
