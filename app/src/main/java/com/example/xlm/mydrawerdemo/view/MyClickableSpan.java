package com.example.xlm.mydrawerdemo.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.example.xlm.mydrawerdemo.Activity.ChildArticleActivity;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;

/**
 * Created by 鹏祺 on 2018/4/27.
 */

public class MyClickableSpan extends ClickableSpan {
    private String url;

    public MyClickableSpan(String url) {
        this.url = url;
    }

    @Override
    public void onClick(View widget) {
        if (url.split("/").length < 3)
            ToastUtils.showShort("地址出错");
        String[] content = url.split("/");
        String id = content[content.length - 1];
        if (url.contains("adnmb.com")) {
            //串跳转
            ChildArticleActivity.gotoChildArticleActivity(widget.getContext(), id, null);
        } else {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            widget.getContext().startActivity(intent);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(true);
        ds.setColor(Color.parseColor("#7cb342"));
    }
}
