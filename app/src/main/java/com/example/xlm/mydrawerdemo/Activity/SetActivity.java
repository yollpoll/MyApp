package com.example.xlm.mydrawerdemo.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.API.GetCookieService;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.base.BaseSwipeActivity;
import com.example.xlm.mydrawerdemo.fragment.FIleListFragment;
import com.example.xlm.mydrawerdemo.http.AddCookieInterceptor;
import com.example.xlm.mydrawerdemo.http.GetCookieInterceptor;
import com.example.xlm.mydrawerdemo.http.Httptools;
import com.example.xlm.mydrawerdemo.utils.Constant;
import com.example.xlm.mydrawerdemo.utils.FileUtils;
import com.example.xlm.mydrawerdemo.utils.SPUtiles;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;
import com.example.xlm.mydrawerdemo.utils.Tools;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by 鹏祺 on 2017/5/24.
 */

public class SetActivity extends BaseActivity {
    private Toolbar mToolbar;
    private RelativeLayout rlGetCookie, rlSaveCookie, rlLoadCookie;
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
        setCookie();
        initToolbar("设置");
        retrofit = Httptools.getInstance().getRetrofit();
    }

    private void setCookie() {
        cookie = "";
        String cookies = SPUtiles.getCookie();
        String[] cookieList = cookies.split(";");
        for (int i = 0; i < cookieList.length; i++) {
            if (cookieList[i].contains("Max-Age")) {
                cookie += cookieList[i].substring(9, cookieList[i].length() - 1);
            }
        }
        if (TextUtils.isEmpty(cookie)) {
            cookie = " 暂时没有饼干";
        } else {
            cookie = "饼干将在 " + Tools.changeTime(cookie) + " 后失效";
        }
        tvCurrentCookie.setText(cookie);
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        rlGetCookie = (RelativeLayout) findViewById(R.id.rl_get_cookie);
        tvCurrentCookie = (TextView) findViewById(R.id.tv_current_cookie);
        llRoot = (LinearLayout) findViewById(R.id.ll_root);
        rlSaveCookie = (RelativeLayout) findViewById(R.id.rl_save_cookie);
        rlLoadCookie = (RelativeLayout) findViewById(R.id.rl_load_cookie);

        rlGetCookie.setOnClickListener(this);
        rlSaveCookie.setOnClickListener(this);
        rlLoadCookie.setOnClickListener(this);
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
                setCookie();
                ToastUtils.SnakeShowShort(llRoot, response.body());
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void saveCookie() {
        String cookie = SPUtiles.getCookie();
        cookie = cookie.replace(" ", "");
        if (FileUtils.writeToFile(Constant.COOKIE, Tools.getDate(System.currentTimeMillis()) + ".txt", cookie)) {
            ToastUtils.SnakeShowShort(llRoot, "饼干保存到了" + FileUtils.cacheFile.getAbsolutePath());
        } else {
            ToastUtils.SnakeShowShort(llRoot, "写入失败");
        }
    }

    private void loadCookie() {
        //打开指定目录，显示项目说明书列表，供用户选择
        String path = Environment.getExternalStorageDirectory() + Constant.SD_CACHE_DIR + "/" + Constant.COOKIE;
        File specItemDir = new File(path);
        if (!specItemDir.exists()) {
            specItemDir.mkdir();
        }
        if (!specItemDir.exists()) {
            ToastUtils.SnakeShowShort(llRoot, "暂时没有cookie");
        } else {
            //取出文件列表：
            final File[] files = specItemDir.listFiles();
            FIleListFragment fIleListFragment = new FIleListFragment(files, onFileClickListener);
            fIleListFragment.show(getSupportFragmentManager(), "");
        }
    }

    private FIleListFragment.OnFileClickListener onFileClickListener = new FIleListFragment.OnFileClickListener() {
        @Override
        public void onClick(File file) {
            String cookie = FileUtils.readFromFile(file);
            SPUtiles.saveCookie(cookie);
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_get_cookie:
                getCookie();
                break;
            case R.id.rl_save_cookie:
                saveCookie();
                break;
            case R.id.rl_load_cookie:
                loadCookie();
                break;
        }
    }
}
