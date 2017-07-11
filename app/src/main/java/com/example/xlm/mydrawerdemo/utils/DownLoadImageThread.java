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
    private boolean isSave;//是否是保存还是分享图片
    private String imageName;

    public DownLoadImageThread(String url, Context context, ImageDownCallback callback,
                               boolean isSave, String imageName) {
        this.url = url;
        this.imageName = imageName;
        this.isSave = isSave;
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void run() {
        File file = null;
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            if (bitmap != null) {
                //TODO 这里需要优化，saveImage应该应该用在外部回调中，不应该在这里全部完成，应当保持这个类功能的单一性即下载图片
                if (isSave) {
                    //保存图片，文件名跟随图片名
                    saveImage(context, bitmap, imageName, isSave);
                } else {
                    //分享图片，文件名固定
                    saveImage(context, bitmap, "save.jpg", isSave);
                }
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

    /**
     * 保存图片到本地
     *
     * @param context
     * @param bitmap
     */
    private void saveImage(Context context, Bitmap bitmap, String imageName, boolean isSave) {
        //替换/
        String img = imageName.replace("/", "_");
        File cacheDir = new File(Environment.getExternalStorageDirectory() + Constant.SD_CACHE_DIR + Constant.IMG_CACHE);
        if (!cacheDir.exists())
            cacheDir.mkdir();
        File cacheImage = new File(cacheDir + "/" + img);
        downFile = cacheImage;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(cacheImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (isSave) {
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        cacheImage.getAbsolutePath(), img, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(cacheImage)));
        }
    }

    public interface ImageDownCallback {
        void onDownLoadSuccess(Bitmap bitmap);

        void onDownLoadSuccess(File file);

        void onDownFailed();
    }
}
