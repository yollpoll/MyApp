package com.example.xlm.mydrawerdemo.bean;

/**
 * Created by xlm on 2015/11/19.
 */
public class ChildArticle {
    private String id;
    private String fgroup;
    private String sort;
    private String name;
    private String showName;
    private String msg;
    private String interval;
    private String createdAt;
    private String updateAt;
    private String status;

    public ChildArticle(String id, String fgroup, String sort, String name, String showName, String msg, String interval, String createdAt, String updateAt, String status) {
        this.id = id;
        this.fgroup = fgroup;
        this.sort = sort;
        this.name = name;
        this.showName = showName;
        this.msg = msg;
        this.interval = interval;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFgroup() {
        return fgroup;
    }

    public void setFgroup(String fgroup) {
        this.fgroup = fgroup;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
