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
        String cookie = SPUtiles.getCookie();
        Request request = chain.request();
        Response response;
        if (!cookie.equals("")) {
            Request compressedRequest = request.newBuilder()
                    .header("Content-type", "application/x-www-form-urlencoded; charset=UTF-8")//请求服务器端上传类型
                    .header("cookie", cookie.substring(0, cookie.length() - 1))
                    .build();
            response = chain.proceed(compressedRequest);
        } else {
            response = chain.proceed(request);
        }
        return response;
    }
}
