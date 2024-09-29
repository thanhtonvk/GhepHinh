package com.tondz.ghephinh;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

public class Sprite {
    private Bitmap bitmap;
    private int x, y; // Tọa độ của sprite
    private int width, height;

    public Sprite(Bitmap bitmap, int x, int y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

    // Kiểm tra va chạm với một sprite khác
    public boolean isColliding(Sprite other) {
        // Kiểm tra nếu bounding box có chồng lên nhau
        if (!Rect.intersects(getBoundingBox(), other.getBoundingBox())) {
            return false; // Không va chạm
        }

        // Lấy vùng va chạm
        Rect intersection = new Rect();
        Rect.intersects(getBoundingBox(), other.getBoundingBox());

        // Kiểm tra từng pixel trong vùng va chạm
        for (int i = intersection.left; i < intersection.right; i++) {
            for (int j = intersection.top; j < intersection.bottom; j++) {
                // Lấy màu pixel của cả hai sprite
                int pixel1 = bitmap.getPixel(i - x, j - y);
                int pixel2 = other.bitmap.getPixel(i - other.x, j - other.y);

                // Nếu cả hai pixel không trong suốt, có va chạm
                if (Color.alpha(pixel1) != 0 && Color.alpha(pixel2) != 0) {
                    return true; // Va chạm
                }
            }
        }
        return false; // Không va chạm
    }

    public Rect getBoundingBox() {
        return new Rect(x, y, x + width, y + height);
    }

}
