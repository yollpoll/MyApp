package com.example.xlm.mydrawerdemo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.bean.ChildForm;
import com.example.xlm.mydrawerdemo.utils.SPUtiles;
import com.example.xlm.mydrawerdemo.view.ChangeLineViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 鹏祺 on 2017/6/13.
 */

public class ChooseTagActivity extends BaseActivity {
    public static final int CHOOSE_TAG = 1;
    private List<ChildForm> listForums = new ArrayList<>();
    private ChangeLineViewGroup clgTags;
    private static NewThreadActivity newThreadActivity;
    private String tagId;

    public static void gotoChooseTagActivity(Activity activity, String tagId, Pair<View, String>... pairs) {
        newThreadActivity = (NewThreadActivity) activity;
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, pairs);
        Intent intent = new Intent(activity, ChooseTagActivity.class);
        intent.putExtra("tagId", tagId);
        activity.startActivityForResult(intent, CHOOSE_TAG, options.toBundle());
    }

    private void initView() {
        clgTags = (ChangeLineViewGroup) findViewById(R.id.clg_tags);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tag);
        getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        initView();
        initData();
    }

    private void initData() {
        tagId = getIntent().getStringExtra("tagId");
        listForums = SPUtiles.getTags();
//        removeTimeLine();
        for (int i = 0; i < listForums.size(); i++) {
            TextView tvTag = (TextView) LayoutInflater.from(this).inflate(R.layout.item_tag, null, false).findViewById(R.id.tv_tag);
            ViewGroup viewGroup = (ViewGroup) tvTag.getParent();
            if (null != viewGroup) {
                viewGroup.removeAllViews();
            }
            tvTag.setText(listForums.get(i).getName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tvTag.setTransitionName(listForums.get(i).getId());
            }
            tvTag.setTag(R.id.tag_first, listForums.get(i).getId());
            tvTag.setTag(R.id.tag_second, listForums.get(i).getName());
            tvTag.setOnClickListener(onTagClickListener);
            clgTags.addView(tvTag);
        }
    }

    private View.OnClickListener onTagClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                for (int i = 0; i < clgTags.getChildCount(); i++) {
                    if (tagId.equals(clgTags.getChildAt(i).getTransitionName())) {
                        clgTags.getChildAt(i).setTransitionName("");
                        break;
                    }
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                v.setTransitionName(tagId);
            }
            Intent intent = getIntent();
            intent.putExtra("tagId", (String) v.getTag(R.id.tag_first));
            intent.putExtra("tagName", (String) v.getTag(R.id.tag_second));
            newThreadActivity.setTagName((String) v.getTag(R.id.tag_second));
            ChooseTagActivity.this.setResult(RESULT_OK, intent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ChooseTagActivity.this.finishAfterTransition();
            } else {
                ChooseTagActivity.this.finish();
            }
        }
    };

    //暂时去掉时间线功能
    private void removeTimeLine() {
        Iterator iterator = listForums.iterator();
        while (iterator.hasNext()) {
            ChildForm childForm = (ChildForm) iterator.next();
            if (childForm.getId().equals("-1")) {
                iterator.remove();
            }
        }
    }
}
