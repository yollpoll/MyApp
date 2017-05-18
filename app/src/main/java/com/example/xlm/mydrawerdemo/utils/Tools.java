package com.example.xlm.mydrawerdemo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.R;
import android.widget.ViewSwitcher;

import com.example.xlm.mydrawerdemo.view.SecretTextView;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    }catch (Exception e){
            Log.i("yollpoll",dateStr);
    }
    return "未知异次元时间";
    }

    /**获取应用名
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
     * @param activityTitle
     * @param msgTitle
     * @param msgText
     * @param imgPath
     */
    public static void shareMsg(String activityTitle, String msgTitle, String msgText,
                         String imgPath,Context context) {


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
}
