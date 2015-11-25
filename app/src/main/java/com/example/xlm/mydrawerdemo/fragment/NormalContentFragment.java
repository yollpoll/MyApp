package com.example.xlm.mydrawerdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.xlm.mydrawerdemo.API.ArticleService;
import com.example.xlm.mydrawerdemo.API.FormListService;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.RecyclerAdapter;
import com.example.xlm.mydrawerdemo.adapter.StringListRecyclerViewAdapter;
import com.example.xlm.mydrawerdemo.base.BaseFragment;
import com.example.xlm.mydrawerdemo.bean.Article;
import com.example.xlm.mydrawerdemo.bean.ChildFourm;
import com.example.xlm.mydrawerdemo.bean.Form;
import com.example.xlm.mydrawerdemo.http.Httptools;
import com.example.xlm.mydrawerdemo.http.Port;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by xlm on 2015/11/16.
 */
public class NormalContentFragment extends BaseFragment {
    private View rootView;
    private RecyclerView mRecyclerView,module;
    private SwipeRefreshLayout mSwipRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager,moduleLayoutManager;
    private RecyclerAdapter mAdapter;
    //右侧模块adapter
    private StringListRecyclerViewAdapter moduleAdaprer;
    //帖子内容
    private List<Article> data=new ArrayList<>();
    //右侧板块名
    private List<ChildFourm> modules=new ArrayList<>();
    //帖子评论
    private List<String> commentContentList=new ArrayList<>();
    private  Retrofit retrofit;
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
    private void initView(View view){
        mRecyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
        mSwipRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        module= (RecyclerView) view.findViewById(R.id.recycler_module);


    }
    private void initData(){
        retrofit=Httptools.getInstance().getRetrofit();
        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        moduleLayoutManager=new LinearLayoutManager(getActivity());
        moduleLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mAdapter=new RecyclerAdapter(data,getActivity());
        moduleAdaprer=new StringListRecyclerViewAdapter(modules,getActivity());

        module.setAdapter(moduleAdaprer);
        module.setLayoutManager(moduleLayoutManager);
        module.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置刷新时颜色切换
        mSwipRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_blue_light);
        mSwipRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
//        getData();
        setModuleList();
    }
    public void onEventMainThread(Article obj){
        data.add(obj);
        mAdapter.notifyDataSetChanged();
    }
    private void getData(){
        ArticleService articleService=retrofit.create(ArticleService.class);
        Call<List<Article>> articleCall=articleService.getArticleList("1",modules.get(0).getId());
        articleCall.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Response<List<Article>> response, Retrofit retrofit) {
                data.addAll(response.body());
                mAdapter.notifyDataSetChanged();
                Log.i("spq","size"+data.size());
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(getActivity(),"网络不通",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //设置板块列表
    private void setModuleList(){
        FormListService formList=retrofit.create(FormListService.class);
        Call<List<Form>> formsCall=formList.getFormList();
        formsCall.enqueue(new Callback<List<Form>>() {
            @Override
            public void onResponse(Response<List<Form>> response, Retrofit retrofit) {
                List<Form> forms=response.body();
                if(forms!=null){
                    for(int i=0;i<forms.size();i++){
                        modules.addAll(forms.get(i).getForums());
                    }
                }
                moduleAdaprer.notifyDataSetChanged();
                //设置正面内容
                getData();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}
