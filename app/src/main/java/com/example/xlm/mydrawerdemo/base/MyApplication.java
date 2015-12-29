package com.example.xlm.mydrawerdemo.base;

import android.app.Application;

/**
 * Created by 鹏祺 on 2015/12/28.
 */
public class MyApplication extends Application {
    private static MyApplication application;
    public static MyApplication getInstance(){
        if(application==null){
            application=new MyApplication();
        }
        return application;
    }
    @Override
    public void onCreate() {
        super.onCreate();

    }
}
