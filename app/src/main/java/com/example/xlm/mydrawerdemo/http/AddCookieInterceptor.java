package com.example.xlm.mydrawerdemo.http;

import android.util.Log;

import com.example.xlm.mydrawerdemo.utils.SPUtiles;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 鹏祺 on 2017/6/1.
 */

public class AddCookieInterceptor implements Interceptor {
    final Map<String, String> cookieMap = new HashMap<>();

    @Override
    public Response intercept(Chain chain) throws IOException {
//        String cookie = SPUtiles.getCookie();
//        Log.d("spq", "cookie>>  " + cookie);
//        Request request = chain.request();
//        Response response;
//        if (!cookie.equals("")) {
//            Request compressedRequest = request.newBuilder()
//                    .header("Content-type", "application/x-www-form-urlencoded; charset=UTF-8")//请求服务器端上传类型
//                    .header("cookie", cookie.substring(0, cookie.length() - 1))
//                    .build();
//            response = chain.proceed(compressedRequest);
//        } else {
//            response = chain.proceed(request);
//        }
//        return response;
        Request.Builder builder = chain.request().newBuilder();
        String cookie = SPUtiles.getCookie();
        Log.d("spq", "cookie>>  " + cookie);
//        HashMap<String, String> cookieMap = new HashMap<>();
//        cookieMap.put("userhash", cookie);
        builder.addHeader("Cookie", "userhash="+cookie);
        return chain.proceed(builder.build());
    }
}
