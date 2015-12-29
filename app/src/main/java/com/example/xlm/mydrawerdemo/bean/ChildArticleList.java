package com.example.xlm.mydrawerdemo.bean;

import java.util.List;

/**
 * Created by 鹏祺 on 2015/12/28.
 */
public class ChildArticleList {
    private String id;
    private String img;
    private String ext;
    private String now;
    private String userid;
    private String name;
    private String email;
    private String title;
    private String content;
    private String admin;
    private int replyCount;
    private List<reply> replys;

    public ChildArticleList(String id, String img, String ext, String now, String userid,
                            String name, String email, String title, String content, String admin, int replyCount, List<reply> replys) {
        this.id = id;
        this.img = img;
        this.ext = ext;
        this.now = now;
        this.userid = userid;
        this.name = name;
        this.email = email;
        this.title = title;
        this.content = content;
        this.admin = admin;
        this.replyCount = replyCount;
        this.replys = replys;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public List<reply> getReplys() {
        return replys;
    }

    public void setReplys(List<reply> replys) {
        this.replys = replys;
    }
}
