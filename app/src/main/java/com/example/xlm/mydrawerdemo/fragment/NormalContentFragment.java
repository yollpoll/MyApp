package com.example.xlm.mydrawerdemo.fragment;

import android.app.Fragment;
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

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.RecyclerAdapter;
import com.example.xlm.mydrawerdemo.base.BaseFragment;
import com.example.xlm.mydrawerdemo.bean.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xlm on 2015/11/16.
 */
public class NormalContentFragment extends BaseFragment {
    private View rootView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerAdapter mAdapter;
    //帖子内容
    private List<Article> data=new ArrayList<>();
    //帖子评论
    private List<String> commentContentList=new ArrayList<>();
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

    }
    private void initData(){
        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mAdapter=new RecyclerAdapter(data,getActivity());

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
    }
    public void onEventMainThread(Article obj){
        data.add(obj);
        mAdapter.notifyDataSetChanged();
    }
    private void getData(){
        for(int i=0;i<10;i++){
            commentContentList.add("这是评论"+i);
        }
        for(int i=0;i<10;i++){
            Article article=new Article(i+"","这是内容"+i,"昨天",commentContentList);
            data.add(article);
        }
        mAdapter.notifyDataSetChanged();
    }
}
