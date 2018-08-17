package com.example.xlm.mydrawerdemo.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.BeautyAdapter;
import com.example.xlm.mydrawerdemo.adapter.FooterAdapter;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;

import org.androidannotations.annotations.Bean;

import java.util.ArrayList;
import java.util.List;

public class BeautyActivity extends BaseActivity {
    private Toolbar toolbar;
    private RecyclerView rvBeauty;
    private BeautyAdapter mAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private GridLayoutManager gridLayoutManager;
    private List<AVFile> listFile = new ArrayList<>();
    private boolean isLoadingMore, isRefresh;
    private int page = 0;

    public static void gotoBeautyActivity(Context context) {
        Intent intent = new Intent(context, BeautyActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty);
        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_beauty, menu);
        return true;
    }

    private void initData() {
        getSupportActionBar().setTitle("祭品库");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeautyActivity.this.finish();
            }
        });
        mAdapter = new BeautyAdapter(listFile,onItemClickListener);
        rvBeauty.setAdapter(mAdapter);
        rvBeauty.setItemAnimator(new DefaultItemAnimator());
        gridLayoutManager
                = new GridLayoutManager(this, 3);
        rvBeauty.setLayoutManager(gridLayoutManager);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_blue_light);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        rvBeauty.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItemPosition = gridLayoutManager.findLastVisibleItemPosition();
                int totalItemPosition = gridLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastItemPosition >= totalItemPosition - 4 && dy > 0) {
                    if (isLoadingMore || mAdapter.getStatus() == FooterAdapter.FOOTER_TYPE_LOADING
                            || isRefresh) {
                    } else {
                        loadPage();//这里多线程也要手动控制isLoadingMore
                    }
                }
            }
        });
        refresh();
    }

    private void getData() {
        AVQuery<AVObject> avQuery = new AVQuery<>("_File");
        avQuery.limit(40);
        avQuery.skip(40 * page);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                swipeRefresh.setEnabled(true);
                if (null == e) {
                    if (isRefresh) {
                        listFile.clear();
                    }

                    for (AVObject avObject : list) {
                        listFile.add(AVFile.withAVObject(avObject));
                    }
                    if (isLoadingMore) {
                        mAdapter.setNormal();
                        mAdapter.notifyItemInserted(listFile.size() - list.size());
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                    progressBar.setVisibility(View.GONE);
                } else {
                    ToastUtils.showShort("五位数查询错误，大爆射失败！");
                }
                swipeRefresh.setRefreshing(false);
                isLoadingMore = false;
                isRefresh = false;
            }
        });

    }

    private void refresh() {
        isRefresh = true;
        isLoadingMore = false;
        page = 0;
        getData();

    }

    private void loadPage() {
        page++;
        mAdapter.setLoading();
        isLoadingMore = true;
        swipeRefresh.setEnabled(false);
        getData();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rvBeauty = (RecyclerView) findViewById(R.id.rv_beauty);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swip_refresh);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        setSupportActionBar(toolbar);
    }

    //找到数组中的最大值
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private BeautyAdapter.OnItemClickListener onItemClickListener=new BeautyAdapter.OnItemClickListener() {
        @Override
        public void onClick(View view, int position) {
            ImageActivity.gotoImageActivity(BeautyActivity.this,listFile.get(position).getUrl(),
                    listFile.get(position).getName(),view);
        }
    } ;
}
