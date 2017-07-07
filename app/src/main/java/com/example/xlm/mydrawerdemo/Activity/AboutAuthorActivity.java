package com.example.xlm.mydrawerdemo.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseActivity;

/**
 * Created by 鹏祺 on 2017/7/7.
 */

public class AboutAuthorActivity extends BaseActivity {
    private RelativeLayout rlAuthor, rlGit;

    public static void gotoAboutAuthorActivity(Context context) {
        Intent intent = new Intent(context, AboutAuthorActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_author);
        initView();
        initData();
    }

    private void initView() {
        rlAuthor = (RelativeLayout) findViewById(R.id.rl_author);
        rlGit = (RelativeLayout) findViewById(R.id.rl_git);

        rlAuthor.setOnClickListener(this);
        rlGit.setOnClickListener(this);
    }

    private void initData() {
        initToolbar("作者");
    }

    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + getResources().getString(R.string.author)));
        intent.putExtra(Intent.EXTRA_SUBJECT, "肥仔岛反馈");
        intent.putExtra(Intent.EXTRA_TEXT, "内容");
        startActivity(intent);
    }

    private void gotoGit() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(getResources().getString(R.string.github));
        intent.setData(content_url);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_author:
                sendEmail();
                break;
            case R.id.rl_git:
                gotoGit();
                break;
        }
    }
}
