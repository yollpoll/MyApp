package com.example.xlm.mydrawerdemo.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.bean.Article;
import com.example.xlm.mydrawerdemo.utils.Tools;

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
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != mToolbar)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mToolbar.setElevation(Tools.calculateDpToPx(2, this));
            }
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

    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
    }
}
