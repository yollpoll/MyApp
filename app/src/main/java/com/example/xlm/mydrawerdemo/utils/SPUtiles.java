package com.example.xlm.mydrawerdemo.utils;

import com.example.xlm.mydrawerdemo.base.MyApplication;

import java.util.List;

/**
 * Created by 鹏祺 on 2017/5/24.
 */

public class SPUtiles {
    public static final String COOKIE = "cookie";
    public static final String UUID = "uuid";
    public static final String CONNECTION = "connection";

    public static String getCookie() {
        return SharePreferencesUtils.getString(MyApplication.getInstance(), COOKIE, "");
    }

    public static void saveCookie(String cookie) {
        SharePreferencesUtils.putString(MyApplication.getInstance(), COOKIE, cookie);
    }

    public static String getUuid() {
        return SharePreferencesUtils.getString(MyApplication.getInstance(), UUID, "");
    }

    public static void saveUuid(String cookie) {
        SharePreferencesUtils.putString(MyApplication.getInstance(), UUID, cookie);
    }

    public static void saveConnection(String ids,String uuid) {
        SharePreferencesUtils.putString(MyApplication.getInstance(),CONNECTION+uuid,ids);
    }

    public static String getConnection(String uuid) {
        return SharePreferencesUtils.getString(MyApplication.getInstance(),CONNECTION+uuid,"");
    }
}
