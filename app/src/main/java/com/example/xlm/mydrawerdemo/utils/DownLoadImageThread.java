package com.example.xlm.mydrawerdemo.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;

/**
 * Created by 鹏祺 on 2017/5/18.
 */

public class DownLoadImageThread implements Runnable {
    public static final int DOWN_SUCCESS_WITH_BITMIP = 1;
    public static final int DOWN_SUCCESS_WITH_FILE = 2;
    public static final int DOWN_FAILED = 3;

    private String url;
    private Context context;
    private ImageDownCallback callback;
    private File downFile;
    private String imageName;
    private String path;

    public DownLoadImageThread(String url, Context context, ImageDownCallback callback,
                               String imageName, String path) {
        this.url = url;
        this.imageName = imageName;
        this.context = context;
        this.callback = callback;
        this.path = path;
    }

    @Override
    public void run() {
        String fileName = "";
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            if (bitmap != null) {
                //保存图片，文件名跟随图片名
                fileName = Tools.saveImageToSd(bitmap, imageName, path);
                downFile = new File(fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != bitmap && downFile.exists()) {
                callback.onDownLoadSuccess(bitmap);
                callback.onDownLoadSuccess(downFile);
            } else {
                callback.onDownFailed();
            }
        }
    }

    public interface ImageDownCallback {
        void onDownLoadSuccess(Bitmap bitmap);

        void onDownLoadSuccess(File file);

        void onDownFailed();
    }
}
