package com.example.xlm.mydrawerdemo.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.utils.CookieUtils;
import com.example.xlm.mydrawerdemo.utils.DialogUtils;
import com.example.xlm.mydrawerdemo.utils.PermissionUtils;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;
import com.example.xlm.mydrawerdemo.utils.Tools;

/**
 * Created by 鹏祺 on 2018/4/3.
 */

public class SettingActivity extends BaseActivity {
    public static final int REQUEST_SD = 123;

    public static void gotoSettingActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    private RelativeLayout rlCurrentCookieQr, rlSaveCookieQr, rlShareCookieQr;
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
        rlSaveCookieQr = (RelativeLayout) findViewById(R.id.rl_save_cookie);
        rlShareCookieQr = (RelativeLayout) findViewById(R.id.rl_share_cookie);

        rlShareCookieQr.setOnClickListener(this);
        llRoot.setOnClickListener(this);
        rlSaveCookieQr.setOnClickListener(this);
        rlCurrentCookieQr.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_get_cookie:
                showCookieQr();
                break;
            case R.id.rl_save_cookie:
                //申请权限
                //保存二维码
                PermissionUtils.checkAndRequestPermission(this, PermissionUtils.WRITE_EXTERNAL_STORAGE, REQUEST_SD,
                        onExplainPermission, onPermissionGet);
                break;
            case R.id.rl_share_cookie:
                Tools.saveImageToSdViaAsyncTask(CookieUtils.getCookieQr(), "cookie_currentCookieQr.jpg", onShareCallBack);
                break;
        }
    }

    private void showCookieQr() {
        if (!CookieUtils.checkCookie()) {
            ToastUtils.SnakeShowShort(llRoot, "没有饼干");
            return;
        }
        Dialog alertDialog = DialogUtils.showDialog(this, R.layout.dialog_qr);
        DialogUtils.setDialog(this, alertDialog, Tools.calculateDpToPx(300, this), Tools.calculateDpToPx(300, this));
        ImageView ivQr = (ImageView) alertDialog.findViewById(R.id.iv_qr);
        ivQr.setImageBitmap(CookieUtils.getCookieQr());
    }

    private void saveCookieQr() {
        if (!CookieUtils.checkCookie()) {
            ToastUtils.SnakeShowShort(llRoot, "没有饼干");
            return;
        }
        Tools.saveImageToSdViaAsyncTask(CookieUtils.getCookieQr(), "cookie_currentCookieQr.jpg", new Tools.OnSaveImageCallback() {
            @Override
            public void callback(String path) {
                ToastUtils.SnakeShowShort(llRoot, "保存成功");
            }
        });
    }

    Tools.OnSaveImageCallback onShareCallBack = new Tools.OnSaveImageCallback() {
        @Override
        public void callback(String path) {
            Tools.shareMsg("分享饼干", "分享饼干", "分享饼干二维码", path, SettingActivity.this);
        }
    };
    PermissionUtils.OnExplainPermission onExplainPermission = new PermissionUtils.OnExplainPermission() {
        @Override
        public void onExplain() {

        }
    };

    //这个是已经获得权限以后
    PermissionUtils.OnPermissionGet onPermissionGet = new PermissionUtils.OnPermissionGet() {
        @Override
        public void onGet() {
            saveCookieQr();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_SD:
                if (PermissionUtils.changeRequestResult(grantResults)) {
                    saveCookieQr();
                } else {
                    ToastUtils.SnakeShowShort(llRoot, "获取权限失败，无法保存二维码");
                }
                break;
        }
    }
}
