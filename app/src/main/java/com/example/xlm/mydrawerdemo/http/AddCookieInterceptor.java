package com.example.xlm.mydrawerdemo.http;

import android.text.TextUtils;

import com.example.xlm.mydrawerdemo.utils.SPUtiles;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by 鹏祺 on 2017/6/1.
 */

public class AddCookieInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String cookie = SPUtiles.getCookie();
        if (!TextUtils.isEmpty(cookie))
            builder.addHeader("Cookie", cookie);
        return chain.proceed(builder.build());
    }
}
