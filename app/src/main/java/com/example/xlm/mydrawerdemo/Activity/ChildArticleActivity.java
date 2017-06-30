package com.example.xlm.mydrawerdemo.Activity;

/**
 * Created by 鹏祺 on 2016/1/1.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.API.ChildArticleService;
import com.example.xlm.mydrawerdemo.API.CollectionService;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.ChildArticleAdapter;
import com.example.xlm.mydrawerdemo.base.BaseSwipeActivity;
import com.example.xlm.mydrawerdemo.base.MyApplication;
import com.example.xlm.mydrawerdemo.bean.ChildArticle;
import com.example.xlm.mydrawerdemo.bean.CollectionBean;
import com.example.xlm.mydrawerdemo.bean.Reply;
import com.example.xlm.mydrawerdemo.http.Httptools;
import com.example.xlm.mydrawerdemo.http.Port;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;
import com.example.xlm.mydrawerdemo.utils.Tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
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
    private int page = 0;
    private boolean isCollected;
    private List<String> ids;

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
                NewThreadActivity.gotoReplyThreadActivity(ChildArticleActivity.this, articleId);
                return true;
            case R.id.action_page:

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
        toolbarHead = (Toolbar) findViewById(R.id.toolbar_head);
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
        adapter = new ChildArticleAdapter(data, this);
        adapter.setOnItemClickListener(new ChildArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onImageClick(View view, int position) {
                String url = Port.IMG_URL + data.get(position).getImg() + data.get(position).getExt();
//                Intent intent=new Intent(ChildArticleActivity.this,ImageActivity.class);
//                intent.putExtra("url",url);
//                ChildArticleActivity.this.startActivity(intent);
                ImageActivity.gotoImageActivity(ChildArticleActivity.this, url, data.get(position).getImg() + data.get(position).getExt(), view);
            }

            @Override
            public void onLongClick(View view, int position) {

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
                    if (isLoadingMore) {
                    } else {
                        loadPage();//这里多线程也要手动控制isLoadingMore
                    }
                }
            }
        });
        refresh();
    }

    /*
    访问网络
     */
    private void getData() {
        ChildArticleService childArticleService = retrofit.create(ChildArticleService.class);
        Call<ChildArticle> call = childArticleService.getArticleList(page, articleId);
        call.enqueue(new Callback<ChildArticle>() {
            @Override
            public void onResponse(Response<ChildArticle> response, Retrofit retrofit) {
                if (isRefresh) {
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
                    //加载下一页不用和刷新一样处理
                    if (data.size() >= 2 && response.body().getReplies().size() >= 1) {
                        if (data.get(1).getId().equals(response.body().getReplies().get(0).getId())) {
                            //过滤掉重复的数据防止重复加载最后一页
                            return;
                        }
                    }
                    data.addAll(response.body().getReplies());
                    adapter.notifyItemRangeInserted(data.size() - response.body().getReplies().size(), data.size());
                }
                isLoadingMore = false;
                isRefresh = false;
            }

            @Override
            public void onFailure(Throwable throwable) {
                isRefresh = false;
                isLoadingMore = false;
            }
        });
    }

    /*
    刷新数据
     */
    private void refresh() {
        isRefresh = true;
        page = 0;
        getData();
    }

    /*
    加载下一页
     */
    private void loadPage() {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
