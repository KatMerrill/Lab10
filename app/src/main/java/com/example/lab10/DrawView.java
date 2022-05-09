package com.example.lab10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawView extends SurfaceView implements Runnable {
    private boolean isRunning=true;
    private Context myContext;
    private SurfaceHolder mySurfaceHolder;
    private Thread myThread = null;
    Sprite sprite1, sprite2;

    public DrawView(Context context) {
        super(context);
    }
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        myContext = context;
        mySurfaceHolder = getHolder();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        sprite1 = new Sprite();
        sprite1.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sprite2));
        sprite2 = new Sprite(100,500,350,750);
        sprite2.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sprite3));

    }

    @Override
    // runs on a separate Thread
    public void run() {
        Canvas canvas;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        while(isRunning) {
            if(mySurfaceHolder == null) {
                return;
            }
            if(mySurfaceHolder.getSurface().isValid()) {
                canvas = mySurfaceHolder.lockCanvas();
                if(canvas != null) {
                    canvas.save();
                    try {
                        canvas.drawBitmap(bitmap, 0,0, new Paint());
                        drawSprites(canvas);
                    } finally {  //unlock and release canvas
                        canvas.restore();
                        mySurfaceHolder.unlockCanvasAndPost(canvas);
                    }

                }
            }
        }
    }
    // pause and resume are used by MainActivity
    public void pause() {
        isRunning = false;
        try {
            // Stop the thread == rejoin the main thread.
            myThread.join();
        } catch (InterruptedException e) {
        }
    }
    public void resume() {
        isRunning = true;
        myThread = new Thread(this);
        myThread.start();
    }
    public void drawSprites(Canvas canvas){
        sprite1.update(canvas);
        sprite1.draw(canvas);

        sprite2.update(canvas);
        sprite2.draw(canvas);

        // collision checks
        int dir = sprite1.intersects_horiz(sprite2);
        if(dir != 0) {
            sprite1.setdX(Math.abs(sprite1.getdX()) * dir);
        }
        dir = sprite2.intersects_horiz(sprite1);
        if(dir != 0) {
            sprite2.setdX(Math.abs(sprite2.getdX()) * dir);
        }
        dir = sprite1.intersects_vert(sprite2);
        if(dir != 0) {
            sprite1.setdY(Math.abs(sprite1.getdY()) * dir);
        }
        dir = sprite2.intersects_vert(sprite1);
        if(dir != 0) {
            sprite2.setdY(Math.abs(sprite2.getdY()) * dir);
        }
    }
}
