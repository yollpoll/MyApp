package com.example.xlm.mydrawerdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.widget.ActionMenuPresenter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.xlm.mydrawerdemo.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle mDrawerToggle;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Toolbar toolbar;
    private RecyclerAdapter adapter;
    private List<String> data=new ArrayList<>();
    private RelativeLayout left_menu1,left_menu2,left_menu3;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        initData();
    }
    private void initView(){
        //左边抽屉按钮
        left_menu1= (RelativeLayout) findViewById(R.id.left_btn_layout1);
        left_menu2= (RelativeLayout) findViewById(R.id.left_btn_layout2);
        left_menu3= (RelativeLayout) findViewById(R.id.left_btn_layout3);
        left_menu1.setOnClickListener(this);
        left_menu2.setOnClickListener(this);
        left_menu3.setOnClickListener(this);

        drawerLayout= (DrawerLayout) findViewById(R.id.dwawer);
        listView= (ListView) findViewById(R.id.left_drawer);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    private void initData(){
        toolbar.setTitle("MyDemo");
        //设置左边NavigationIcon按钮
        toolbar.setNavigationIcon(R.mipmap.icon_menu);
        Toolbar.OnMenuItemClickListener onMenuItemClickListener=new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String msg="";
                switch (item.getItemId()){
                    case R.id.action_edit:
                        Intent intent=new Intent(MainActivity.this,NewArticle.class);
                        startActivity(intent);
                        break;
                    case R.id.action_share:
                        swipeRefreshLayout.setRefreshing(true);
//                        msg += "Click refresh";
                        refresh();
                        break;
                    case R.id.action_settings:
                        msg += "Click setting";
                        break;
                }
                if(!"".equals(msg)){
                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        };
        //菜单按钮点击事件
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);

        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        adapter=new RecyclerAdapter(data,this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //设置刷新时颜色切换
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_blue_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        //当drawer打开时，设置一个阴影覆盖在主要内容上面
//        drawerLayout.setDrawerShadow(R.drawable.checked, GravityCompat.START);
        //关联抽屉和toolbar
        mDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.close,R.string.open){
        };

        getData();
    }
    private void getData(){
        for(int i=0;i<40;i++){
            data.add("这是标题");
        }
        adapter.notifyDataSetChanged();
    }

    private void refresh(){
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this,"刷新完成",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.left_btn_layout1:
                Toast.makeText(this,"点击按钮1",Toast.LENGTH_SHORT).show();
                break;
            case R.id.left_btn_layout2:
                break;
            case R.id.left_btn_layout3:
                break;
            default:
                break;
        }
    }
}
