package com.example.xlm.mydrawerdemo.bean;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.xlm.mydrawerdemo.utils.RxTools;
import com.example.xlm.mydrawerdemo.utils.SPUtiles;
import com.example.xlm.mydrawerdemo.utils.Tools;

import java.util.List;

/**
 * Created by 鹏祺 on 2017/7/14.
 */

public class Draft {
    private String date;
    private String content;
    private Bitmap picture;
    private boolean isShowCheck;
    private boolean isCheck;

    public Draft(String date, String content, Bitmap picture) {
        this.date = date;
        this.content = content;
        this.picture = picture;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isShowCheck() {
        return isShowCheck;
    }

    public void setShowCheck(boolean showCheck) {
        isShowCheck = showCheck;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }


    /**
     * 变换地址成bitmap
     *
     * @param draftWithPath
     */

    public static void changePathToBitmap(final DraftWithPath draftWithPath, RxTools.BitmapCallback callback) {
        RxTools.DecodeStream(draftWithPath.getPicture(), callback);
    }

    /**
     * 变换bitmap成地址
     *
     * @param draft
     * @param callback
     */
    public static void changeBitmapToPath(Draft draft, Tools.OnSaveImageCallback callback) {
        Tools.saveImageToSdViaAsyncTask(draft.getPicture(), "draft_" + System.currentTimeMillis() + ".png", callback);
    }

    /**
     * 保存当前草稿
     *
     * @param draft
     */
    public static void saveDraft(final Draft draft) {
        //先转换
        changeBitmapToPath(draft, new Tools.OnSaveImageCallback() {
            @Override
            public void callback(String path) {
                DraftWithPath draftWithPath = new DraftWithPath(draft.getDate(), draft.getContent(), path);
                List<DraftWithPath> draftWithPaths = SPUtiles.getDrafts();
                draftWithPaths.add(draftWithPath);
                SPUtiles.saveDrafts(draftWithPaths);
            }
        });
    }

    /**
     * 转化成path以后保存整个队列到sp中
     *
     * @param drafts
     * @param context
     */
    public static void saveDrafts(List<Draft> drafts, Context context, RxTools.DraftWithPathCallback callback) {
        RxTools.ChangeBitmapToPath(context, drafts, callback);
    }

    /**
     * 从SP中获取DraftWithPath转化成Draft
     *
     * @param callback
     * @return
     */
    public static void loadDrafts(RxTools.DraftCallback callback) {
        List<DraftWithPath> draftWithPaths = SPUtiles.getDrafts();
        RxTools.DecodeStreamList(draftWithPaths, callback);
    }
}
