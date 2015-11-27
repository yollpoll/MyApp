package com.example.xlm.mydrawerdemo.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.view.SecretTextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.LogRecord;

/**
 * Created by xlm on 2015/11/25.
 */
public class Tools {
    public static final int CHANGE_TEXT=1;
    public static class ChangeTextObj{
        TextView secretTextView;
        Spanned text;

        public ChangeTextObj(TextView secretTextView, Spanned text) {
            this.secretTextView = secretTextView;
            this.text = text;
        }

        public TextView getSecretTextView() {
            return secretTextView;
        }

        public void setSecretTextView(SecretTextView secretTextView) {
            this.secretTextView = secretTextView;
        }

        public Spanned getText() {
            return text;
        }

        public void setText(Spanned text) {
            this.text = text;
        }
    }
    //轮流切换text
    public static void changeText(final TextView textView,final List<Spanned> texts){
        class ChangeText implements Runnable{

            @Override
            public void run() {
                int i=0;
                while (i<=texts.size()){
                    i++;
                    //到最后的时候切换回第一个
                    if(i==texts.size()){
                        i=0;
                    }
                    Message message=Message.obtain();
                    message.what=CHANGE_TEXT;
                    ChangeTextObj changeTextObj=new ChangeTextObj(textView,texts.get(i));
                    message.obj=changeTextObj;
                    mHandler.sendMessage(message);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }

                }
            }
        };
        new Thread(new ChangeText()).start();
    }
    static Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CHANGE_TEXT:
                    ChangeTextObj c= (ChangeTextObj) msg.obj;
                    TextView secretView=c.secretTextView;
                    Spanned text=c.text;
                    secretView.setText(text);
                    break;
            }
        }
    };
    //时间转化，变成相对时间
    public static String replaceTime(String dateStr){
        String returnStr="";
        char[] buf=new char[10];
        char[] buf2=new char[8];
        dateStr.getChars(0,10,buf,0);
        dateStr.getChars(13,21,buf2,0);
        String tempStr=new String(buf);
        String tempStr2=new String(buf2);
        dateStr=tempStr+" "+tempStr2;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date=sdf.parse(dateStr);
            Date nowDate=new Date();
            Long time=nowDate.getTime()-date.getTime();
            if(time/(1000*60*60*24)>=2){
                //昨天以前
               returnStr=new String(buf);
            }else if(time/(1000*60*60*24)>=1&&(time/(1000*60*60*24)<2)){
                returnStr ="昨天";
            }else if(time/(1000*60*60*24)==0){
                //一天以内
                if(time/(1000*60*60)>0){
                    //一小时内上
                    returnStr=time/(1000*60*60)+"小时前";
                }else {
                    //一小时内
                    if(time/(1000*60)>0){
                        //一分钟以上
                        returnStr=time/(1000*60)+"分钟前";
                    }else {
                        returnStr="刚刚";
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("spq","时间 "+returnStr);
        return returnStr;
    }
}
