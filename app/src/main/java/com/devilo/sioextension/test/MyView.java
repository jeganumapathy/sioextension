package com.devilo.sioextension.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

/**
 * Created by Administrator on 11/23/2016.
 */
public class MyView extends View implements SurfaceHolder.Callback {

    private Paint mPaint;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private float radius = 5;
    private SurfaceHolder surfaceHolder;
    private Bitmap bmpIcon;
    private static final float MINP = 0.25f;
    private static final float MAXP = 0.75f;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private Paint mBitmapPaint;

    public MyView(Context c) {
        super(c);
        init();
    }

    private void init() {
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
        mEmboss = new EmbossMaskFilter(new float[]{1, 1, 1},
                0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
        bmpIcon = BitmapFactory.decodeResource(getResources(),
                android.R.drawable.ic_btn_speak_now
        );

    }

    TestActivity.TouchEvent touch;

    public void setTouch(TestActivity.TouchEvent touch) {
        this.touch = touch;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFAAAAAA);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        //canvas.drawPath(mPath, mPaint);
        canvas.drawCircle(mX, mY, radius, mPaint);
    }


    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    public void touch_start(float x, float y) {
        // mPath.reset();
        //mPath.moveTo(x, y);
        mX = x;
        mY = y;

    }

    public void touch_start_inv(float x, float y) {
        // mPath.reset();
        //mPath.moveTo(x, y);
        mX = x;
        mY = y;
        invalidate();
    }


    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mX = x;
            mY = y;
        }
        if (touch != null)
            touch.callback(x, y);
    }

    private void touch_up() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Canvas canvas = surfaceHolder.lockCanvas(null);
        surfaceHolder.unlockCanvasAndPost(canvas);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
