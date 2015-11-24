package com.example.xlm.mydrawerdemo.http;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by xlm on 2015/11/24.
 */
public class Httptools {
    private static Httptools instance=null;
    private static Retrofit retrofit=null;
    public Retrofit getRetrofit(){
        if(retrofit==null){
             retrofit=new Retrofit.Builder()
                    .baseUrl(Port.HEAD_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Httptools getInstance() {
        if(instance==null){
            instance=new Httptools();
        }
        return instance;
    }
}
