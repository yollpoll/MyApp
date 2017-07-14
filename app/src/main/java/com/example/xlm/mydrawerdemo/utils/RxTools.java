package com.example.xlm.mydrawerdemo.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.xlm.mydrawerdemo.bean.Draft;
import com.example.xlm.mydrawerdemo.bean.DraftWithPath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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

    /**
     * 通过文件地址获得bitmap,rxJava异步
     *
     * @param path
     * @param callback
     */
    public static void DecodeStream(final String path, final BitmapCallback callback) {
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                File file = new File(path);
                FileInputStream fileInputStream;
                Bitmap bitmap;
                try {
                    fileInputStream = new FileInputStream(file);
                    subscriber.onNext(BitmapFactory.decodeStream(fileInputStream));
                } catch (FileNotFoundException e) {
                    subscriber.onNext(null);
                    e.printStackTrace();
                } finally {
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        callback.callback(bitmap);
                    }
                });
    }

    /**
     * 循环遍历获取bitmap集合
     * 将带地址的草稿列表转化成带bitmap的草稿列表
     */
    public static void DecodeStreamList(final List<DraftWithPath> draftWithPaths, final DraftCallback callback) {
        Observable.create(new Observable.OnSubscribe<List<DraftWithPath>>() {
            @Override
            public void call(Subscriber<? super List<DraftWithPath>> subscriber) {
                subscriber.onNext(draftWithPaths);
                subscriber.onCompleted();
            }
        }).flatMap(new Func1<List<DraftWithPath>, Observable<List<Draft>>>() {
            @Override
            public Observable<List<Draft>> call(List<DraftWithPath> draftWithPaths) {
                List<Draft> drafts = new ArrayList<Draft>();
                for (int i = 0; i < draftWithPaths.size(); i++) {
                    Bitmap bitmap = null;
                    File file = new File(draftWithPaths.get(i).getPicture());
                    FileInputStream fileInputStream;
                    try {
                        fileInputStream = new FileInputStream(file);
                        bitmap = BitmapFactory.decodeStream(fileInputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Draft draft = new Draft(draftWithPaths.get(i).getDate(), draftWithPaths.get(i).getContent(), bitmap);
                    drafts.add(draft);
                }
                List<List<Draft>> result = new ArrayList<List<Draft>>();
                result.add(drafts);
                return Observable.from(result);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Draft>>() {
                    @Override
                    public void call(List<Draft> drafts) {
                        callback.callback(drafts);
                    }
                });
    }

    public interface BitmapCallback {
        void callback(Bitmap bitmap);
    }

    public interface DraftCallback {
        void callback(List<Draft> drafts);
    }
}
