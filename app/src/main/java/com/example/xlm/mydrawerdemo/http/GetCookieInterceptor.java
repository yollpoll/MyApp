package com.example.xlm.mydrawerdemo.http;

import android.text.TextUtils;

import com.example.xlm.mydrawerdemo.utils.SPUtiles;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by 鹏祺 on 2017/6/1.
 */

public class GetCookieInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (!response.header("Set-Cookie").isEmpty()) {
            String cookie = response.header("Set-Cookie");
            if (!TextUtils.isEmpty(cookie))
                SPUtiles.saveCookie(cookie);
        }
        return response;
    }
}
