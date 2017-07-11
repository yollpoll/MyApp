package com.example.xlm.mydrawerdemo.bean;

import java.util.List;

/**
 * Created by spq on 2015/11/16.
 */
public class Article {
    private String id;
    private String img;//图片相对地址
    private String ext;//图片后缀
    private String now;//该串可视化发言时间
    private String userid;//该串饼干
    private String name;
    private String email;
    private String title;
    private String content;
    private String admin;//是否红名
    private String replyCount;
    private String fid;
    private List<Reply> replys;

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

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
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

    public String getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    public List<Reply> getReplies() {
        return replys;
    }

    public void setReplies(List<Reply> replies) {
        this.replys = replies;
    }

    public Article(String id, String img, String ext, String now, String userid,
                   String name, String email, String title, String content, String admin, String replyCount,
                   List<Reply> replys) {
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

    public Article() {
    }
}
