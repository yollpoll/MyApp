package com.example.xlm.mydrawerdemo.http;

import android.text.TextUtils;

import com.example.xlm.mydrawerdemo.utils.SPUtiles;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieHandler;
import java.util.HashSet;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by 鹏祺 on 2017/6/1.
 */

public class GetCookieInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            final StringBuffer cookieBuffer = new StringBuffer();
            Observable.from(originalResponse.headers("Set-Cookie"))
                    .map(new Func1<String, String>() {
                        @Override
                        public String call(String s) {
                            String[] cookieArray = s.split(";");
                            return s+";";
                        }
                    })
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String cookie) {
                            cookieBuffer.append(cookie);
                        }
                    });
            SPUtiles.saveCookie(cookieBuffer.toString());
        }
        return originalResponse;
    }
}
