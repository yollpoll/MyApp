package com.example.xlm.mydrawerdemo.bean;

/**
 * Created by xlm on 2015/11/26.
 */
public class ChangeTitleEvent {
    private String title;

    public ChangeTitleEvent(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
