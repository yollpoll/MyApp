package com.example.xlm.mydrawerdemo.bean;

import com.example.xlm.mydrawerdemo.utils.SPUtiles;
import com.google.gson.Gson;

/**
 * Created by 鹏祺 on 2017/7/5.
 */

public class Announcement {
    private String content;
    private long date;
    private boolean enable;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void save() {
        SPUtiles.saveAnnouncement(this);
    }

    public static Announcement load() {
        return SPUtiles.loadAnnouncement();
    }
}
