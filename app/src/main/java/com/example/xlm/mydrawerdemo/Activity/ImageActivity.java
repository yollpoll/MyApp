package com.example.xlm.mydrawerdemo.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.base.BaseSwipeActivity;
import com.example.xlm.mydrawerdemo.http.Port;
import com.example.xlm.mydrawerdemo.utils.DownLoadImageThread;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;
import com.example.xlm.mydrawerdemo.utils.Tools;

import java.io.File;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by 鹏祺 on 2016/1/16.
 */
public class ImageActivity extends BaseActivity {
    private String url;
    private ImageView imgView;
    private static Bitmap mBitmap;
    //    private ProgressBar mProgressBar;
    private Toolbar mToolbar;
    private String imageName;
    private String mSharePath;
    private boolean isShareing;

    public static void gotoImageActivity(Activity activity, Bitmap bitmap, View shareView) {
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, shareView, "img");
        mBitmap = bitmap;
        Intent intent = new Intent(activity, ImageActivity.class);
        intent.putExtra("imageName", System.currentTimeMillis());
        activity.startActivity(intent, options.toBundle());
    }

    public static void gotoImageActivity(Context context, String url, String imageName) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("imageName", imageName);
        context.startActivity(intent);
    }

    public static void gotoImageActivity(Activity activity, String url, String imageName, View shareView) {
        if (url.endsWith("gif")) {
            //动态图的情况下使用一般的跳转方式
            gotoImageActivity(activity, url, imageName);
        } else {
            //金泰图使用transition
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, shareView, "img");
            Intent intent = new Intent(activity, ImageActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("imageName", imageName);
            activity.startActivity(intent, options.toBundle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                if (null != mSharePath) {
                    Tools.shareMsg(Tools.getApplicationName(ImageActivity.this), mToolbar.getTitle().toString()
                            , "", mSharePath, ImageActivity.this);
                } else {
                    //这种情况是用户进入直接分享图，图片还没有下载完成
                    isShareing = true;
                    onDown(url, false, true);
                }
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

    private void initData() {
        imageName = getIntent().getStringExtra("imageName");
        initTitle();
        loadImage();
    }

    private void initTitle() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("大图赏析");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ImageActivity.this.finishAfterTransition();
                } else {
                    ImageActivity.this.finish();
                }
            }
        });
    }

    private void loadImage() {
        url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
//            onDown(url, true, true);
//            Glide.with(this)
//                    .load(url)
//                    .asBitmap()
////                    .crossFade()
//                    .error(R.mipmap.icon_yygq)
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            imgView.setImageBitmap(resource);
//                        }
//                    });
            if (url.endsWith("gif")) {
                Glide.with(this).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imgView);
            } else {
                Glide.with(this).load(url).into(imgView);
            }
            Log.d("spq", "getUrl>>>>>");
        } else {
            imgView.setImageBitmap(mBitmap);
            Log.d("spq", "getBitmap>>>>>");
        }
    }

    private void initView() {
        imgView = (ImageView) findViewById(R.id.img);
//        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    /**
     * @param url
     * @param isShow 是否是第一次显示
     * @param isSave 是保存还是分享
     */
    private void onDown(String url, final boolean isShow, boolean isSave) {
        //下载图片
        final DownloadHandler handler = new DownloadHandler();
        DownLoadImageThread downLoadImageThread = new DownLoadImageThread(url, this, new DownLoadImageThread.ImageDownCallback() {
            @Override
            public void onDownLoadSuccess(Bitmap bitmap) {
                Message message = new Message();
                message.what = DownLoadImageThread.DOWN_SUCCESS_WITH_BITMIP;
//                Bundle bundle=new Bundle();
//                bundle.putParcelable("bitmip",bitmap);
//                message.setData(bundle);
                mBitmap = bitmap;
                handler.sendMessage(message);
            }

            @Override
            public void onDownLoadSuccess(File file) {
                Message message = new Message();
                message.what = DownLoadImageThread.DOWN_SUCCESS_WITH_FILE;
                Bundle bundle = new Bundle();
                bundle.putString("path", file.getAbsolutePath());
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onDownFailed() {
                handler.sendEmptyMessage(DownLoadImageThread.DOWN_FAILED);
            }
        }, isSave, imageName);
        new Thread(downLoadImageThread).start();
    }

    //处理下载好图片
    private class DownloadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DownLoadImageThread.DOWN_SUCCESS_WITH_BITMIP:
//                    Bitmap bitmap=msg.getData().getParcelable("bitmap");
                    if (null != mBitmap)
                        imgView.setImageBitmap(mBitmap);
                    break;
                case DownLoadImageThread.DOWN_SUCCESS_WITH_FILE:
                    String path = msg.getData().getString("path");
                    mSharePath = path;
                    if (isShareing) {
                        //这种情况是从用户分享走到这一步的
                        Tools.shareMsg(Tools.getApplicationName(ImageActivity.this), mToolbar.getTitle().toString()
                                , "", path, ImageActivity.this);
                        isShareing = false;
                    }
                    break;
                case DownLoadImageThread.DOWN_FAILED:
                    break;
            }
        }
    }
}
