package com.example.xlm.mydrawerdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;

import com.example.xlm.mydrawerdemo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by xlm on 2015/11/25.
 */
public class Tools {
    public static final int CHANGE_TEXT = 1;

    public static class ChangeTextObj {
        TextSwitcher textSwitcher;
        Spanned text;
        Context context;

        public ChangeTextObj(TextSwitcher textSwitcher, Spanned text, Context context) {
            this.textSwitcher = textSwitcher;
            this.text = text;
            this.context = context;
        }

        public TextSwitcher getSecretTextView() {
            return textSwitcher;
        }

        public void setSecretTextView(TextSwitcher secretTextView) {
            this.textSwitcher = secretTextView;
        }

        public Spanned getText() {
            return text;
        }

        public void setText(Spanned text) {
            this.text = text;
        }
    }

    //轮流切换text
    public static void changeText(final TextSwitcher textSwitcher, final List<Spanned> texts, final Context context) {
        class ChangeText implements Runnable {

            @Override
            public void run() {
                int i = 0;
                while (i < texts.size()) {
                    //到最后的时候切换回第一个
//                    if (i == texts.size()) {
//                        i = 0;
//                    }
                    Message message = Message.obtain();
                    message.what = CHANGE_TEXT;
                    ChangeTextObj changeTextObj = new ChangeTextObj(textSwitcher, texts.get(i), context);
                    message.obj = changeTextObj;
                    mHandler.sendMessage(message);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                    i++;
                }
            }
        }
        ;
        new Thread(new ChangeText()).start();
    }

    static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHANGE_TEXT:
                    final ChangeTextObj c = (ChangeTextObj) msg.obj;
                    TextSwitcher textSwitcher = c.textSwitcher;
                    Spanned text = c.text;
                    textSwitcher.setText(text);
                    break;
            }
        }
    };

    //时间转化，变成相对时间
    public static String replaceTime(String dateStr) {
        try {
            String returnStr = "";
            char[] buf = new char[10];
            char[] buf2 = new char[8];
            dateStr.getChars(0, 10, buf, 0);
            dateStr.getChars(13, 21, buf2, 0);
            String tempStr = new String(buf);
            String tempStr2 = new String(buf2);
            dateStr = tempStr + " " + tempStr2;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                Date date = sdf.parse(dateStr);
                Date nowDate = new Date();
                Long time = nowDate.getTime() - date.getTime();
                if (time / (1000 * 60 * 60 * 24) >= 2) {
                    //昨天以前
                    returnStr = new String(buf);
                } else if (time / (1000 * 60 * 60 * 24) >= 1 && (time / (1000 * 60 * 60 * 24) < 2)) {
                    returnStr = "昨天";
                } else if (time / (1000 * 60 * 60 * 24) == 0) {
                    //一天以内
                    if (time / (1000 * 60 * 60) > 0) {
                        //一小时内上
                        returnStr = time / (1000 * 60 * 60) + "小时前";
                    } else {
                        //一小时内
                        if (time / (1000 * 60) > 0) {
                            //一分钟以上
                            returnStr = time / (1000 * 60) + "分钟前";
                        } else {
                            returnStr = "刚刚";
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return returnStr;
        } catch (Exception e) {
            Log.i("yollpoll", dateStr);
        }
        return "未知异次元时间";
    }


    /**
     * 秒数变成具体时间
     *
     * @param timeStr
     * @return
     */
    public static String changeTime(String timeStr) {
        int time = 0;
        try {
            time = Integer.parseInt(timeStr);
        } catch (Exception e) {
            return "";
        }
        try {
            int day = time / (60 * 60 * 24);
            int hour = (time % (day * 60 * 60 * 24)) / (60 * 60);
            int min = (time - day * (60 * 60 * 24) - hour * (60 * 60)) / 60;
            int second = time - day * (60 * 60 * 24) - hour * (60 * 60) - min * (60);
            return day + "日" + hour + "时" + min + "分" + second + "秒";
        } catch (ArithmeticException e) {
            return "未知时间";
        }
    }


    /**
     * 根据秒数返回日期
     *
     * @param time
     * @return
     */
    public static String getDate(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
        return simpleDateFormat.format(new Date(time));
    }

    /**
     * 获取应用名
     *
     * @param context
     * @return
     */
    public static String getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    /**
     * 分享
     *
     * @param activityTitle
     * @param msgTitle
     * @param msgText
     * @param imgPath
     */
    public static void shareMsg(String activityTitle, String msgTitle, String msgText,
                                String imgPath, Context context) {


        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/png");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, activityTitle));
    }

    /**
     * 设置某个View的margin
     *
     * @param view   需要设置的view
     * @param isDp   需要设置的数值是否为DP
     * @param left   左边距
     * @param right  右边距
     * @param top    上边距
     * @param bottom 下边距
     * @return
     */
    public static ViewGroup.LayoutParams setViewMargin(View view, boolean isDp, Context context, int left, int right, int top, int bottom) {
        if (view == null) {
            return null;
        }

        int leftPx = left;
        int rightPx = right;
        int topPx = top;
        int bottomPx = bottom;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = null;
        //获取view的margin设置参数
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            //不存在时创建一个新的参数
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }

        //根据DP与PX转换计算值
        if (isDp) {
            leftPx = calculateDpToPx(left, context);
            rightPx = calculateDpToPx(right, context);
            topPx = calculateDpToPx(top, context);
            bottomPx = calculateDpToPx(bottom, context);
        }
        //设置margin
        marginParams.setMargins(leftPx, topPx, rightPx, bottomPx);
        view.setLayoutParams(marginParams);
        return marginParams;
    }

    public static int calculateDpToPx(int padding_in_dp, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (padding_in_dp * scale + 0.5f);
    }

    public static final int PIC_FROM_PHOTO = 11;
    public static final int PIC_FROM_CAMERA = 12;
    public static final int CROP_PHOTO = 13;
    public static Uri imgUri;

    public static void showChoosePicDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.alert_choose_photo, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        RelativeLayout rlPhoto = (RelativeLayout) view.findViewById(R.id.rl_photo);
        RelativeLayout rlCamera = (RelativeLayout) view.findViewById(R.id.rl_camera);
        rlCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                File file = activity.getExternalCacheDir();
                File imgFile = new File(file, "temp.jpg");
                if (file.exists())
                    file.delete();
                try {
                    imgFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imgUri = Uri.fromFile(imgFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                activity.startActivityForResult(intent, PIC_FROM_CAMERA);

            }
        });
        rlPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activity.startActivityForResult(intent, PIC_FROM_PHOTO);
            }
        });
    }

    public interface OnSaveImageCallback {
        void callback(String path);
    }

    /**
     * rxJava实现
     * 异步的方式存储图片
     *
     * @param context
     * @param bitmap
     * @param imageName
     * @param callback
     */
    public static void saveImageViaAsyncTask(final Context context, final Bitmap bitmap, final String imageName, final OnSaveImageCallback callback) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(saveImage(context, bitmap, imageName));
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        callback.callback(s);
                    }
                });
    }

    /**
     * 保存图片到缓存目录
     *
     * @param context
     * @param bitmap
     */
    public static String saveImage(Context context, Bitmap bitmap, String imageName) {
        if (null == bitmap)
            return "";
        //替换/
        String img = imageName.replace("/", "_");
        File cacheDir = context.getExternalCacheDir();
        File cacheImage = new File(cacheDir + "/" + img);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(cacheImage);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
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
        return cacheImage.getAbsolutePath();
    }


    /**
     * rxJava实现
     * 异步方式存储图片
     *
     * @param bitmap
     * @param imageName
     * @param callback
     */
    public static void saveImageToSdViaAsyncTask(final Bitmap bitmap, final String imageName, final OnSaveImageCallback callback) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(saveImageToSd(bitmap, imageName));
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        callback.callback(s);
                    }
                });
    }

    /**
     * 保存图片到SD卡目录
     * 这个方法应当写在子线程
     *
     * @param bitmap
     */
    public static String saveImageToSd(Bitmap bitmap, String imageName) {
        if (null == bitmap)
            return "";
        //替换/
        String img = imageName.replace("/", "_");
        File cacheDir = new File(Environment.getExternalStorageDirectory() + Constant.SD_CACHE_DIR + Constant.IMG_CACHE);
        if (!cacheDir.exists())
            cacheDir.mkdir();
        File cacheImage = new File(cacheDir + "/" + img);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(cacheImage);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
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
        return cacheImage.getAbsolutePath();
    }

    /**
     * 更新图片库
     */
    public static void updatePhoto(Context context, String path, String fileName) {
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    path, fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(path))));
    }

    /**
     * 压缩图片
     *
     * @param path
     * @param destWidth
     * @param destHeight
     * @return
     */
    public static Bitmap compressBitmap(String path, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        int inSampleSize = 1;
        while (outHeight / inSampleSize > destHeight || outWidth / inSampleSize > destWidth) {
            inSampleSize *= 2;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(path, options);
    }
}
