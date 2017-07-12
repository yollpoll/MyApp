package com.example.xlm.mydrawerdemo.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public DownLoadImageThread(String url, Context context, ImageDownCallback callback,
                               String imageName) {
        this.url = url;
        this.imageName = imageName;
        this.context = context;
        this.callback = callback;
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
                fileName = Tools.saveImageToSd(bitmap, imageName);
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
