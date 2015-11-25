package com.example.xlm.mydrawerdemo.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Spanned;

import com.example.xlm.mydrawerdemo.view.SecretTextView;


import java.util.List;
import java.util.logging.LogRecord;

/**
 * Created by xlm on 2015/11/25.
 */
public class Tools {
    public static final int CHANGE_TEXT=1;
    //轮流切换text
    public static void changeText(final SecretTextView textView,final List<Spanned> texts){
        class ChangeText implements Runnable{

            @Override
            public void run() {
                int i=0;
                while (i<=texts.size()){
                    textView.setText(texts.get(i));
                    textView.show();
                    i++;
                    //到最后的时候切换回第一个
                    if(i==texts.size()){
                        i=0;
                    }
                    Message message=new Message();
                    message.what=CHANGE_TEXT;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }

                }
            }
        };
    }
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
}
