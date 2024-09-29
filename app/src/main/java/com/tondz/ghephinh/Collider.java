package com.tondz.ghephinh;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Collider {
    private Bitmap bitmap; // Hình ảnh gốc
    private PointF[] points; // Các điểm xác định hình dạng
    public Collider(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.points = createPoints(bitmap);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }


    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public PointF[] getPoints() {
        return points;
    }

    public void setPoints(PointF[] points) {
        this.points = points;
    }

    // Tạo các điểm xác định hình dạng dựa trên pixel không trong suốt
    private PointF[] createPoints(Bitmap bitmap) {
        // Lấy kích thước hình ảnh
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // Tìm các điểm không trong suốt và lưu vào mảng
        List<PointF> pointList = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (bitmap.getPixel(x, y) != 0) { // Kiểm tra pixel không trong suốt
                    pointList.add(new PointF(x, y));
                }
            }
        }
        return pointList.toArray(new PointF[0]);
    }

    public boolean contains(float x, float y) {
        return pointInPolygon(points, x, y);
    }

    private boolean pointInPolygon(PointF[] points, float x, float y) {
        int i, j;
        boolean result = false;
        for (i = 0, j = points.length - 1; i < points.length; j = i++) {
            if ((points[i].y > y) != (points[j].y > y) &&
                    (x < (points[j].x - points[i].x) * (y - points[i].y) / (points[j].y - points[i].y) + points[i].x)) {
                result = !result;
            }
        }
        return result;
    }
}
