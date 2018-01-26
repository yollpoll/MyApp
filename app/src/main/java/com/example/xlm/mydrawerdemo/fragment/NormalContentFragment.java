package com.example.xlm.mydrawerdemo.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.xlm.mydrawerdemo.retrofitService.ArticleService;
import com.example.xlm.mydrawerdemo.Activity.ChildArticleActivity;
import com.example.xlm.mydrawerdemo.Activity.ImageActivity;
import com.example.xlm.mydrawerdemo.Activity.NewThreadActivity;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.ArticleRecyclerAdapter;
import com.example.xlm.mydrawerdemo.base.BaseFragment;
import com.example.xlm.mydrawerdemo.bean.Article;
import com.example.xlm.mydrawerdemo.http.Httptools;
import com.example.xlm.mydrawerdemo.http.Port;
import com.example.xlm.mydrawerdemo.utils.Constant;
import com.example.xlm.mydrawerdemo.utils.SpaceItemDecoration;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by xlm on 2015/11/16.
 */
public class NormalContentFragment extends BaseFragment {
    private View rootView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager, moduleLayoutManager;
    private ArticleRecyclerAdapter adapterArticle;
    public String formId = "";//板块id,默认综合1
    private String formTitle = "";//板块名
    private int page = 1;//页数
    private boolean isLoadingMore = false;//是否正在加载中
    //帖子内容
    private List<Article> data = new ArrayList<>();
    //帖子评论
    private List<String> commentContentList = new ArrayList<>();
    private Retrofit retrofit;
    private ProgressBar progressBar;


    public NormalContentFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = R.layout.fragment_article_layout;
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(layoutId, null);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mSwipRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    int spacingInPixels = 0;

    private void initData() {
        this.formId = getArguments().getString("formId");
        mSwipRefreshLayout.setRefreshing(true);
        retrofit = Httptools.getInstance().getRetrofit();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        moduleLayoutManager = new LinearLayoutManager(getActivity());
        moduleLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        adapterArticle = new ArticleRecyclerAdapter(data, getActivity());

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(adapterArticle);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if (spacingInPixels == 0) {
            spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_space);
            mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        }
        //设置刷新时颜色切换
        mSwipRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_blue_light);
        mSwipRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLinearLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    if (isLoadingMore) {
                        Toast.makeText(getActivity(), "加载中", Toast.LENGTH_SHORT).show();
                    } else {
                        loadPage();//这里多线程也要手动控制isLoadingMore
                    }
                }
            }
        });
        //点击进入串内部
        adapterArticle.setOnItemClickListener(new ArticleRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ChildArticleActivity.class);
                intent.putExtra("id", data.get(position).getId());
                intent.putExtra("title", data.get(position).getTitle());
                startActivity(intent);
            }

            @Override
            public void onImageClick(View view, int position) {
                String url = Port.getImg() + data.get(position).getImg() + data.get(position).getExt();
                ImageActivity.gotoImageActivity(getActivity(), url, data.get(position).getImg() + data.get(position).getExt(), view);
            }

            @Override
            public void onLongClick(View view, int position) {
                showMenu(position);
            }
        });
        getData(false);
    }

    //长按显示子菜单
    private void showMenu(int position) {
        ThreadMenuFragment threadMenuFragment = new ThreadMenuFragment();
        threadMenuFragment.setOnItemClickListener(onItemClickListener);
        Bundle arg = new Bundle();
        arg.putInt("position", position);
        threadMenuFragment.setArguments(arg);
        threadMenuFragment.show(getActivity().getSupportFragmentManager(), ThreadMenuFragment.REPLY);
    }

    private ThreadMenuFragment.OnItemClickListener onItemClickListener = new ThreadMenuFragment.OnItemClickListener() {
        @Override
        public void onClick(int id, int position, DialogFragment fragment) {
            fragment.dismiss();
            switch (id) {
                case R.id.tv_report:
                    NewThreadActivity.gotoReport(getActivity(), ">>No." + data.get(position).getId() + "\n");
                    break;
                case R.id.tv_reply:
                    NewThreadActivity.gotoReplyThreadActivity(getActivity(), data.get(position).getId(), "");
                    break;
                case R.id.tv_copy:
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", data.get(position).getContent());
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    ToastUtils.SnakeShowShort(mSwipRefreshLayout, "已经复制到剪贴板");
                    break;
            }
        }
    };

    public void onEventMainThread(Article obj) {
        data.add(obj);
        adapterArticle.notifyDataSetChanged();
    }

    //加载下一页
    private void loadPage() {
        page++;
        getData(true);
    }

    public void refresh() {
        mSwipRefreshLayout.setRefreshing(true);
        page = 1;
        getData(false);
    }


    private void getData(final boolean isLoad) {
        ArticleService articleService = retrofit.create(ArticleService.class);
        Call<List<Article>> articleCall;
        if (formId.equals(Constant.TIME_LINE)) {
            articleCall = articleService.getTimeLine();
        } else {
            articleCall = articleService.getArticleList(page + "", formId);
        }
        articleCall.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Response<List<Article>> response, Retrofit retrofit) {
                mSwipRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                //是否是加载下一页,是就不清空
                if (!isLoad) {
                    data.clear();
                }
                data.addAll(response.body());
                adapterArticle.notifyDataSetChanged();
                if (!isLoad) {
                    mLinearLayoutManager.smoothScrollToPosition(mRecyclerView, null, 0);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                mSwipRefreshLayout.setRefreshing(false);
                ToastUtils.showShort("网络不通");
            }
        });
    }
}
