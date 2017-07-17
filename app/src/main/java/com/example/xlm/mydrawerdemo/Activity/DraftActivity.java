package com.example.xlm.mydrawerdemo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.CollectionAdapter;
import com.example.xlm.mydrawerdemo.adapter.DraftAdapter;
import com.example.xlm.mydrawerdemo.adapter.OnItemClickListenr;
import com.example.xlm.mydrawerdemo.adapter.OnItemLongClickListener;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.bean.CollectionBean;
import com.example.xlm.mydrawerdemo.bean.Draft;
import com.example.xlm.mydrawerdemo.utils.RxTools;
import com.example.xlm.mydrawerdemo.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 草稿页面
 * Created by 鹏祺 on 2017/7/14.
 */

public class DraftActivity extends BaseActivity {
    public static final int GET_DRAFT = 12345;

    private RecyclerView rvDraft;
    private DraftAdapter mAdapter;
    private List<Draft> listDraft = new ArrayList<>();
    private ActionMode actionMode;
    boolean isActionModeShow = false;//actionmode是否显示
    private LinearLayoutManager layoutManager;

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
        flag_key = true;//返回鍵不用父類的方法
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

        mAdapter = new DraftAdapter(listDraft, onItemClickListenr,
                onItemLongClickListener, onCheckListener, onImageClickListener);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDraft.setLayoutManager(layoutManager);
        rvDraft.setAdapter(mAdapter);
        getData();
    }

    private void initView() {
        rvDraft = (RecyclerView) findViewById(R.id.rv_draft);
    }

    private OnItemClickListenr onItemClickListenr = new OnItemClickListenr() {
        @Override
        public void onItemClick(View view, int position) {
            if (isActionModeShow) {
                listDraft.get(position).setCheck(!listDraft.get(position).isCheck());
                mAdapter.notifyDataSetChanged();
            } else {
                final Draft draft = listDraft.get(position);
                Draft.changeBitmapToPath(draft, new Tools.OnSaveImageCallback() {
                    @Override
                    public void callback(String path) {
                        Intent intent = getIntent();
                        intent.putExtra("content", draft.getContent());
                        intent.putExtra("picPath", path);
                        intent.putExtra("date", draft.getDate());
                        DraftActivity.this.setResult(RESULT_OK, intent);
                        DraftActivity.this.finish();
                    }
                });
            }
        }
    };
    private DraftAdapter.OnImageClickListener onImageClickListener = new DraftAdapter.OnImageClickListener() {
        @Override
        public void onClick(View view, int position) {
            ImageActivity.gotoImageActivity(DraftActivity.this, listDraft.get(position).getPicture(), view);
        }
    };
    private DraftAdapter.OnCheckListener onCheckListener = new DraftAdapter.OnCheckListener() {
        @Override
        public void onCheck(boolean isCheck, int position) {
            listDraft.get(position).setCheck(isCheck);
        }
    };
    private OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {
        @Override
        public void onLongClick(View view, int position) {
            if (!isActionModeShow) {
                actionMode = startSupportActionMode(actionModeCallback);
            }
        }
    };

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_del_collection, menu);
            isActionModeShow = true;
            return true;//返回true表示action mode会被创建  false if entering this mode should be aborted.
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            showCheckBox();
            mode.setTitle("删除");
//            mSwipFresh.setEnabled(false);
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
//                case R.id.menu_del_collection:
//                    for (int i = 0; i < list.size(); i++) {
//                        if (list.get(i).isCheck()) {
//                            delList.add(list.get(i));
//                        }
//                    }
//                    delCollection();
//                    break;
//                case R.id.menu_clear_collection:
//                    if (item.getTitle().equals("全选")) {
//                        item.setTitle("取消");
//                        for (CollectionBean bean : list) {
//                            bean.setCheck(true);
//                        }
//                    } else if (item.getTitle().equals("取消")) {
//                        item.setTitle("全选");
//                        for (CollectionBean bean : list) {
//                            bean.setCheck(false);
//                        }
//                    }
//                    mAdapter.notifyDataSetChanged();
//                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            disMissCheckBox();
//            mSwipFresh.setEnabled(true);
            isActionModeShow = false;
        }
    };

    //显示多选框
    private void showCheckBox() {
        TranslateAnimation animation = new TranslateAnimation(0f, -200f, 0f, 0f);
        animation.setDuration(400);
        animation.setFillAfter(true);
        for (int i = 0; i < rvDraft.getChildCount(); i++) {
            View view = rvDraft.getChildAt(i);
            DraftAdapter.ViewHolder viewHolder = (DraftAdapter.ViewHolder) rvDraft.getChildViewHolder(view);
            viewHolder.rlContent.startAnimation(animation);
        }
        animation.setAnimationListener(showCheckBoxListener);
    }

    //隐藏多选框
    private void disMissCheckBox() {
        TranslateAnimation animation = new TranslateAnimation(0f, 200f, 0f, 0f);
        animation.setDuration(400);
        animation.setFillAfter(true);
        for (int i = 0; i < layoutManager.getChildCount(); i++) {
            View view = rvDraft.getChildAt(i);
            DraftAdapter.ViewHolder viewHolder = (DraftAdapter.ViewHolder) rvDraft.getChildViewHolder(view);
            viewHolder.rlContent.startAnimation(animation);
        }
        animation.setAnimationListener(DismisCheckBoxListener);
    }

    private Animation.AnimationListener showCheckBoxListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            for (int i = 0; i < listDraft.size(); i++) {
                listDraft.get(i).setShowCheck(true);
            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
    private Animation.AnimationListener DismisCheckBoxListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            for (int i = 0; i < listDraft.size(); i++) {
                listDraft.get(i).setShowCheck(false);
            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}
