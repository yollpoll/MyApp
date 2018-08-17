package com.example.xlm.mydrawerdemo.Activity;

/**
 * Created by 鹏祺 on 2016/1/1.
 */

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.ChildArticleAdapter;
import com.example.xlm.mydrawerdemo.adapter.FooterAdapter;
import com.example.xlm.mydrawerdemo.base.BaseSwipeActivity;
import com.example.xlm.mydrawerdemo.base.MyApplication;
import com.example.xlm.mydrawerdemo.bean.ChildArticle;
import com.example.xlm.mydrawerdemo.bean.CollectionBean;
import com.example.xlm.mydrawerdemo.bean.Reply;
import com.example.xlm.mydrawerdemo.fragment.ThreadMenuFragment;
import com.example.xlm.mydrawerdemo.http.Httptools;
import com.example.xlm.mydrawerdemo.http.Port;
import com.example.xlm.mydrawerdemo.retrofitService.ChildArticleService;
import com.example.xlm.mydrawerdemo.retrofitService.CollectionService;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by xlm on 2016/1/12.
 */
public class ChildArticleActivity extends BaseSwipeActivity implements View.OnClickListener {
    private String articleId, title;
    private TextView tvHeadTitle;
    private Retrofit retrofit;
    private List<Reply> data = new ArrayList<>();
    private SwipeRefreshLayout swipChildArticle;
    private RecyclerView recyclerChildArticle;
    private LinearLayoutManager linearLayoutManager;
    private ChildArticleAdapter adapter;
    private Boolean isLoadingMore = false, isRefresh = false;
    private Toolbar toolbarHead;
    private int page = 1;
    private boolean isCollected;
    private List<String> ids;
    private ChildArticle childArticle;
    private ContentLoadingProgressBar progressBar;

    public static void gotoChildArticleActivity(Context context, String id, @Nullable String title) {
        Intent intent = new Intent(context, ChildArticleActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NewThreadActivity.REQUEST_REPLY:
                if (resultCode == RESULT_OK) {
                    refresh();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childarticle);
        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_child_article, menu);
        return true;
    }

    CollectionService collectionService;

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_collect:
                isCollected = !isCollected;
                if (isCollected) {
                    Call<String> callCollect = collectionService.addCollection(MyApplication.getInstance().getUuId(), articleId);
                    callCollect.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Response<String> response, Retrofit retrofit) {
                            if (null != response) {
                                ToastUtils.SnakeShowShort(swipChildArticle, response.body());
                                if (!"该主题不存在".equals(response.body())) {
                                    item.setIcon(R.mipmap.icon_collect);
                                    ids.add(articleId);
                                    CollectionBean.saveIds(ids);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            ToastUtils.SnakeShowShort(swipChildArticle, "订阅失败");
                        }
                    });
                } else {
                    Call<String> callDelCollect = collectionService.delCollection(MyApplication.getInstance().getUuId(), articleId);
                    callDelCollect.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Response<String> response, Retrofit retrofit) {
                            if (null != response) {
                                item.setIcon(R.mipmap.icon_discollect);
                                ToastUtils.SnakeShowShort(swipChildArticle, response.body());
                                Iterator<String> iterator = ids.iterator();
                                while (iterator.hasNext()) {
                                    if (iterator.next().equals(articleId))
                                        iterator.remove();
                                }
                                CollectionBean.saveIds(ids);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }
                    });
                }
                return true;
            case R.id.action_reply:
                NewThreadActivity.gotoReplyThreadActivity(ChildArticleActivity.this, articleId, "");
                return true;
            case R.id.action_page:
                choosePage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        initCollectIds(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    private void initView() {
        tvHeadTitle = (TextView) findViewById(R.id.tv_head_title);
        swipChildArticle = (SwipeRefreshLayout) findViewById(R.id.swip_childarticle);
        recyclerChildArticle = (RecyclerView) findViewById(R.id.recyclerview_childarticle);
        toolbarHead = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);

        setSupportActionBar(toolbarHead);
    }

    private void initCollectIds(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_collect);
        ids = CollectionBean.getIds(MyApplication.getInstance().getUuId());
        for (String id : ids) {
            if (id.equals(articleId)) {
                isCollected = true;
                item.setIcon(R.mipmap.icon_collect);
            }
        }
    }

    private void initData() {
        //获取上个页面传入数据
        articleId = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbarHead.setTitle(title);
//        toolbarHead.setNavigationIcon(R.mipmap.icon_back);
        toolbarHead.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChildArticleActivity.this.finish();
            }
        });
        //初始化retrofit
        retrofit = Httptools.getInstance().getRetrofit();
        collectionService = retrofit.create(CollectionService.class);
        //初始化list
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new ChildArticleAdapter(data);
        adapter.setOnItemClickListener(new ChildArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onImageClick(View view, int position) {
                String url = Port.getImg() + data.get(position).getImg() + data.get(position).getExt();
//                Intent intent=new Intent(ChildArticleActivity.this,ImageActivity.class);
//                intent.putExtra("url",url);
//                ChildArticleActivity.this.startActivity(intent);
                ImageActivity.gotoImageActivity(ChildArticleActivity.this, url, data.get(position).getImg() + data.get(position).getExt(), view);
            }

            @Override
            public void onLongClick(View view, int position) {
                showMenu(position);
            }
        });
        recyclerChildArticle.setLayoutManager(linearLayoutManager);
        recyclerChildArticle.setAdapter(adapter);
        recyclerChildArticle.setHasFixedSize(false);
        swipChildArticle.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_blue_light);
        swipChildArticle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        recyclerChildArticle.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                int totalItemPosition = linearLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastItemPosition >= totalItemPosition - 4 && dy > 0) {
                    if (isLoadingMore || adapter.getStatus() == FooterAdapter.FOOTER_TYPE_LOADING) {
                    } else {
                        loadPage();//这里多线程也要手动控制isLoadingMore
                    }
                }
            }
        });
        refresh();
    }

    //长按显示子菜单
    private void showMenu(int position) {
        ThreadMenuFragment threadMenuFragment = new ThreadMenuFragment();
        threadMenuFragment.setOnItemClickListener(onItemClickListener);
        Bundle arg = new Bundle();
        arg.putInt("position", position);
        threadMenuFragment.setArguments(arg);
        threadMenuFragment.show(getSupportFragmentManager(), ThreadMenuFragment.REPLY);
    }

    private ThreadMenuFragment.OnItemClickListener onItemClickListener = new ThreadMenuFragment.OnItemClickListener() {
        @Override
        public void onClick(int id, int position, DialogFragment fragment) {
            fragment.dismiss();
            switch (id) {
                case R.id.tv_report:
                    NewThreadActivity.gotoReport(ChildArticleActivity.this, ">>No." + data.get(position).getId() + "\n");
                    break;
                case R.id.tv_reply:
                    NewThreadActivity.gotoReplyThreadActivity(ChildArticleActivity.this, articleId, ">>No." + data.get(position).getId() + "\n");
                    break;
                case R.id.tv_copy:
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", data.get(position).getContent());
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    ToastUtils.SnakeShowShort(swipChildArticle, "已经复制到剪贴板");
                    break;
            }
        }
    };

    /*
    访问网络
     */
    private void getData() {
        ChildArticleService childArticleService = retrofit.create(ChildArticleService.class);
        Call<ChildArticle> call = childArticleService.getArticleList(page, articleId);
        Log.d("spq", "articleId" + articleId + "   page" + page);
        call.enqueue(new Callback<ChildArticle>() {
            @Override
            public void onResponse(Response<ChildArticle> response, Retrofit retrofit) {
                childArticle = response.body();
                getSupportActionBar().setTitle(response.body().getTitle());
                                            if (isRefresh && page == 1) {
                    //在replay列表中加入本串的信息，作为第一个数据
                    data.clear();
                    ChildArticle temp = response.body();
                    Reply head = new Reply();
                    head.setId(temp.getId());
                    head.setImg(temp.getImg());
                    head.setNow(temp.getNow());
                    head.setUserid(temp.getUserid());
                    head.setName(temp.getName());
                    head.setEmail(temp.getEmail());
                    head.setTitle(temp.getTitle());
                    head.setContent(temp.getContent());
                    head.setSage(temp.getSage());
                    head.setAdmin(temp.getAdmin());
                    head.setExt(temp.getExt());
                    data.add(head);
                    data.addAll(response.body().getReplies());
//                        adapter.addAll(data);
                    adapter.notifyDataSetChanged();
                    swipChildArticle.setRefreshing(false);
                }
                if (isLoadingMore) {
                    adapter.setNormal();
                    //加载下一页不用和刷新一样处理
                    if (data.size() >= 2 && response.body().getReplies().size() >= 1) {
                        if (data.get(1).getId().equals(response.body().getReplies().get(0).getId())) {
                            //过滤掉重复的数据防止重复加载最后一页
                            return;
                        }
                    }
                    if (response.body().getReplies().size() <= 0) {
                        page--;
                        Log.d("spq", "pageNo>>>>>>>>>" + page);
                    }
                    data.addAll(response.body().getReplies());
                    adapter.notifyItemRangeInserted(data.size() - response.body().getReplies().size(), data.size());
                }
                if ((!isLoadingMore) && (!isRefresh)) {
                    progressBar.setVisibility(View.GONE);
                    data.clear();
                    data.addAll(response.body().getReplies());
                    adapter.notifyDataSetChanged();
                }
                isLoadingMore = false;
                isRefresh = false;
            }

            @Override
            public void onFailure(Throwable throwable) {
                adapter.setNormal();
                progressBar.setVisibility(View.GONE);
                swipChildArticle.setRefreshing(false);
                isRefresh = false;
                isLoadingMore = false;
            }
        });
    }


    /**
     * 跳轉到具體一頁,page参数全局已经赋值
     */
    private void gotoPage() {
        progressBar.setVisibility(View.VISIBLE);
        isRefresh = false;
        isLoadingMore = false;
        getData();
    }

    /*
    刷新数据
     */
    private void refresh() {
        isRefresh = true;
        swipChildArticle.setRefreshing(true);
        page = 1;
        getData();
    }

    /*
    加载下一页
     */
    private void loadPage() {
        adapter.setLoading();
        isLoadingMore = true;
        page++;
        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_btn_left:
                scrollToFinishActivity();
                break;
        }
    }


    private void choosePage() {
        if (null == childArticle)
            return;
        final int allPage = (int) Math.ceil(((double) childArticle.getReplyCount() + 1) / 20);//向上取整
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_choose_page);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        dialog.show();
        final TextView tvChoosePage = (TextView) dialog.findViewById(R.id.tv_page);
        tvChoosePage.setText("第" + (page) + "页,共" + allPage + "页");
        SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.seek_page);
        seekBar.setMax(allPage - 1);
        seekBar.setProgress((page - 1));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                page = progress + 1;
                tvChoosePage.setText("第" + (progress + 1) + "页,共" + (allPage) + "页");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                gotoPage();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
