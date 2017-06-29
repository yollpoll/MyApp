package com.example.xlm.mydrawerdemo.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseActivity;

/**
 * Created by 鹏祺 on 2017/6/29.
 */

public class WebActivity extends BaseActivity {
    private String path;
    private WebView mWebView;
    private WebSettings webSettings;

    public static void gotoWebActivity(Context context, String path) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("path", path);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();
        initData();
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.web_view);
    }

    private void initData() {
        path = "content://com.android.htmlfileprovider" + getIntent().getStringExtra("path");
        mWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl(path);
    }
}
