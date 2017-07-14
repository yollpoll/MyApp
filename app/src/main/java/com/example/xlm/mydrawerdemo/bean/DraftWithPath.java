package com.example.xlm.mydrawerdemo.bean;

/**
 * Created by 鹏祺 on 2017/7/14.
 */

public class DraftWithPath {
    private String date;
    private String content;
    private String picture;

    DraftWithPath(String date, String content, String picture) {
        this.date = date;
        this.content = content;
        this.picture = picture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
