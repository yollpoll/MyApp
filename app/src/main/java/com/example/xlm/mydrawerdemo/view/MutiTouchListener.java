package com.example.xlm.mydrawerdemo.view;

import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 鹏祺 on 2017/6/1.
 */

public abstract class MutiTouchListener implements View.OnTouchListener {
    private static final long TIME_SPACE = 400;
    private long lastTouchTime = 0;
    private AtomicInteger touchCount = new AtomicInteger(1);

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            final long nowTime = System.currentTimeMillis();
            if (nowTime - lastTouchTime < TIME_SPACE||touchCount.get()==1) {
                //在间隔内点击第二次,点击次数增加
                onMultiTouch(v, event, touchCount.get());
                touchCount.incrementAndGet();
            }
            lastTouchTime=nowTime;
        }
        return true;
    }
    //外部调用的回调
    public abstract void onMultiTouch(View v, MotionEvent event, int count);

}
