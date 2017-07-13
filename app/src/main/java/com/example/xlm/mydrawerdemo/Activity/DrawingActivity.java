package com.example.xlm.mydrawerdemo.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;
import com.example.xlm.mydrawerdemo.utils.Tools;
import com.example.xlm.mydrawerdemo.view.ChangeBurshWidthView;
import com.example.xlm.mydrawerdemo.view.DrawView;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by 鹏祺 on 2017/6/19.
 * 涂鸦板
 */

public class DrawingActivity extends BaseActivity implements View.OnLongClickListener {
    public static final int REQUEST_DRAWING = 1234;
    private DrawView mDrawView;
    private FloatingActionButton flbMenu;
    private Toolbar mToolbar;
    private FloatingActionMenu actionMenu;
    private CoordinatorLayout clRoot;
    private ImageView imgCache;
    private ImageView imgCleaner;
    private RelativeLayout rlRoot;

    public static void gotoDrawingActivity(Activity activity) {
        Intent intent = new Intent(activity, DrawingActivity.class);
        activity.startActivityForResult(intent, REQUEST_DRAWING);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_submit:
                new AlertDialog.Builder(this).setMessage(R.string.sure_submit)
                        .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                submit();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                return true;
            case R.id.menu_save:
                save();
                return true;
            case R.id.menu_set_bg:
                setBackground();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Tools.PIC_FROM_PHOTO:
                if (resultCode != RESULT_OK) {
                    ToastUtils.SnakeShowShort(rlRoot, "获取图片失败");
                    break;
                }
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    mDrawView.setBackGround(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case Tools.PIC_FROM_CAMERA:
                if (resultCode != RESULT_OK) {
                    ToastUtils.SnakeShowShort(rlRoot, "拍照失败");
                    break;
                }
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(Tools.imgUri, "image/*");
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Tools.imgUri);
                DrawingActivity.this.startActivityForResult(intent, Tools.CROP_PHOTO);
                break;
            case Tools.CROP_PHOTO:
                if (resultCode != RESULT_OK) {
                    ToastUtils.SnakeShowShort(rlRoot, "剪裁失败");
                    break;
                }
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Tools.imgUri));
                    mDrawView.setBackGround(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        initView();
        initData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            myFinsh();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void myFinsh() {
        new AlertDialog.Builder(DrawingActivity.this).setMessage(R.string.sure_exit)
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DrawingActivity.this.finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void initView() {
        mDrawView = (DrawView) findViewById(R.id.draw_view);
        flbMenu = (FloatingActionButton) findViewById(R.id.fb_menu);
        imgCache = (ImageView) findViewById(R.id.img_cache);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        clRoot = (CoordinatorLayout) findViewById(R.id.cl_root);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);

        flbMenu.setOnLongClickListener(this);
        flbMenu.setOnClickListener(this);
        mDrawView.setCleanModeListener(onCleanModeChangerListener);
    }

    private void initData() {
        initFloatingActionButton();
        initTitle();
    }

    private void initTitle() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("涂鸦");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFinsh();
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fb_menu:
                break;
        }
    }

    int red = 0, blue = 0, green = 0;
    RelativeLayout rlColor;

    private void changeColor() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_choose_color);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        dialog.show();
        SeekBar sbRed = (SeekBar) dialog.findViewById(R.id.seek_red);
        SeekBar sbGreen = (SeekBar) dialog.findViewById(R.id.seek_green);
        SeekBar sbBlue = (SeekBar) dialog.findViewById(R.id.seek_blue);
        TextView tvBrush = (TextView) dialog.findViewById(R.id.tv_brush);
        TextView tvBg = (TextView) dialog.findViewById(R.id.tv_bg);
        rlColor = (RelativeLayout) dialog.findViewById(R.id.rl_color);
        rlColor.setBackgroundColor(Color.rgb(red, green, blue));
        sbRed.setProgress(red * 100 / 255);
        sbGreen.setProgress(green * 100 / 255);
        sbBlue.setProgress(blue * 100 / 255);

        tvBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setPaintColor(Color.rgb(red, green, blue));
                dialog.dismiss();
            }
        });

        tvBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawView.setBackGround(Color.rgb(red, green, blue));
                dialog.dismiss();
            }
        });

        sbRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red = progress * 255 / 100;
                rlColor.setBackgroundColor(Color.rgb(red, green, blue));
//                imgCache.setImageBitmap(mDrawView.getBitmapCache());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                green = progress * 255 / 100;
                rlColor.setBackgroundColor(Color.rgb(red, green, blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blue = progress * 255 / 100;
                rlColor.setBackgroundColor(Color.rgb(red, green, blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void submit() {
        Intent intent = getIntent();
        intent.putExtra("path", Tools.saveImage(this, mDrawView.getBitmapCache(), "draw_" + System.currentTimeMillis() + ".jpg"));
        setResult(RESULT_OK, intent);
        this.finish();
    }

    /**
     * 用rxJava实行异步
     */
    private void save() {
        Observable.create(new Observable.OnSubscribe<Map<String, String>>() {
            @Override
            public void call(Subscriber<? super Map<String, String>> subscriber) {
                String path;
                String fileName = "draw_" + System.currentTimeMillis() + ".jpg";
                path = Tools.saveImageToSd(mDrawView.getBitmapCache(), fileName);
                Map<String, String> params = new HashMap<String, String>();
                params.put("path", path);
                params.put("fileName", fileName);
                subscriber.onNext(params);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Map<String, String>>() {
                    @Override
                    public void call(Map<String, String> s) {
                        Tools.updatePhoto(DrawingActivity.this, s.get("path"), s.get("fileName"));
                        ToastUtils.SnakeShowShort(rlRoot, "保存成功");
                    }
                });
    }

    private void setBackground() {
        Tools.showChoosePicDialog(this);
    }

    private void changeWidth() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_brush_width);
        Window window = dialog.getWindow();
        final WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        dialog.show();
        final ChangeBurshWidthView viewLine = (ChangeBurshWidthView) dialog.findViewById(R.id.view_show_line);
        final SeekBar seekWidth = (SeekBar) dialog.findViewById(R.id.seek_width);
        seekWidth.setProgress(mDrawView.getPaintWidth());
        viewLine.setWidth(mDrawView.getPaintWidth());
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mDrawView.setPaintWidth(seekWidth.getProgress());
            }
        });
        seekWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                viewLine.setWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void clean() {
        if (mDrawView.isCleanMode()) {
            mDrawView.cancelCleanMode();
        } else {
            mDrawView.setCleanMode();
        }
    }

    private DrawView.OnCleanModeChangerListener onCleanModeChangerListener = new DrawView.OnCleanModeChangerListener() {
        @Override
        public void onChange(boolean isCleanMode) {
            if (isCleanMode) {
                imgCleaner.setImageResource(R.mipmap.icon_cleaner_fill);
            } else {
                imgCleaner.setImageResource(R.mipmap.icon_cleaner);
            }
        }
    };

    private void clear() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.sure_clear)
                .setPositiveButton("清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDrawView.clear();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void initFloatingActionButton() {
        ImageView imgColor = new ImageView(this);
        imgColor.setImageResource(R.mipmap.icon_draw_color);
        imgColor.setPadding(20, 20, 20, 20);
        imgColor.setId(R.id.img_color);

        ImageView imgWidth = new ImageView(this);
        imgWidth.setImageResource(R.mipmap.icon_draw_width);
        imgWidth.setPadding(20, 20, 20, 20);
        imgWidth.setId(R.id.img_width);

        imgCleaner = new ImageView(this);
        imgCleaner.setImageResource(R.mipmap.icon_cleaner);
        imgCleaner.setPadding(20, 20, 20, 20);
        imgCleaner.setId(R.id.img_cleaner);

        ImageView imgClear = new ImageView(this);
        imgClear.setImageResource(R.mipmap.icon_clear);
        imgClear.setPadding(20, 20, 20, 20);
        imgClear.setId(R.id.img_clear);


        SubActionButton subColor = new SubActionButton.Builder(this).setContentView(imgColor).build();
        SubActionButton subWidth = new SubActionButton.Builder(this).setContentView(imgWidth).build();
        SubActionButton subSave = new SubActionButton.Builder(this).setContentView(imgCleaner).build();
        SubActionButton subClear = new SubActionButton.Builder(this).setContentView(imgClear).build();

        subColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor();
            }
        });
        subWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeWidth();
            }
        });
        subSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clean();
            }
        });
        subClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
        int size = Tools.calculateDpToPx(50, DrawingActivity.this);
        actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(subColor, size, size)
                .addSubActionView(subWidth, size, size)
                .addSubActionView(subSave, size, size)
                .addSubActionView(subClear, size, size)
                // ...
                .attachTo(flbMenu)
                .build();
    }

    private void dismissToolbar() {
        int size = (int) (0 - getResources().getDimension(R.dimen.toolbar_size));
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, size);
        animation.setDuration(400);
        animation.setFillAfter(true);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        mToolbar.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mToolbar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void showToolbar() {
        int size = (int) (0 - getResources().getDimension(R.dimen.toolbar_size));
        TranslateAnimation animation = new TranslateAnimation(0, 0, size, 0);
        animation.setDuration(400);
        animation.setFillAfter(true);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        mToolbar.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mToolbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.fb_menu:
                if (mToolbar.getVisibility() == View.VISIBLE) {
                    dismissToolbar();
                } else {
                    showToolbar();
                }
                break;
        }
        return true;
    }
}
