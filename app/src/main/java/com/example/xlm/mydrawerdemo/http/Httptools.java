package com.example.xlm.mydrawerdemo.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by xlm on 2015/11/24.
 */
public class Httptools {
    private static Httptools instance = null;
    private static Retrofit retrofit = null;

    public Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Port.HEAD_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        retrofit.client().interceptors().add(new AddCookieInterceptor());
        return retrofit;
    }

    public static Httptools getInstance() {
        if (instance == null) {
            instance = new Httptools();
        }
        return instance;
    }


    /**
     * 根据文件创建requestbody
     *
     * @param file
     * @return
     */
    public static RequestBody getRequestBody(File file) {
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/*"), file);
        return requestFile;
    }

    /**
     * 根据文件创建requestbody
     *
     * @param content
     * @return
     */
    public static RequestBody getRequestBody(String content) {
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("text/plain"), content);
        return requestBody;
    }
}
