package com.example.xlm.mydrawerdemo.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.glide.ProgressModelLoader;
import com.example.xlm.mydrawerdemo.utils.Constant;
import com.example.xlm.mydrawerdemo.utils.DialogUtils;
import com.example.xlm.mydrawerdemo.utils.DownLoadImageThread;
import com.example.xlm.mydrawerdemo.utils.PermissionUtils;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;
import com.example.xlm.mydrawerdemo.utils.Tools;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by 鹏祺 on 2016/1/16.
 */
public class ImageActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_SD = 1;
    private String url;
    private PhotoView imgView;
    private static Bitmap mBitmap;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;
    private String imageName;
    private String mSharePath;
    private boolean isShareing;
    private RelativeLayout rlRoot;

    public static void gotoImageActivity(Activity activity, Bitmap bitmap, View shareView) {
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, shareView, "img");
        mBitmap = bitmap;
        Intent intent = new Intent(activity, ImageActivity.class);
        intent.putExtra("imageName", System.currentTimeMillis());
        activity.startActivity(intent, options.toBundle());
    }

    //同时传bitmap和url
    public static void gotoImageActivity(Activity activity, String url, String imageName, Bitmap bitmap, View shareView) {
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, shareView, "img");
        mBitmap = bitmap;
        Intent intent = new Intent(activity, ImageActivity.class);
        intent.putExtra("url", url);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_SD:
                if (PermissionUtils.changeRequestResult(grantResults)) {
                    onDown(url, imageName,Constant.IMG_CACHE);
                } else {
                    ToastUtils.SnakeShowShort(rlRoot, "获取权限失败，无法 保存/分享");
                }
                break;
        }
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
//                    onDown(url);
                    PermissionUtils.checkAndRequestPermission(ImageActivity.this, PermissionUtils.WRITE_EXTERNAL_STORAGE,
                            REQUEST_WRITE_SD, onExplainPermission, onPermissionGet);
                }
                return true;
            case R.id.menu_save:
                PermissionUtils.checkAndRequestPermission(ImageActivity.this, PermissionUtils.WRITE_EXTERNAL_STORAGE,
                        REQUEST_WRITE_SD, onExplainPermission, onPermissionGet);
//                onDown(url);
                return true;
            case R.id.menu_beauty:
                PermissionUtils.checkAndRequestPermission(ImageActivity.this, PermissionUtils.WRITE_EXTERNAL_STORAGE,
                        REQUEST_WRITE_SD, onExplainPermission, onBeautyGet);
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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ImageActivity.this.finishAfterTransition();
                } else {
                    ImageActivity.this.finish();
                }
                break;
        }
        return true;
    }

    private void loadImage() {
        url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
//            if (null != mBitmap) {
//                mProgressBar.setVisibility(View.GONE);
//                imgView.setImageBitmap(mBitmap);
//            }
            if (url.endsWith("gif")) {
                Glide.with(this).using(new ProgressModelLoader(new ProgressHandler(ImageActivity.this, mProgressBar))).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imgView);
            } else {
                Glide.with(this).using(new ProgressModelLoader(new ProgressHandler(ImageActivity.this, mProgressBar))).load(url).into(imgView);
            }
        } else {
            mProgressBar.setVisibility(View.GONE);
            imgView.setImageBitmap(mBitmap);
        }
    }

    private void initView() {
        imgView = (PhotoView) findViewById(R.id.img);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    /**
     * @param url
     */
    private void onDown(String url, String imgName,String path) {
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
        }, imgName, path);
        new Thread(downLoadImageThread).start();
    }

    //保存祭品到本地,输入祭品名
    private void saveBeauty(final String url) {
        final Dialog dialog = DialogUtils.showDialog(this, R.layout.dialog_save_beauty);
        final EditText edtBeautyName = (EditText) dialog.findViewById(R.id.edt_beauty);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtBeautyName.getText().toString())) {
                    ToastUtils.showShort("请输入祭品名");
                } else {
                    confirmSaveBeauty(edtBeautyName.getText().toString(), url);
                    dialog.dismiss();
                }
            }
        });
    }

    private void confirmSaveBeauty(String beautyName, String url) {
        onDown(url, beautyName+".png",Constant.IMG_BEAUTY);
    }

    //处理下载好图片
    private class DownloadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DownLoadImageThread.DOWN_SUCCESS_WITH_BITMIP:
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
                    } else {
                        ToastUtils.showShort("保存成功在目录:"+msg.getData().getString("path"));
                        Tools.updatePhoto(ImageActivity.this, path, imageName.replace("/", "_"));
                    }
                    break;
                case DownLoadImageThread.DOWN_FAILED:
                    break;
            }
        }
    }

    private PermissionUtils.OnPermissionGet onBeautyGet = new PermissionUtils.OnPermissionGet() {
        @Override
        public void onGet() {
            saveBeauty(url);
        }
    };
    private PermissionUtils.OnPermissionGet onPermissionGet = new PermissionUtils.OnPermissionGet() {
        @Override
        public void onGet() {
            onDown(url, imageName,Constant.IMG_CACHE);
        }
    };
    private PermissionUtils.OnExplainPermission onExplainPermission = new PermissionUtils.OnExplainPermission() {
        @Override
        public void onExplain() {
            if (isShareing) {
                ToastUtils.SnakeShowShort(rlRoot, "分享/保存 图片需要SD卡读写权限");
            }
        }
    };

    private static class ProgressHandler extends Handler {

        private final WeakReference<Activity> mActivity;
        private final ProgressBar progressBar;

        public ProgressHandler(Activity activity, ProgressBar progressImageView) {
            super(Looper.getMainLooper());
            mActivity = new WeakReference<>(activity);
            progressBar = progressImageView;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Activity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        int percent = msg.arg1 * 100 / msg.arg2;
//                        progressBar.setProgress(percent);
                        if (percent == 100) {
                            progressBar.setVisibility(View.GONE);
                        } else {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
