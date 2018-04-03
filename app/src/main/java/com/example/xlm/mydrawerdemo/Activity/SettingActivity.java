package com.example.xlm.mydrawerdemo.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.utils.DialogUtils;
import com.example.xlm.mydrawerdemo.utils.SPUtiles;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;
import com.example.xlm.mydrawerdemo.zxing.encoding.EncodingHandler;

/**
 * Created by 鹏祺 on 2018/4/3.
 */

public class SettingActivity extends BaseActivity {
    public static void gotoSettingActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    private RelativeLayout rlCurrentCookieQr;
    private LinearLayout llRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initData();
    }

    private void initData() {
        initToolbar("设置");
    }

    private void initView() {
        rlCurrentCookieQr = (RelativeLayout) findViewById(R.id.rl_get_cookie);
        llRoot = (LinearLayout) findViewById(R.id.ll_root);

        llRoot.setOnClickListener(this);
        rlCurrentCookieQr.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_get_cookie:
                showCookieQr();
                break;
        }
    }

    private void showCookieQr() {
        String cookie = SPUtiles.getCookie();
        if (TextUtils.isEmpty(cookie)) {
            ToastUtils.SnakeShowShort(llRoot, "没有饼干");
            return;
        }
        Dialog alertDialog = DialogUtils.showDialog(this, R.layout.dialog_qr);
        DialogUtils.setDialog(this, alertDialog,1000,1000);
        ImageView ivQr = (ImageView) alertDialog.findViewById(R.id.iv_qr);
        Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Bitmap qr = EncodingHandler.createQRCode(cookie, 200, 200, bitmap);
        ivQr.setImageBitmap(qr);
    }
}
