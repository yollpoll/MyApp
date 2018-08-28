package com.example.xlm.mydrawerdemo.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.BeautyAdapter;
import com.example.xlm.mydrawerdemo.adapter.FooterAdapter;
import com.example.xlm.mydrawerdemo.adapter.LocalBeautyAdapter;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.bean.LocalBeautyBean;
import com.example.xlm.mydrawerdemo.utils.Constant;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class BeautyActivity extends BaseActivity {
    private final static int LOCAL_BEAUTY = 1;
    private final static int NET_BEAUTY = 2;

    private int beautySource = LOCAL_BEAUTY;
    private boolean isLoadingMore, isRefresh;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private int page = 0;
    private SwipeRefreshLayout swipeRefresh;


    //五位数
    private RecyclerView rvBeauty;
    private BeautyAdapter mAdapter;
    private GridLayoutManager gridLayoutManager;
    private List<AVFile> listFile = new ArrayList<>();

    //本地
    private RecyclerView rvLocalBeauty;
    private LocalBeautyAdapter localBeautyAdapter;
    private List<LocalBeautyBean> listLocal = new ArrayList<>();
    private GridLayoutManager localLayoutManager;
    private File[] listLocalFile;

    //filter
    private LinearLayout llFilter;
    private TextView tvLocal, tvSexy;

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
        swipeRefresh.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_blue_light);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        initNetBeauty();
        initLocalBeauty();
        refresh();
    }

    private void getData() {
        switch (beautySource) {
            case NET_BEAUTY:
                getNetData();
                break;
            case LOCAL_BEAUTY:
                getLocalData();
                break;
        }

    }

    private void getNetData() {
        AVQuery<AVObject> avQuery = new AVQuery<>("_File");
        avQuery.limit(40);
        avQuery.skip(40 * page);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                swipeRefresh.setEnabled(true);
                mAdapter.setNormal();
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

    private void getLocalData() {
        File dir = new File(Environment.getExternalStorageDirectory()
                + Constant.SD_CACHE_DIR
                + Constant.IMG_BEAUTY);

        listLocalFile = dir.listFiles();
        Observable.create(new Observable.OnSubscribe<List<LocalBeautyBean>>() {
            @Override
            public void call(Subscriber<? super List<LocalBeautyBean>> subscriber) {
                List<LocalBeautyBean> resultList = new ArrayList<>();
                for (File file : listLocalFile) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    String name = file.getName();
                    LocalBeautyBean beautyBean = new LocalBeautyBean();
                    beautyBean.setBitmap(bitmap);
                    beautyBean.setName(name);
                    resultList.add(beautyBean);
                }
                subscriber.onNext(resultList);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<LocalBeautyBean>>() {
                    @Override
                    public void call(List<LocalBeautyBean> list) {
                        if (isRefresh) {
                            listLocal.clear();
                        }
                        listLocal.addAll(list);
                        progressBar.setVisibility(View.GONE);
                        localBeautyAdapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                        isLoadingMore = false;
                        isRefresh = false;
                    }
                });
    }

    private void initNetBeauty() {
        mAdapter = new BeautyAdapter(listFile, onItemClickListener);
        rvBeauty.setAdapter(mAdapter);
        rvBeauty.setItemAnimator(new DefaultItemAnimator());
        gridLayoutManager
                = new GridLayoutManager(this, 3);
        rvBeauty.setLayoutManager(gridLayoutManager);
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
                    if (isLoadingMore
                            || isRefresh
                            || mAdapter.getStatus() == FooterAdapter.FOOTER_TYPE_LOADING
                            || mAdapter.getStatus() == FooterAdapter.FOOTER_TYPE_NOMORE) {
                    } else {
                        loadPage();//这里多线程也要手动控制isLoadingMore
                    }
                }
            }
        });
    }

    private void initLocalBeauty() {
        localBeautyAdapter = new LocalBeautyAdapter(listLocal, onLocalItemClickListener);
        localLayoutManager = new GridLayoutManager(this, 2);
        rvLocalBeauty.setAdapter(localBeautyAdapter);
        rvLocalBeauty.setLayoutManager(localLayoutManager);
        rvLocalBeauty.setItemAnimator(new DefaultItemAnimator());
//        rvLocalBeauty.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int lastItemPosition = gridLayoutManager.findLastVisibleItemPosition();
//                int totalItemPosition = gridLayoutManager.getItemCount();
//                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
//                // dy>0 表示向下滑动
//                if (lastItemPosition >= totalItemPosition - 4 && dy > 0) {
//                    if (isLoadingMore
//                            || isRefresh
//                            || localBeautyAdapter.getStatus() == FooterAdapter.FOOTER_TYPE_LOADING
//                            || localBeautyAdapter.getStatus() == FooterAdapter.FOOTER_TYPE_NOMORE) {
//                    } else {
//                        loadPage();//这里多线程也要手动控制isLoadingMore
//                    }
//                }
//            }
//        });

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
        llFilter = (LinearLayout) findViewById(R.id.ll_filter);
        tvLocal = (TextView) findViewById(R.id.tv_beauty);
        tvSexy = (TextView) findViewById(R.id.tv_sexy);
        rvLocalBeauty = (RecyclerView) findViewById(R.id.rv_local_beauty);

        llFilter.setOnClickListener(this);
        tvLocal.setOnClickListener(this);
        tvSexy.setOnClickListener(this);
        setSupportActionBar(toolbar);
    }


    private void changeSource(int source) {
        beautySource = source;
        switch (source) {
            case NET_BEAUTY:
                //切换到五位数
                page = 0;
                isRefresh = false;
                isLoadingMore = false;
                rvLocalBeauty.setVisibility(View.GONE);
                rvBeauty.setVisibility(View.VISIBLE);
                refresh();
                break;
            case LOCAL_BEAUTY:
                //切换到本地
                page = 0;
                isRefresh = false;
                isLoadingMore = false;
                rvBeauty.setVisibility(View.GONE);
                rvLocalBeauty.setVisibility(View.VISIBLE);
                refresh();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_sexy:
                showWarning();
//                changeSource(NET_BEAUTY);
                break;
            case R.id.tv_beauty:
                changeSource(LOCAL_BEAUTY);
                break;
        }
    }
    private void showWarning(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("警告！大爆射")
                .setMessage("这些祭品中含有大量五位数，自己大爆射就可以了，请勿传播造成不良影响。如果诸位肥肥发现这个问题请通知我，我会第一时间删除")
                .setNegativeButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeSource(NET_BEAUTY);
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private BeautyAdapter.OnItemClickListener onItemClickListener = new BeautyAdapter.OnItemClickListener() {
        @Override
        public void onClick(View view, int position) {
            ImageActivity.gotoImageActivity(BeautyActivity.this, listFile.get(position).getUrl(),
                    listFile.get(position).getName(), view);
        }
    };
    private LocalBeautyAdapter.OnItemClickListener onLocalItemClickListener = new LocalBeautyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {
            ImageActivity.gotoImageActivity(BeautyActivity.this, listLocal.get(position).getBitmap(),
                    view);
        }
    };
}
