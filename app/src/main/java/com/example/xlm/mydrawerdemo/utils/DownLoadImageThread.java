package com.example.xlm.mydrawerdemo.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;

/**
 * Created by 鹏祺 on 2017/5/18.
 */

public class DownLoadImageThread implements Runnable{
    private String url;
    private Context context;
    private ImageDownCallback callback;
    private File downFile;

    public DownLoadImageThread(String url, Context context, ImageDownCallback callback, File downFile) {
        this.url = url;
        this.context = context;
        this.callback = callback;
        this.downFile = downFile;
    }

    @Override
    public void run() {
        File file=null;
        Bitmap bitmap=null;
        try {
            bitmap= Glide.with(context)
                                    .load(url)
                                    .asBitmap()
                                    .into(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                                    .get();

        }catch (Exception e){

        }finally {

        }
    }

    /**
     * 保存图片到本地
     * @param context
     * @param bitmap
     */
    private void saveImage(Context context,Bitmap bitmap){

    }
    public interface ImageDownCallback{
        void onDownLoadSuccess(Bitmap bitmap);
        void onDownLoadSuccess(File file);
        void onDownFailed();
    }
}
