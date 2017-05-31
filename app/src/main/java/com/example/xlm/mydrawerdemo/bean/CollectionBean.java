package com.example.xlm.mydrawerdemo.bean;

import com.example.xlm.mydrawerdemo.base.MyApplication;
import com.example.xlm.mydrawerdemo.utils.SPUtiles;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 鹏祺 on 2017/5/25.
 */

public class CollectionBean {
        private String id;
        private String fid;
        private String category;
        private String img;
        private String ext;
        private String now;
        private String userid;
        private String name;
        private String email;
        private String title;
        private String content;
        private String status;
        private String admin;
        private boolean isCheck;
        private boolean isShowCheck=false;

    public boolean isShowCheck() {
        return isShowCheck;
    }

    public void setShowCheck(boolean showCheck) {
        isShowCheck = showCheck;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public static void saveIds(List<String> list){
        String ids="";
        for (String bean:list){
            ids+=","+bean;
        }
        SPUtiles.saveConnection(ids, MyApplication.getInstance().getUuId());
    }

    public static List<String> getIds(String uuid){
        List<String> ids=new ArrayList<>();
        String idsStr=SPUtiles.getConnection(uuid);
        String[] list=idsStr.split(",");
        for (int i=0;i<list.length;i++){
            ids.add(list[i]);
        }
        return ids;
    }

}
