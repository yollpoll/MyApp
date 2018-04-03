package com.example.xlm.mydrawerdemo.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.example.xlm.mydrawerdemo.R;

/**
 * Created by 鹏祺 on 2018/4/3.
 */

public class MyDialog extends Dialog {
    public MyDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.MyDialogStyle);
    }
}
