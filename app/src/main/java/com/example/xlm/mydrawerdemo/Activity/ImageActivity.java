package com.example.xlm.mydrawerdemo.Activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.base.BaseSwipeActivity;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by 鹏祺 on 2016/1/16.
 */
public class ImageActivity extends BaseSwipeActivity {
    private String url;
    private ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        initView();
        initData();
    }
    private void initData(){
        url=getIntent().getStringExtra("url");
        Glide.with(this)
                .load(url)
                .into(imgView);
    }
    private void initView(){
        imgView= (ImageView) findViewById(R.id.img);
    }
}
