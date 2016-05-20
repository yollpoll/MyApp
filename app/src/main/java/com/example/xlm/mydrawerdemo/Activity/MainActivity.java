package com.example.xlm.mydrawerdemo.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.API.FormListService;
import com.example.xlm.mydrawerdemo.Dao.DaoSession;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.ArticlePagerAdapter;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.base.MyApplication;
import com.example.xlm.mydrawerdemo.bean.ChangeTitleEvent;
import com.example.xlm.mydrawerdemo.bean.ChildForm;
import com.example.xlm.mydrawerdemo.bean.Form;
import com.example.xlm.mydrawerdemo.fragment.NormalContentFragment;
import com.example.xlm.mydrawerdemo.http.Httptools;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class MainActivity extends BaseActivity {
    private DrawerLayout drawerLayout;
    private ListView listView;
    private RelativeLayout left_menu1, left_menu2, left_menu3, headBtnLeft;
    private TextView tvHeadTitle, tvLeft1, tvLeft2, tvLeft3;
    private ImageView imgMenu;
    private DaoSession daoSession;
    private ArticlePagerAdapter pagerAdapter;
    private List<Fragment> listFragment = new ArrayList<>();
    private List<String> listTitle = new ArrayList<>();
    private List<ChildForm> listTab = new ArrayList<>();
    private TabLayout tab;
    private ViewPager mViewPager;
    private boolean isFirst = true;
    private Retrofit retrofit;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        notifyTab();
    }

    private void initView() {
        //左边抽屉按钮
        left_menu1 = (RelativeLayout) findViewById(R.id.left_btn_layout1);
        left_menu2 = (RelativeLayout) findViewById(R.id.left_btn_layout2);
        left_menu3 = (RelativeLayout) findViewById(R.id.left_btn_layout3);
        tvLeft1 = (TextView) findViewById(R.id.tv_btn1);
        tvLeft2 = (TextView) findViewById(R.id.tv_btn2);
        tvLeft3 = (TextView) findViewById(R.id.tv_btn3);
        tvLeft1.setText("板块设置");

        left_menu1.setOnClickListener(this);
        left_menu2.setOnClickListener(this);
        left_menu3.setOnClickListener(this);
        //初始化标题
        tvHeadTitle = (TextView) findViewById(R.id.tv_head_title);
        tvHeadTitle.setText("广场");
        imgMenu = (ImageView) findViewById(R.id.img_head_btn_left);
        imgMenu.setImageResource(R.mipmap.icon_menu);
        headBtnLeft = (RelativeLayout) findViewById(R.id.head_btn_left);
        headBtnLeft.setOnClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        listView = (ListView) findViewById(R.id.left_drawer);
        tab = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_articles);
    }

    private void initData() {
        retrofit = Httptools.getInstance().getRetrofit();
        getForumTab();
    }

    private void getForumTab() {

        daoSession = MyApplication.getInstance().getDaoSession();
        Query query = daoSession.getChildFormDao().queryBuilder()
                .build();
        listTab = query.list();
        QueryBuilder.LOG_VALUES = true;
        QueryBuilder.LOG_SQL = true;
        if (listTab.size() <= 0) {
            //本地数据库中没有数据
            getDefaultTab();
        } else {
            bindTab();
        }
    }

    //默认tab页面
    private void getDefaultTab() {
        FormListService formList = retrofit.create(FormListService.class);
        Call<List<Form>> formsCall = formList.getFormList();
        formsCall.enqueue(new Callback<List<Form>>() {
            @Override
            public void onResponse(Response<List<Form>> response, Retrofit retrofit) {
                listTab = (ArrayList<ChildForm>) response.body().get(0).getForums();
                bindTab();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    //绑定页面和tablayout
    private void bindTab() {
        for (ChildForm form : listTab) {
            listTitle.add(form.getName());
            listFragment.add(new NormalContentFragment());
        }
        tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (String s : listTitle) {
            tab.addTab(tab.newTab().setText(s));
        }
        pagerAdapter = new ArticlePagerAdapter(getSupportFragmentManager(), listFragment, listTitle);
        mViewPager.setAdapter(pagerAdapter);
        tab.setupWithViewPager(mViewPager);
    }

    private void notifyTab() {
        if (isFirst) {
            isFirst = false;
            return;
        }
        Query query = daoSession.getChildFormDao().queryBuilder().build();
        List<ChildForm> listTab = query.list();

        listTitle.clear();
        listFragment.clear();
        for (ChildForm form : listTab) {
            listTitle.add(form.getName());
            listFragment.add(new NormalContentFragment());
        }
        pagerAdapter = new ArticlePagerAdapter(getSupportFragmentManager(), listFragment, listTitle);
        mViewPager.setAdapter(pagerAdapter);
        tab.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_btn_layout1:
                Intent intent = new Intent(MainActivity.this, ChoseForumActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.left_btn_layout2:
                break;
            case R.id.left_btn_layout3:
                break;
            case R.id.head_btn_left:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            default:
                break;
        }
    }

    /*
    修改标题
     */
    public void onEventMainThread(ChangeTitleEvent event) {
        tvHeadTitle.setText(event.getTitle());
    }
}
