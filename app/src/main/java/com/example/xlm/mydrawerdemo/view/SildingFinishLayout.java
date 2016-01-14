package com.example.xlm.mydrawerdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by xlm on 2016/1/14.
 */
public class SildingFinishLayout extends RelativeLayout implements View.OnTouchListener {
    /*
    父控件，滑动是调用父控件里的srollby方法
     */
    private ViewGroup mParentView;
    /*
    处理滑动逻辑的view
     */
    private View touchView;
    /*
    处理滑动的最小距离
     */
    private int mTouchSlop;
    /*
        按下点的x坐标
     */
    private int downX;
    /*
        按下点的y坐标
     */
    private int downY;
    /*
        临时存储x坐标
     */
    private int tempX;

    /*
        滚动类
     */
    private Scroller mScroller;

    /*
     layout的宽度
     */
    private int viewWidth;

    /*
    是否正在滑动
     */
    private boolean isSilding;

    /*
    是否结束
     */
    private boolean isFinish;
    private OnSildingFinishListener onSildingFinishListener;

    public interface OnSildingFinishListener {
        public void onSildingFinish();
    }

    public SildingFinishLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }

    public SildingFinishLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            mParentView = (ViewGroup) this.getParent();
            viewWidth = this.getWidth();
        }
    }

    /*
        设置回调，在onSildingFinish()方法中finish activity
     */
    public void setOnSildingFinishListener(OnSildingFinishListener onSildingFinishListener) {
        this.onSildingFinishListener = onSildingFinishListener;

    }

    public View getTouchView() {
        return touchView;
    }

    /*
    设置 touch的view
     */
    public void setTouchView(View touchView) {
        this.touchView = touchView;
        touchView.setOnTouchListener(this);
    }

    /*
    滚出界面
     */
    private void scrollRight() {
        final int delta = (viewWidth + mParentView.getScrollX());
        mScroller.startScroll(mParentView.getScrollX(), 0, -delta + 1, 0, Math.abs(delta));
        postInvalidate();
    }

    /*
    滚回起始位置
     */
    private void scrollOrigin() {
        int delta = mParentView.getScrollX();
        mScroller.startScroll(mParentView.getScrollX(), 0, -delta, 0,
                Math.abs(delta));
        postInvalidate();
    }
    /*
    touch的view是否是AbsListView
     */
    private boolean isTouchOnAbsListView(){
        return touchView instanceof AbsListView ? true:false;
    }
    /**
     * touch的view是否是ScrollView或者其子类
     *
     * @return
     */
    private boolean isTouchOnScrollView() {
        return touchView instanceof ScrollView ? true : false;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX=tempX= (int) event.getRawX();
                downY= (int) event.getRawY();
                break;
            case  MotionEvent.ACTION_MOVE:
                int moveX= (int) event.getRawX();
                int deltaX=tempX-moveX;
                tempX=moveX;
                if(Math.abs(moveX-downX)>mTouchSlop&&Math.abs((int) event.getRawY() - downY) < mTouchSlop){
                    isSilding = true;
                    //如果点击的是abslistview，则取消item的点击事件
                    //不然随着滑动也会触发item的点击事件
                    if(isTouchOnAbsListView()){
                        MotionEvent cancelEvent=MotionEvent.obtain(event);
                        cancelEvent.setAction(MotionEvent.ACTION_CANCEL
                                | (event.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                        v.onTouchEvent(cancelEvent);
                    }
                }

                if (moveX - downX >= 0 && isSilding) {
                    mParentView.scrollBy(deltaX, 0);

                    // 屏蔽在滑动过程中ListView ScrollView等自己的滑动事件
                    if (isTouchOnScrollView() || isTouchOnAbsListView()) {
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isSilding = false;
                if (mParentView.getScrollX() <= -viewWidth / 3) {
                    isFinish = true;
                    scrollRight();
                } else {
                    scrollOrigin();
                    isFinish = false;
                }
                break;

        }
        // 假如touch的view是AbsListView或者ScrollView 我们处理完上面自己的逻辑之后
        // 再交给AbsListView, ScrollView自己处理其自己的逻辑
        if (isTouchOnScrollView() || isTouchOnAbsListView()) {
            return v.onTouchEvent(event);
        }

        return true;
    }
    @Override
    public void computeScroll() {
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (mScroller.computeScrollOffset()) {
            mParentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();

            if (mScroller.isFinished()) {

                if (onSildingFinishListener != null && isFinish) {
                    onSildingFinishListener.onSildingFinish();
                }
            }
        }
    }

}
