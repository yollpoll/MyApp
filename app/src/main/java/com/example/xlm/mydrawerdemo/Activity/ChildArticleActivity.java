package com.example.xlm.mydrawerdemo.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.bean.ChildArticle;
import com.example.xlm.mydrawerdemo.view.SildingFinishLayout;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by xlm on 2016/1/12.
 */
public class ChildArticleActivity extends SwipeBackActivity implements View.OnClickListener{
    private String articleId,title;
    private RelativeLayout headBtnLeft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childarticle);
        initView();
        initData();
    }
    private void initView(){
        headBtnLeft= (RelativeLayout) findViewById(R.id.head_btn_left);
        headBtnLeft.setOnClickListener(this);
    }

    private void initData(){
        articleId=getIntent().getStringExtra("id");
        title=getIntent().getStringExtra("title");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_btn_left:
                scrollToFinishActivity();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        scrollToFinishActivity();
    }
}
