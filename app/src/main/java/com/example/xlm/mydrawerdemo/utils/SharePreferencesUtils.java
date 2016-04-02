package com.example.xlm.mydrawerdemo.utils;

import android.app.SharedElementCallback;
import android.content.Context;
import android.content.SharedPreferences;

import retrofit.http.PUT;

/**
 * Created by 鹏祺 on 2016/4/1.
 */
public class SharePreferencesUtils {
    public static final String SHARED_PREFERENCE_NAME = "common";

    public static void putString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 没有默认值
     */
    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }


    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    public static int getInt(Context context, String key, int defultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defultValue);
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultValue);
    }

    public static void putFloat(Context context, String key, Float valus) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, valus);
        editor.commit();

    }

    public static Float getFloat(Context context, String key, Float defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public static Float getFloat(Context context, String key) {
        return getFloat(context, key);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreference = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

}
