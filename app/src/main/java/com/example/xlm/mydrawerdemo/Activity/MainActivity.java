package com.example.xlm.mydrawerdemo.Activity;

import android.content.Intent;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.bean.ChangeTitleEvent;
import com.example.xlm.mydrawerdemo.fragment.NormalContentFragment;
import com.example.xlm.mydrawerdemo.utils.DrawerTools;

public class MainActivity extends BaseActivity {
    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private RelativeLayout left_menu1,left_menu2,left_menu3;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private NormalContentFragment normalContentFragment;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        toolbar= (Toolbar) findViewById(R.id.toolbar);
    }
    private void initData(){
        //设置左边NavigationIcon按钮
        toolbar.setNavigationIcon(R.mipmap.icon_menu);
        toolbar.setTitle("广场");
        setSupportActionBar(toolbar);
        //Toorbar按钮点击事件
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
//                        swipeRefreshLayout.setRefreshing(true);
                        msg += "Click refresh";
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
        mFragmentManager=getFragmentManager();
        mFragmentTransaction=mFragmentManager.beginTransaction();
        normalContentFragment=new NormalContentFragment();
        mFragmentTransaction.replace(R.id.framelayout_fragment, normalContentFragment);
        mFragmentTransaction.commit();
        //菜单按钮点击事件
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);

        //当drawer打开时，设置一个阴影覆盖在主要内容上面
//        drawerLayout.setDrawerShadow(R.drawable.checked, GravityCompat.START);
        //关联抽屉和toolbar
        mDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.close,R.string.open){
        };
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
    /*
    修改标题
     */
    public void onEventMainThread(ChangeTitleEvent event){
        toolbar.setTitle(event.getTitle());
    }
}
