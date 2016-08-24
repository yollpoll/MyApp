package com.example.xlm.mydrawerdemo.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
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
    private static Bitmap mBitmap;

    public static void gotoImageActivity(Context context,Bitmap bitmap){
        mBitmap=bitmap;
        Intent intent=new Intent(context,ImageActivity.class);
        context.startActivity(intent);
    }
    public static void gotoImageActivity(Context context,String url){
        Intent intent=new Intent(context,ImageActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        initView();
        initData();
    }
    private void initData(){
        url=getIntent().getStringExtra("url");
        if(!TextUtils.isEmpty(url)){
            Glide.with(this)
                    .load(url)
                    .into(imgView);
        }else {
            imgView.setImageBitmap(mBitmap);
        }
    }
    private void initView(){
        imgView= (ImageView) findViewById(R.id.img);
    }
}
