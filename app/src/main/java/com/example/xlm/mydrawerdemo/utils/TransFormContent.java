package com.example.xlm.mydrawerdemo.utils;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.view.ClickSpanMovementMethod;
import com.example.xlm.mydrawerdemo.view.MyClickableSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 鹏祺 on 2016/9/1.
 */
public class TransFormContent {
    public static void trans(final Spanned content, TextView tv, final OnClickListener onClickListener) {
        SpannableString spannableString = new SpannableString(content);
        if (content.toString().startsWith("[AD]")) {
            //广告
            URLSpan[] urlSpans = spannableString.getSpans(0, content.length(), URLSpan.class);
            if (urlSpans.length > 0) {
                spannableString.setSpan(new MyClickableSpan(urlSpans[0].getURL()), spannableString.getSpanStart(urlSpans[0]),
                        spannableString.getSpanEnd(urlSpans[0]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }else {
        Pattern pattern = Pattern.compile(">>.*");
        final Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            final String group = matcher.group();
            Log.d("spq", "group>>>>>>>>>>>>>>>" + group);
            if (null != onClickListener) {
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Log.d("spq", "onSpanClick>>>>>>>");
                        onClickListener.onClick(group);
                    }
                }, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }
        }


        tv.setText(spannableString);
        tv.setLinkTextColor(Color.parseColor("#7cb342"));
        //使用自定义的MovementMethod,解决子view不能把事件传递给父view的问题
        tv.setMovementMethod(ClickSpanMovementMethod.getInstance());
        //setMovementMethod以后，会自动把tv的focusable 设置为true影响外部的点击事件，需要手动设置false
        tv.setFocusable(false);
        tv.setClickable(false);
        tv.setLongClickable(false);

    }

    public interface OnClickListener {
        void onClick(String id);
    }

    public static void trans(final Spanned content, EditText editText, final OnClickListener onClickListener) {
        Pattern pattern = Pattern.compile(">>.*\n");
        final Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (onClickListener != null) {
                        onClickListener.onClick(content.subSequence(matcher.start() + 2, matcher.end() - 1).toString());
                    }
                }
            }, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            editText.setText(spannableString);
            editText.setLinkTextColor(Color.parseColor("#7cb342"));
            editText.setMovementMethod(LinkMovementMethod.getInstance());
//            tv.setFocusable(false);
//            tv.setClickable(false);
//            tv.setLongClickable(false);
        } else {
            editText.setText(content);
        }
    }
}
