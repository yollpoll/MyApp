package com.example.xlm.mydrawerdemo.base;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.example.xlm.mydrawerdemo.Dao.DaoMaster;
import com.example.xlm.mydrawerdemo.Dao.DaoSession;
import com.example.xlm.mydrawerdemo.http.Port;
import com.example.xlm.mydrawerdemo.utils.SPUtiles;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by xlm on 2016/1/15.
 */
public class MyApplication extends Application {
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private SQLiteDatabase db;
    private static MyApplication instance;


    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setupDatabase();
        createUuid();
        getRealHeadUrl();
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    private void getRealHeadUrl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                String realUrl = "";
                try {
                    url = new URL(Port.MAIN_HEAD_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.getResponseCode();
                    realUrl = connection.getURL().toString();
                    connection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(realUrl)) {
                    SPUtiles.saveHeadUrl(realUrl);
//                    Port.HEAD_URL = realUrl;
                    Log.d("spq", "head_url>>>>>>>>>>>>>" + realUrl);
                }
            }
        }).start();
    }

    private void setupDatabase() {
        //通过DaoMaster的内部类DevOpenHelper你可以得到一个便利的SQLiteOpenHelper对象
        //可能你已经注意到了，你并不需要去编写[CREATE TABLE]这样的sql语句，因为greenDao已经帮你做了
        //注意：默认的DaoMaster.DevOpenHeler会在数据库升级时，删除所有的表，意味着这将导致数据的丢失
        //所以，在正式的项目中，你还应该做一层封装，来实现数据的安全升级
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "forum-db", null);
        db = helper.getWritableDatabase();
        //注意：该数据库连接属于DaoMaster所以多个session指的是相同的数据库连接
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public String getUuId() {
        return SPUtiles.getUuid();
    }

    public void createUuid() {
        if (TextUtils.isEmpty(getUuId()))
            SPUtiles.saveUuid(UUID.randomUUID().toString());
    }

    public String getCookie() {
        return SPUtiles.getCookie();
    }

    public void setCookie(String cookie) {
        SPUtiles.saveCookie(cookie);
    }

}
