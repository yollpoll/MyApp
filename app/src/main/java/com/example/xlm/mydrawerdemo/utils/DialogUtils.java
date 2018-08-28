package com.example.xlm.mydrawerdemo.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by 鹏祺 on 2018/4/3.
 */

public class    DialogUtils {
    /**
     * 生成一个Alertdialog
     *
     * @param context
     * @param layoutId
     * @return
     */
    public static AlertDialog showDialog(Context context, int layoutId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    /**
     * 根据参数设置dialog宽高
     *
     * @param context
     * @param dialog
     * @param width
     * @param height
     */
    public static void setDialog(Activity context, Dialog dialog, int width, int height) {
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = width;
        layoutParams.height = height;
        window.setAttributes(layoutParams);
    }

    /**
     * @param context
     * @param dialog
     */
    public static void setDialogWrapContent(Activity context, Dialog dialog) {
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        Log.d("spq", display.getHeight() + "...");
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
    }
}
