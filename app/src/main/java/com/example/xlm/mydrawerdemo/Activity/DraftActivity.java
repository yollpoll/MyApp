package com.example.xlm.mydrawerdemo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.DraftAdapter;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.bean.Draft;
import com.example.xlm.mydrawerdemo.utils.RxTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 草稿页面
 * Created by 鹏祺 on 2017/7/14.
 */

public class DraftActivity extends BaseActivity {
    public static final int GET_DRAFT = 123;

    private RecyclerView rvDraft;
    private DraftAdapter mAdapter;
    private List<Draft> listDraft = new ArrayList<>();

    public static void gotoDraftActivity(Activity activity) {
        Intent intent = new Intent(activity, DraftActivity.class);
        activity.startActivityForResult(intent, GET_DRAFT);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        initView();
        initData();
    }

    private void getData() {
        Draft.loadDrafts(new RxTools.DraftCallback() {
            @Override
            public void callback(List<Draft> drafts) {
                listDraft.addAll(drafts);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initData() {
        initToolbar("草稿");

        mAdapter = new DraftAdapter(listDraft);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDraft.setLayoutManager(layoutManager);
        rvDraft.setAdapter(mAdapter);
        getData();
    }

    private void initView() {
        rvDraft = (RecyclerView) findViewById(R.id.rv_draft);
    }
}
