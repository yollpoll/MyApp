package com.example.xlm.mydrawerdemo.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.base.BaseSwipeActivity;
import com.example.xlm.mydrawerdemo.utils.Tools;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by 鹏祺 on 2016/1/16.
 */
public class ImageActivity extends BaseSwipeActivity {
    private String url;
    private ImageView imgView;
    private static Bitmap mBitmap;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_share:
                Tools.shareMsg(Tools.getApplicationName(ImageActivity.this),mToolbar.getTitle().toString()
                        ,"","",ImageActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        initView();
        initData();
    }
    private void initData(){
        initTitle();
        loadImage();
    }
    private void initTitle(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("大图赏析(　^ω^)");
//        mToolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.icon_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageActivity.this.finish();
            }
        });
    }
    private void loadImage(){
        url=getIntent().getStringExtra("url");
        if(!TextUtils.isEmpty(url)){
            Glide.with(this)
                    .load(url)
                    .crossFade()
                    .error(R.mipmap.icon_yygq)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imgView);
        }else {
            imgView.setImageBitmap(mBitmap);
        }
    }
    private void initView(){
        imgView= (ImageView) findViewById(R.id.img);
        mProgressBar= (ProgressBar) findViewById(R.id.progressBar);
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
    }
}
