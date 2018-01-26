package com.example.xlm.mydrawerdemo.Activity;

import android.os.Bundle;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        getBackUpUrl();
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
                MainActivity.gotoMainActivity(IndexActivity.this);
            }

            @Override
            public void onFailure(Throwable throwable) {
                MainActivity.gotoMainActivity(IndexActivity.this);
            }
        });
    }
}
