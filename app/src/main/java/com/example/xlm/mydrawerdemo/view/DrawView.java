package com.example.xlm.mydrawerdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.utils.Tools;


/**
 * Created by 鹏祺 on 2017/6/19.
 * 涂鸦板
 */

public class DrawView extends android.support.v7.widget.AppCompatImageView {
    private Bitmap imgCache;

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setPaintColor(int color) {
        mPaint.setColor(color);
        mPath.reset();
        imgCache = getDrawingCache();
        Log.d("spq", ">>>>>>>>>.");
    }

    public void setPaintWidth(int dp) {
        mPaint.setStrokeWidth(Tools.calculateDpToPx(dp, getContext()));
    }

    public void setBackGround(int bgColor) {
        this.bgColor = bgColor;
        postInvalidate();
    }

    public void setBackGround(Bitmap bitmap) {
//        bpBackGround=bitmap;
        setImageBitmap(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawLine(lastX, lastY, nowX, nowY, mPaint);
        if (null != imgCache) {
            Log.d("spq", "width>>>>>>>>>>>>>>>" + imgCache.getWidth() + "height>>>>>>>>" + imgCache.getHeight());
            RectF rectF = new RectF(0, 0, imgCache.getWidth(), imgCache.getHeight());
            canvas.drawBitmap(imgCache, null, rectF, mPaint);
            canvas.drawPath(mPath, mPaint);
        }
        canvas.drawColor(bgColor);
        canvas.drawPath(mPath, mPaint);
        mPath.moveTo(lastX, lastY);
    }

    private Paint mPaint;
    private Path mPath;
    private int bgColor = Color.WHITE;
//    private Bitmap bpBackGround ;

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.black));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(20);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPath = new Path();
        setDrawingCacheEnabled(true);
    }

    private float lastX = 0, lastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (lastX == 0 && lastY == 0) {
                    lastX = event.getX();
                    lastY = event.getY();
                    mPath.moveTo(lastX, lastY);
                } else {
                    mPath.lineTo(event.getX(), event.getY());
                }
                postInvalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (lastX == 0 && lastY == 0) {
                    lastX = event.getX();
                    lastY = event.getY();
                    mPath.moveTo(lastX, lastY);
                } else {
                    lastX = event.getX();
                    lastY = event.getY();
                    mPath.lineTo(lastX, lastY);
                }
                postInvalidate();
                return true;
            case MotionEvent.ACTION_UP:
                lastX = 0;
                lastY = 0;
                return true;
            default:
                return true;
        }

    }
}
