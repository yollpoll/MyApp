package com.example.xlm.mydrawerdemo.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.API.GetCookieService;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseSwipeActivity;
import com.example.xlm.mydrawerdemo.http.AddCookieInterceptor;
import com.example.xlm.mydrawerdemo.http.GetCookieInterceptor;
import com.example.xlm.mydrawerdemo.http.Httptools;
import com.example.xlm.mydrawerdemo.utils.SPUtiles;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;

import java.net.CookieManager;
import java.net.CookiePolicy;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by 鹏祺 on 2017/5/24.
 */

public class SetActivity extends BaseSwipeActivity {
    private Toolbar mToolbar;
    private RelativeLayout rlGetCookie;
    private TextView tvCurrentCookie;
    private String cookie;
    private Retrofit retrofit;
    private LinearLayout llRoot;

    public static void gotoSetActivity(Context context) {
        Intent intent = new Intent(context, SetActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_set_actiivty, menu);
        return true;
    }

    private void initData() {
        initTitle();
        cookie = SPUtiles.getCookie();
        if (TextUtils.isEmpty(cookie))
            cookie = "当前饼干是: 暂时没有饼干";
        tvCurrentCookie.setText("当前饼干是:" + cookie);

        retrofit = Httptools.getInstance().getRetrofit();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        rlGetCookie = (RelativeLayout) findViewById(R.id.rl_get_cookie);
        tvCurrentCookie = (TextView) findViewById(R.id.tv_current_cookie);
        llRoot = (LinearLayout) findViewById(R.id.ll_root);

        rlGetCookie.setOnClickListener(this);
    }

    private void initTitle() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("设置");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetActivity.this.finish();
            }
        });
    }

    private void getCookie() {
//        retrofit.client().interceptors().add(new AddCookieInterceptor());
        retrofit.client().interceptors().add(new GetCookieInterceptor());
        retrofit.client().getCookieHandler().setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        GetCookieService cookieService = retrofit.create(GetCookieService.class);
        Call<String> call = cookieService.getArticleList();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if ("error".equals(response.body()) || TextUtils.isEmpty(response.body())) {
//                    ToastUtils.SnakeShowShort(llRoot, "获取失败");
                    cookie = "暂时没有饼干";
                } else {
                    cookie = SPUtiles.getCookie();
                }
                tvCurrentCookie.setText("当前饼干是：" + cookie);
                ToastUtils.SnakeShowShort(llRoot, response.body());
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_get_cookie:
                getCookie();
                break;
        }
    }
}
