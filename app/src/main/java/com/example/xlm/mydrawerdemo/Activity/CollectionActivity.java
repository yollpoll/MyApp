package com.example.xlm.mydrawerdemo.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;

import com.example.xlm.mydrawerdemo.retrofitService.CollectionService;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.CollectionAdapter;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.base.MyApplication;
import com.example.xlm.mydrawerdemo.bean.CollectionBean;
import com.example.xlm.mydrawerdemo.http.Httptools;
import com.example.xlm.mydrawerdemo.http.Port;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by 鹏祺 on 2017/5/25.
 */

public class CollectionActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private List<CollectionBean> list = new ArrayList<>();
    private List<CollectionBean> delList = new ArrayList<>();
    private List<String> ids = new ArrayList<>();
    private CollectionAdapter mAdapter;
    private RecyclerView rvCollection;
    private Toolbar mToolbar;
    private Retrofit retrofit;
    private int page = 1;
    private ProgressBar mProgressBar;
    private boolean isLoad;//判断是否加载中;
    private SwipeRefreshLayout mSwipFresh;
    private String uuid;
    private LinearLayoutManager layoutManager;
    private CollectionService collectionService;
    private ActionMode actionMode;

    public static void gitoCollectionActivity(Context context) {
        Intent intent = new Intent(context, CollectionActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        initView();
        initData();
    }


    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        rvCollection = (RecyclerView) findViewById(R.id.rv_collection);
        mSwipFresh = (SwipeRefreshLayout) findViewById(R.id.swip_refresh);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mSwipFresh.setOnRefreshListener(this);
    }

    private void initData() {
        flag_key=true;
        initToolbar("订阅");
        uuid = MyApplication.getInstance().getUuId();
        mAdapter = new CollectionAdapter(list, onItemClickListener);
        rvCollection.setAdapter(mAdapter);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCollection.setLayoutManager(layoutManager);
        rvCollection.setItemAnimator(new DefaultItemAnimator());

//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_space);
//        rvCollection.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        mSwipFresh.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_blue_light);
        rvCollection.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    if (isLoad || isActionModeShow) {
                    } else {
                        loadMore();//这里多线程也要手动控制isLoadingMore
                    }
                }

            }
        });

        retrofit = Httptools.getInstance().getRetrofit();
        refresh();
    }

    private void loadMore() {
        page++;
        getData();
    }

    private void refresh() {
        page = 1;
        getData();
    }

    private void getData() {
        if (isActionModeShow)
            disMissCheckBox();
        isLoad = true;
        collectionService = retrofit.create(CollectionService.class);
        Call<List<CollectionBean>> call = collectionService.getCollection(page, uuid);
        call.enqueue(new Callback<List<CollectionBean>>() {
            @Override
            public void onResponse(Response<List<CollectionBean>> response, Retrofit retrofit) {
                if (response == null)
                    return;
                isLoad = false;
                if (page == 1) {
                    list.clear();
                    mSwipFresh.setRefreshing(false);
                }
                list.addAll(response.body());
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
                //刷新本地數據
                ids.clear();
                for (CollectionBean collectionBean : list) {
                    ids.add(collectionBean.getId());
                }
                CollectionBean.saveIds(ids);
            }

            @Override
            public void onFailure(Throwable throwable) {
                mProgressBar.setVisibility(View.GONE);
                isLoad = false;
                if (page == 1)
                    mSwipFresh.setRefreshing(false);
            }
        });
    }

    private CollectionAdapter.OnItemClickListener onItemClickListener = new CollectionAdapter.OnItemClickListener() {
        @Override
        public void onClick(View view, int position) {
            Intent intent = new Intent(CollectionActivity.this, ChildArticleActivity.class);
            intent.putExtra("id", list.get(position).getId());
            intent.putExtra("title", list.get(position).getTitle());
            startActivity(intent);
        }

        @Override
        public void onImageClick(View view, int position) {
            String url = Port.IMG_URL + list.get(position).getImg() + list.get(position).getExt();
            ImageActivity.gotoImageActivity(CollectionActivity.this, url, list.get(position).getImg() + list.get(position).getExt(), view);
        }

        @Override
        public void onLongClick(View view, int position) {
            if (!isActionModeShow) {
                actionMode = startSupportActionMode(actionModeCallback);
            }
        }

        @Override
        public void onCheck(boolean isChecked, int position) {
            list.get(position).setCheck(isChecked);
        }
    };

    //显示多选框
    private void showCheckBox() {
        TranslateAnimation animation = new TranslateAnimation(0f, -200f, 0f, 0f);
        animation.setDuration(400);
        animation.setFillAfter(true);
        for (int i = 0; i < rvCollection.getChildCount(); i++) {
            View view = rvCollection.getChildAt(i);
            CollectionAdapter.ViewHolder viewHolder = (CollectionAdapter.ViewHolder) rvCollection.getChildViewHolder(view);
            viewHolder.cardRoot.startAnimation(animation);
        }
        animation.setAnimationListener(showCheckBoxListener);
    }

    private Animation.AnimationListener showCheckBoxListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setShowCheck(true);
            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    //隐藏多选框
    private void disMissCheckBox() {
        TranslateAnimation animation = new TranslateAnimation(0f, 200f, 0f, 0f);
        animation.setDuration(400);
        animation.setFillAfter(true);
        for (int i = 0; i < layoutManager.getChildCount(); i++) {
            View view = rvCollection.getChildAt(i);
            CollectionAdapter.ViewHolder viewHolder = (CollectionAdapter.ViewHolder) rvCollection.getChildViewHolder(view);
            viewHolder.cardRoot.startAnimation(animation);
        }
        animation.setAnimationListener(DismisCheckBoxListener);
    }

    private Animation.AnimationListener DismisCheckBoxListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setShowCheck(false);
            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    boolean isActionModeShow = false;//actionmode是否显示
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
            mSwipFresh.setEnabled(false);
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_del_collection:
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isCheck()) {
                            delList.add(list.get(i));
                        }
                    }
                    delCollection();
                    break;
                case R.id.menu_clear_collection:
                    if (item.getTitle().equals("全选")) {
                        item.setTitle("取消");
                        for (CollectionBean bean : list) {
                            bean.setCheck(true);
                        }
                    } else if (item.getTitle().equals("取消")) {
                        item.setTitle("全选");
                        for (CollectionBean bean : list) {
                            bean.setCheck(false);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            disMissCheckBox();
            mSwipFresh.setEnabled(true);
            isActionModeShow = false;
        }
    };


    int delCount = 0;

    private void delCollection() {
        if (delList.size() == 0)
            return;
        String tid = delList.get(delCount).getId();
        Call<String> call = collectionService.delCollection(uuid, tid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (delCount == delList.size() - 1) {
                    if (null != actionMode)
                        actionMode.finish();
                    onRefresh();
                    ToastUtils.SnakeShowShort(mSwipFresh, response.body());
                } else {
                    delCount++;
                    delCollection();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    public void onRefresh() {
        refresh();
    }
}
