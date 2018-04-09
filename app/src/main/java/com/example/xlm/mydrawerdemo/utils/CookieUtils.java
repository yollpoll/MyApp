package com.example.xlm.mydrawerdemo.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.example.xlm.mydrawerdemo.zxing.encoding.EncodingHandler;

/**
 * Created by 鹏祺 on 2018/4/4.
 */

public class CookieUtils {
    public static Bitmap getCookieQr() {
        Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        final Bitmap qr = EncodingHandler.createQRCode(getCookie(), 200, 200, bitmap);
        return qr;
    }

    public static String getCookie() {
        String cookie = SPUtiles.getCookie();
        return cookie;
    }

    public static void saveCookie(String cookie) {
        SPUtiles.saveCookie(cookie);
    }

    public static boolean checkCookie() {
        String cookie = SPUtiles.getCookie();
        if (TextUtils.isEmpty(cookie))
            return false;
        return true;
    }
}
