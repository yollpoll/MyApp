package com.example.xlm.mydrawerdemo.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.bean.ChildArticle;
import com.example.xlm.mydrawerdemo.view.SildingFinishLayout;

/**
 * Created by xlm on 2016/1/12.
 */
public class ChildArticleActivity extends Activity {
    private SildingFinishLayout layoutChildArticle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childarticle);
        initView();
        initData();
    }
    private void initView(){
        layoutChildArticle= (SildingFinishLayout) findViewById(R.id.layout_childarticle);
        layoutChildArticle.setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {
            @Override
            public void onSildingFinish() {
                ChildArticleActivity.this.finish();
            }
        });
        layoutChildArticle.setTouchView(layoutChildArticle);
    }
    private void initData(){

    }
}
