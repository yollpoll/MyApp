package com.example.xlm.mydrawerdemo.utils;

import android.text.TextUtils;

import com.example.xlm.mydrawerdemo.base.MyApplication;
import com.example.xlm.mydrawerdemo.bean.Announcement;
import com.example.xlm.mydrawerdemo.bean.ChildForm;
import com.example.xlm.mydrawerdemo.bean.DraftWithPath;
import com.example.xlm.mydrawerdemo.bean.Form;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 鹏祺 on 2017/5/24.
 */

public class SPUtiles {
    public static final String COOKIE = "cookie";
    public static final String UUID = "uuid";
    public static final String CONNECTION = "connection";
    public static final String TAGS = "tags";//板块
    public static final String ANNOUNCEMENT = "announcement";
    public static final String FORMS = "forms";
    public static final String DRAFT = "draft";
    public static final String HEAD_URL = "head_url";

    public static void saveHeadUrl(String url) {
        SharePreferencesUtils.putString(MyApplication.getInstance().getApplicationContext(), HEAD_URL, url);
    }

    public static String getHeadUrl() {
        return SharePreferencesUtils.getString(MyApplication.getInstance().getApplicationContext(), HEAD_URL, "");
    }

    public static void saveDrafts(List<DraftWithPath> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        SharePreferencesUtils.putString(MyApplication.getInstance().getApplicationContext(), DRAFT, json);
    }

    public static List<DraftWithPath> getDrafts() {
        List<DraftWithPath> result = new ArrayList<>();
        Gson gson = new Gson();
        String data = SharePreferencesUtils.getString(MyApplication.getInstance(), DRAFT, "");
        if (!TextUtils.isEmpty(data)) {
            result = gson.fromJson(data, new TypeToken<List<DraftWithPath>>() {
            }.getType());
        }
        return result;
    }

    public static List<ChildForm> getTags() {
        List<ChildForm> result = new ArrayList<>();
        String data = SharePreferencesUtils.getString(MyApplication.getInstance(), TAGS, "");
        if (!TextUtils.isEmpty(data)) {
            Gson gson = new Gson();
            result = gson.fromJson(data, new TypeToken<List<ChildForm>>() {
            }.getType());
        }
        return result;
    }

    public static List<Form> getForms() {
        String json = SharePreferencesUtils.getString(MyApplication.getInstance(), FORMS);
        List<Form> result = new ArrayList<>();
        if (!TextUtils.isEmpty(json)) {
            Gson gson = new Gson();
            result = gson.fromJson(json, new TypeToken<List<Form>>() {
            }.getType());
        }
        return result;
    }

    public static void saveForms(List<Form> form) {
        Gson gson = new Gson();
        String json = gson.toJson(form);
        SharePreferencesUtils.putString(MyApplication.getInstance(), FORMS, json);
    }

    public static void saveAnnouncement(Announcement announcement) {
        Gson gson = new Gson();
        String json = gson.toJson(announcement);
        SharePreferencesUtils.putString(MyApplication.getInstance(), ANNOUNCEMENT, json);
    }

    public static Announcement loadAnnouncement() {
        String json = SharePreferencesUtils.getString(MyApplication.getInstance(), ANNOUNCEMENT, "");
        Gson gson = new Gson();
        return gson.fromJson(json, Announcement.class);
    }

    public static void saveTags(List<ChildForm> list) {
        Gson gson = new Gson();
        String result = gson.toJson(list);
        SharePreferencesUtils.putString(MyApplication.getInstance(), TAGS, result);
    }

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

    public static void saveConnection(String ids, String uuid) {
        SharePreferencesUtils.putString(MyApplication.getInstance(), CONNECTION + uuid, ids);
    }

    public static String getConnection(String uuid) {
        return SharePreferencesUtils.getString(MyApplication.getInstance(), CONNECTION + uuid, "");
    }
}
