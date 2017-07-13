package com.example.xlm.mydrawerdemo.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.InputStream;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 存放一些异步操作
 * Created by 鹏祺 on 2017/7/13.
 */

public class RxTools {
    /**
     * 解析图库中的图片返回bitmap
     *
     * @param cr
     * @param uri
     * @param callback
     */
    public static void DecodeStream(final ContentResolver cr, final Uri uri, final BitmapCallback callback) {
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {
                    subscriber.onNext(BitmapFactory.decodeStream(cr.openInputStream(uri)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    subscriber.onNext(null);
                } finally {
                    subscriber.onCompleted();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        callback.callback(bitmap);
                    }
                });
    }

    public interface BitmapCallback {
        void callback(Bitmap bitmap);
    }
}
