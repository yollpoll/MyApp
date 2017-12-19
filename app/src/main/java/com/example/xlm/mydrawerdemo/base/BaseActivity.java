package com.example.xlm.mydrawerdemo.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.bean.Article;
import com.example.xlm.mydrawerdemo.utils.SPUtiles;

import de.greenrobot.event.EventBus;

/**
 * Created by xlm on 2015/11/13.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected EventBus eventBus;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus = EventBus.getDefault();
        initHeadUrl();
    }

    protected void initToolbar(String title) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null)
            return;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(title);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFinish();
            }
        });
    }

    private void initHeadUrl() {
        String headUrl = SPUtiles.getHeadUrl();
        if (TextUtils.isEmpty(headUrl)) {
            return;
        }
//        Port.HEAD_URL = headUrl;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }

    public void onEvent(Article article) {

    }

    protected void myFinish() {
        BaseActivity.this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    protected boolean flag_key;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !flag_key) {
            myFinish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
