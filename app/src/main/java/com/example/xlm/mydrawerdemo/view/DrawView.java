package com.example.xlm.mydrawerdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private Bitmap imgCache, fakeCache;
    private boolean isShowCache = false;

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
        //更新缓存
        fakeCache = Bitmap.createBitmap(imgCache);

        mPaint.setColor(color);
        mPath.reset();

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

    public Bitmap getBitmapCache() {
        return fakeCache;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (null != fakeCache) {
            //每次绘制都会把缓存中的绘制一遍
            RectF rectF = new RectF(0, 0, fakeCache.getWidth(), fakeCache.getHeight());
            canvas.drawBitmap(fakeCache, null, rectF, mPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mCanvas) {
            //新建一个canvas 模仿自带的画一样的图
            imgCache = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(imgCache);
        }
        if (null != fakeCache) {
            draw(canvas);
        }
        if (null != mCanvas) {
            //自定义一个canvas抄袭绘制过程并且保存在bitmap中
            mCanvas.drawColor(bgColor);
            mCanvas.drawPath(mPath, mPaint);
        }
        canvas.drawColor(bgColor);
        canvas.drawPath(mPath, mPaint);
        mPath.moveTo(lastX, lastY);
    }

    private Paint mPaint;
    private Path mPath;
    private int bgColor = Color.WHITE;
    private Canvas mCanvas;
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
