package com.example.xlm.mydrawerdemo.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.xlm.mydrawerdemo.bean.Article;

import de.greenrobot.event.EventBus;

/**
 * Created by xlm on 2015/11/13.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus=EventBus.getDefault();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }
    public void onEvent(Article article){

    }

    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
    }
}
