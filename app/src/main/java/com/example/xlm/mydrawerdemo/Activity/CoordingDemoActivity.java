package com.example.xlm.mydrawerdemo.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.StringAdapter;
import com.example.xlm.mydrawerdemo.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by 鹏祺 on 2016/2/28.
 */
public class CoordingDemoActivity extends BaseActivity {
    private Toolbar toolbar;
    private RecyclerView reccler;
    private LinearLayoutManager linearLayoutManager;
    private StringAdapter stringAdapter;
    private List<String> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordind_demo);
        initView();
        initData();
    }
    private void initView(){
        reccler= (RecyclerView) findViewById(R.id.recycler);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    private void initData(){
        getDate();
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stringAdapter=new StringAdapter(list,this);
        reccler.setLayoutManager(linearLayoutManager);
        reccler.setAdapter(stringAdapter);
        reccler.setHasFixedSize(true);
        reccler.setItemAnimator(new DefaultItemAnimator());
    }
    private void getDate(){
        for(int i=0;i<50;i++){
            list.add(i+"");
        }
    }
}
