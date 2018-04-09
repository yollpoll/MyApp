package com.example.xlm.mydrawerdemo.http;

import com.example.xlm.mydrawerdemo.utils.CookieUtils;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
            final Map<String, String> cookieMap = new HashMap<>();
            String cookie = "";
            Observable.from(originalResponse.headers("Set-Cookie"))
                    .flatMap(new Func1<String, Observable<String>>() {
                        @Override
                        public Observable<String> call(String s) {
                            String[] cookieArray = s.split(";");
                            return Observable.from(cookieArray);
                        }
                    })
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String cookie) {
                            String[] cookieList = cookie.split("=");
                            try {
                                cookieMap.put(cookieList[0], cookieList[1]);
                            } catch (Exception e) {

                            }
                        }
                    });
            for (String key : cookieMap.keySet()) {
                cookie += key + "=" + cookieMap.get(key) + ";";
            }
            CookieUtils.saveCookie(cookie);
        }
        return originalResponse;
    }
}
