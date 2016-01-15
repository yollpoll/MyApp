package com.example.xlm.mydrawerdemo.base;

import android.app.Application;

/**
 * Created by xlm on 2016/1/15.
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    public static  MyApplication getInstance(){
        if(instance==null){
            instance=new MyApplication();
        }
        return instance;
    }
}
