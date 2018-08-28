package com.example.xlm.mydrawerdemo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by 鹏祺 on 2017/7/10.
 */

public class PermissionUtils {
    public static String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;//SD 卡读写

    /**
     * 检查权限
     *
     * @param activity
     * @param permission
     * @return
     */
    public static boolean checkPermission(Activity activity, String permission) {
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 申请权限
     *
     * @param activity
     * @param permissions
     * @param requestCode
     */
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 检查同时申请
     *
     * @param activity
     * @param permission
     * @param requestCode
     */
    public static void checkAndRequestPermission(Activity activity, String permission, int requestCode,
                                                 OnExplainPermission onExplainPermission, OnPermissionGet onPermissionGet) {
        if (!checkPermission(activity, permission)) {
            //需要申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                //需要解释权限
                onExplainPermission.onExplain();
            }
            requestPermissions(activity, new String[]{permission}, requestCode);
        } else {
            onPermissionGet.onGet();
        }
    }

    /**
     * 申请权限时转化结果，只申请一个权限的时候
     *
     * @param grantResults
     * @return
     */
    public static boolean changeRequestResult(int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //通过
            return true;
        } else {
            //失败
            return false;
        }
    }

    /**
     * 权限申请已经通过了的情况
     */
    public interface OnPermissionGet {
        void onGet();
    }

    /**
     * 解释权限申请
     */
    public interface OnExplainPermission {
        void onExplain();
    }
}
