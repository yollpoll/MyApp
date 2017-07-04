package com.example.xlm.mydrawerdemo.utils;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xlm.mydrawerdemo.base.MyApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 鹏祺 on 2016/9/1.
 */
public class TransFormContent {
    public static void trans(final Spanned content, TextView tv, final OnClickListener onClickListener) {
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
            tv.setText(spannableString);
            tv.setLinkTextColor(Color.parseColor("#7cb342"));
            tv.setMovementMethod(LinkMovementMethod.getInstance());
//            tv.setFocusable(false);
//            tv.setClickable(false);
//            tv.setLongClickable(false);
        } else {
            tv.setText(content);
        }
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
