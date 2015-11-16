package com.example.xlm.mydrawerdemo.bean;

import java.util.List;

/**
 * Created by spq on 2015/11/16.
 */
public class Article {
    private String id;
    private String content;
    private String time;
    private List<String> commentContent;

    public Article(String id, String content, String time, List<String> commentContent) {
        this.id = id;
        this.content = content;
        this.time = time;
        this.commentContent = commentContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(List<String> commentContent) {
        this.commentContent = commentContent;
    }
}
