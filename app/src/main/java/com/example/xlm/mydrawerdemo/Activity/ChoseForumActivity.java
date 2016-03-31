package com.example.xlm.mydrawerdemo.Activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.API.FormListService;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.ChoseForumAdapater;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.bean.ChildForm;
import com.example.xlm.mydrawerdemo.bean.Form;
import com.example.xlm.mydrawerdemo.http.Httptools;
import com.example.xlm.mydrawerdemo.utils.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by 鹏祺 on 2016/3/31.
 */
public class ChoseForumActivity extends BaseActivity {
    private RecyclerView recyclerForum;
    private LinearLayoutManager linearLayoutManager;
    private Retrofit retrofit;
    private List<ChildForm> listForums=new ArrayList<>();
    private ChoseForumAdapater adapater;
    private Toolbar toolbar;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_forum);
        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView(){
        recyclerForum= (RecyclerView) findViewById(R.id.recycler_forum);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        tvTitle= (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("板块设置");
        initToolbar();
    }
    private void initData(){
        adapater=new ChoseForumAdapater(listForums,this);
        adapater.setOnClickListener(new ChoseForumAdapater.OnClickListener() {
            @Override
            public void onItemClick(View view, int position, CheckBox checkBox) {
                if(checkBox.isChecked()){
                    checkBox.setChecked(false);
                }else {
                    checkBox.setChecked(true);
                }
            }
        });
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerForum.setLayoutManager(linearLayoutManager);
        recyclerForum.setAdapter(adapater);
        recyclerForum.setHasFixedSize(true);
        recyclerForum.setItemAnimator(new DefaultItemAnimator());
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_space);
        recyclerForum.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        retrofit= Httptools.getInstance().getRetrofit();
        setModuleList();
    }
    //设置板块列表
    private void setModuleList() {
        FormListService formList = retrofit.create(FormListService.class);
        Call<List<Form>> formsCall = formList.getFormList();
        formsCall.enqueue(new Callback<List<Form>>() {
            @Override
            public void onResponse(Response<List<Form>> response, Retrofit retrofit) {
                List<Form> forms = response.body();
                if (forms != null) {
                    for (int i = 0; i < forms.size(); i++) {
                        listForums.addAll(forms.get(i).getForums());
                    }
                }
                adapater.notifyItemRangeInserted(0, listForums.size());
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}
