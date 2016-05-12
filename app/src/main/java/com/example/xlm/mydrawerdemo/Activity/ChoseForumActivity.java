package com.example.xlm.mydrawerdemo.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.API.FormListService;
import com.example.xlm.mydrawerdemo.Dao.ChildFormDao;
import com.example.xlm.mydrawerdemo.Dao.DaoSession;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.ChoseForumAdapater;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.base.MyApplication;
import com.example.xlm.mydrawerdemo.bean.ChildForm;
import com.example.xlm.mydrawerdemo.bean.Form;
import com.example.xlm.mydrawerdemo.http.Httptools;
import com.example.xlm.mydrawerdemo.utils.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
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
    private List<ChildForm> listForums = new ArrayList<>();
    private List<ChildForm> listChecked = new ArrayList<>();
    private ChoseForumAdapater adapater;
    private Toolbar toolbar;
    private TextView tvTitle;
    private DaoSession daoSession;
    private ChildFormDao childFormDao;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_forum);
        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_chose_forum, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_confirm) {
            submit();
        }
        return super.onOptionsItemSelected(item);
    }


    private void submit() {
        addChildForum();
        this.finish();
    }

    private void initToolbar() {
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

    private void initView() {
        recyclerForum = (RecyclerView) findViewById(R.id.recycler_forum);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("板块设置");
        initToolbar();
    }

    private void initData() {
        //初始化dao 和session
        daoSession = MyApplication.getInstance().getDaoSession();
        childFormDao = daoSession.getChildFormDao();

        adapater = new ChoseForumAdapater(listForums, this);
        adapater.setOnClickListener(onClickListener);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerForum.setLayoutManager(linearLayoutManager);
        recyclerForum.setAdapter(adapater);
        recyclerForum.setHasFixedSize(true);
        recyclerForum.setItemAnimator(new DefaultItemAnimator());
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_space);
        recyclerForum.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        retrofit = Httptools.getInstance().getRetrofit();
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
                for (ChildForm c : getLocalChoose()) {
                    for (ChildForm c2 : listForums) {
                        if (c.getId().equals(c2.getId())) {
                            c2.setChecked(true);
                        }
                    }
                }
                adapater.notifyItemRangeInserted(0, listForums.size());
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    /*
    获取存在本地的版块选择
     */
    private List<ChildForm> getLocalChoose() {
        Query query = daoSession.getChildFormDao().queryBuilder().build();
        List<ChildForm> result = query.list();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        return result;
    }

    //添加到数据库
    private void addChildForum() {
        childFormDao.deleteAll();
        for (ChildForm form : listChecked) {
            childFormDao.insert(form);
        }
    }

    //点击监听
    ChoseForumAdapater.OnClickListener onClickListener = new ChoseForumAdapater.OnClickListener() {
        @Override
        public void onItemClick(View view, int position, CheckBox checkBox) {
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
            } else {
                checkBox.setChecked(true);
            }
        }

        @Override
        public void onChcked(ChildForm childForm, boolean isChecked) {
            if (isChecked) {
                listChecked.add(childForm);
            } else {
                listChecked.remove(childForm);
            }
        }
    };
}
