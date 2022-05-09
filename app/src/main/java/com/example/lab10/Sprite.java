package com.example.lab10;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class Sprite extends RectF {
    private int dX, dY, color;

    private static final int BMP_ROWS = 4, BMP_COLS = 4; // number of rows/cols in the bitmap image
    private int anim_frame = 0; // which frame of the animation is currently displayed
    private int direction = 0; // defines which row of bitmap is drawn, based on direction of motion

    private Bitmap bitmap; // the bitmap is assigned in DrawView
    public Sprite(float left, float top, float right, float bottom, int dX, int dY, int color) {
        super(left, top, right, bottom);
        this.dX = dX;
        this.dY = dY;
        this.color = color;
    }
    public Sprite(float left, float top, float right, float bottom) {
        this(left, top, right, bottom, 2, 1, Color.BLUE);
    }
    public Sprite() {
        this(0, 0, 250, 250, 1, 2, Color.GREEN);
    }

    public void update(Canvas canvas) {
        // moves sprite
        offset(dX, dY);
        // checks bounds - if at edge, reverses the direction and swaps magnitudes
        if(top < 0 || bottom > canvas.getHeight() || left < 0 || right > canvas.getWidth()) {
            if(top < 0 || bottom > canvas.getHeight())
                dY *= -1;
            if(left < 0 || right > canvas.getWidth())
                dX *= -1;
            // regardless of direction, switches magnitude of x and y
            int yMag = Math.abs(dY);
            int xMag = Math.abs(dX);
            int yDir = 1, xDir = 1;
            if(dY < 0)
                yDir = -1;
            if(dX < 0)
                xDir = -1;
            dY = yDir * xMag;
            dX = xDir * yMag;
            // checks that sprite never leaves the boundaries
            if(left < 0) {
                offsetTo(0, top);
            }
            if(right > canvas.getWidth()) {
                offsetTo(canvas.getWidth() - width(), top);
            }
            if(top < 0) {
                offsetTo(left, 0);
            }
            if(bottom > canvas.getHeight()) {
                offsetTo(left,canvas.getHeight() - height());
            }
        }
        // sets the frame of the animation to the next (making the character appear to walk)
        if(top % 15 == 0)  // changes for every xth pixel it moves to avoid changing too quickly
            anim_frame = (anim_frame + 1) % BMP_COLS;
        // sets the "direction of walking", aka which row of the character sheet is displayed
        if(Math.abs(dX) > Math.abs(dY)) {
            if(dX > 0) {
                direction = 2; // right
            }
            else{
                direction = 1; // left
            }
        }
        else {
            if(dY > 0) {
                direction = 0; // down/toward viewer
            }
            else {
                direction = 3; // back/away from viewer
            }
        }
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);

        // src = the rectangle of the bitmap image that is displayed
        int section_width = bitmap.getWidth() / BMP_COLS;
        int left_bmp = section_width * anim_frame;
        int right_bmp = section_width * (anim_frame + 1);
        int section_height = bitmap.getHeight() / BMP_ROWS;
        int top_bmp = section_height * direction;
        int bottom_bmp = section_height * (direction + 1);
        Rect src = new Rect(left_bmp, top_bmp, right_bmp, bottom_bmp);

        canvas.drawBitmap(bitmap, src, this, paint);
    }

    public int getdX() {
        return dX;
    }

    public void setdX(int dX) {
        this.dX = dX;
    }

    public int getdY() {
        return dY;
    }

    public void setdY(int dY) {
        this.dY = dY;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    // returns the direction that sprite must go to avoid collision
    public int intersects_horiz(Sprite other) {
        if(RectF.intersects(this, other)){
            if(other.left < right && right < other.right)
                return -1;
            if(other.left < left && left < other.right)
                return 1;
        }
        return 0;

    }
    public int intersects_vert(Sprite other) {
        if(RectF.intersects(this, other)){
            if(other.top < bottom && bottom < other.bottom)
                return -1;
            if(other.top < top && top < other.bottom) {
                return 1;
            }
        }
        return 0;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
