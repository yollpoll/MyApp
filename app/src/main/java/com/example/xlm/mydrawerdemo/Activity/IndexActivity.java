package com.example.xlm.mydrawerdemo.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.base.MyApplication;
import com.example.xlm.mydrawerdemo.http.Httptools;
import com.example.xlm.mydrawerdemo.retrofitService.BackupUrlService;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by 鹏祺 on 2018/1/26.
 */

public class IndexActivity extends BaseActivity {
    private AppCompatImageView ivBg;
    private boolean ifFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityAnima();
        setContentView(R.layout.activity_index);
        getBackUpUrl();
        initView();
        initData();
    }

    private void initActivityAnima() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.index_activity_transition);
        //第一次进入时使用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //退出时使用
            getWindow().setExitTransition(explode);
        }
    }
    private void initView() {
        ivBg = (AppCompatImageView) findViewById(R.id.iv_bg);
    }

    private void initData() {
        //进行缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.4f, 1.0f, 1.4f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setInterpolator(new DecelerateInterpolator());
        //动画播放完成后保持形状
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //可以在这里先进行某些操作
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (ifFinish) {
                    gotoMain();
                } else {
                    ifFinish = true;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivBg.startAnimation(scaleAnimation);
    }

    private void getBackUpUrl() {
        Retrofit retrofit = Httptools.getInstance().getRetrofit();
        BackupUrlService service = retrofit.create(BackupUrlService.class);
        Call<List<String>> call = service.getBackUpUrl();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Response<List<String>> response, Retrofit retrofit) {
                String backUrl = "https://nmbimg.fastmirror.org";
                MyApplication.getInstance().setBackUpUrl(backUrl);
                if (ifFinish) {
                    gotoMain();
                } else {
                    ifFinish = true;
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                MainActivity.gotoMainActivity(IndexActivity.this);
                IndexActivity.this.finish();
            }
        });
    }

    private void gotoMain() {
        this.finish();
        MainActivity.gotoMainActivity(IndexActivity.this);
    }
}
