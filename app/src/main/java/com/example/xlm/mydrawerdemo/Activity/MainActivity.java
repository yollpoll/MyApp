package com.example.xlm.mydrawerdemo.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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
import com.example.xlm.mydrawerdemo.http.Port;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class MainActivity extends BaseActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView listView;
    private RelativeLayout left_menu1, left_menu2, left_menu3;
    private TextView tvLeft1, tvLeft2, tvLeft3;
    private DaoSession daoSession;
    private ArticlePagerAdapter pagerAdapter;
    private List<Fragment> listFragment = new ArrayList<>();
    private NormalContentFragment currentFragment;
    private List<String> listTitle = new ArrayList<>();
    private List<ChildForm> listTab = new ArrayList<>();
    private TabLayout tab;
    private ViewPager mViewPager;
    private boolean isFirst = true;
    private Retrofit retrofit;
    private static final int START_CHOOSEFORUM = 1;
    private ImageView imgCover;
    private Bitmap coverBitmap;
    private FloatingActionButton fbNew;
    private CoordinatorLayout cdlContent;
    private String currentTagName, currentTagId;//当前板块名字和id


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
        notifyTab();
    }

    //
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        initToolBar();
        initDrawerLayout();
        tab = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_articles);
        fbNew = (FloatingActionButton) findViewById(R.id.fb_new);
        cdlContent = (CoordinatorLayout) findViewById(R.id.cdl_content);
        detector.setOnDoubleTapListener(onDoubleTapListener);
        //使用手势监听仍然保留这个是为了保留点击时候的波纹效果
        fbNew.setOnClickListener(this);
        fbNew.setOnTouchListener(onTouchListener);
//        initFloatingActionButton();
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return detector.onTouchEvent(event);
        }
    };
    //手势事件处理
    private GestureDetector detector = new GestureDetector(new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
//            ToastUtils.SnakeShowShort(mViewPager, "显示菜单");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            if (null == currentFragment) {
//                if (listFragment.size() > 0) {
//                    currentFragment = (NormalContentFragment) listFragment.get(0);
//                } else {
//                    return false;
//                }
//            }
//            currentFragment.refresh();
            NewThreadActivity.gotoNewThreadActivity(MainActivity.this, currentTagName, currentTagId);
            return false;
        }
    });
    //双击事件
    private GestureDetector.OnDoubleTapListener onDoubleTapListener = new GestureDetector.OnDoubleTapListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (TextUtils.isEmpty(currentTagName))
                return false;
            if (TextUtils.isEmpty(currentTagId))
                return false;
            NewThreadActivity.gotoNewThreadActivity(MainActivity.this, currentTagName, currentTagId);
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (null == currentFragment) {
                if (listFragment.size() > 0) {
                    currentFragment = (NormalContentFragment) listFragment.get(0);
                } else {
                    return false;
                }
            }
            currentFragment.refresh();
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    };

    //初始化左边抽屉
    private void initDrawerLayout() {
        left_menu1 = (RelativeLayout) findViewById(R.id.left_btn_layout1);
        left_menu2 = (RelativeLayout) findViewById(R.id.left_btn_layout2);
        left_menu3 = (RelativeLayout) findViewById(R.id.left_btn_layout3);
        tvLeft1 = (TextView) findViewById(R.id.tv_btn1);
        tvLeft2 = (TextView) findViewById(R.id.tv_btn2);
        tvLeft3 = (TextView) findViewById(R.id.tv_btn3);
        tvLeft1.setText("板块");
        imgCover = (ImageView) findViewById(R.id.img_cover);
        Glide.get(this).clearMemory();
        Glide.with(this)
                .load(Port.COVER)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.mipmap.left_drawer_bg)
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imgCover.setImageBitmap(resource);
                        coverBitmap = resource;
                    }
                });

        imgCover.setOnClickListener(this);
        left_menu1.setOnClickListener(this);
        left_menu2.setOnClickListener(this);
        left_menu3.setOnClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open,
                R.string.close);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
        listView = (ListView) findViewById(R.id.left_drawer);
    }

    //初始化toolbar
    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("广场");
//        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.icon_back));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        retrofit = Httptools.getInstance().getRetrofit();
        getForumTab();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentFragment = (NormalContentFragment) listFragment.get(position);
                toolbar.setTitle(listTab.get(position).getName());
                getSupportActionBar().setTitle(listTab.get(position).getName());

                currentTagName = listTab.get(position).getName();
                currentTagId = listTab.get(position).getId();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getForumTab() {

        daoSession = MyApplication.getInstance().getDaoSession();
        Query query = daoSession.getChildFormDao().queryBuilder()
                .build();
        listTab = query.list();
        removeTimeLine();
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
                removeTimeLine();
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
            if (form.getId().equals("-1"))
                continue;
            listTitle.add(form.getName());
            NormalContentFragment normalContentFragment = new NormalContentFragment();
            Bundle bundle = new Bundle();
            bundle.putString("formId", form.getId());
            normalContentFragment.setArguments(bundle);
            listFragment.add(normalContentFragment);
        }
        tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (String s : listTitle) {
            tab.addTab(tab.newTab().setText(s));
        }
        pagerAdapter = new ArticlePagerAdapter(getSupportFragmentManager(), listFragment, listTitle);
        mViewPager.setAdapter(pagerAdapter);
        tab.setupWithViewPager(mViewPager);
        if (listTitle.size() >= 0) {
            toolbar.setTitle(listTitle.get(0));
            getSupportActionBar().setTitle(listTitle.get(0));
        }
        if (listTab.size() > 0) {
            currentTagId = listTab.get(0).getId();
            currentTagName = listTab.get(0).getName();
        } else {
            currentTagName = null;
            currentTagId = null;
        }
    }

    private void notifyTab() {
        if (isFirst) {
            isFirst = false;
            Log.d("spq", "isFirst");
            return;
        }
        Log.d("spq", "isNotFirst");
        Query query = daoSession.getChildFormDao().queryBuilder().build();
        listTab = query.list();
        removeTimeLine();
        listTitle.clear();
        listFragment.clear();
        for (ChildForm form : listTab) {
            listTitle.add(form.getName());
            NormalContentFragment normalContentFragment = new NormalContentFragment();
            Bundle bundle = new Bundle();
            bundle.putString("formId", form.getId());
            normalContentFragment.setArguments(bundle);
            listFragment.add(normalContentFragment);
        }
        pagerAdapter = new ArticlePagerAdapter(getSupportFragmentManager(), listFragment, listTitle);
        mViewPager.setAdapter(pagerAdapter);
        tab.setupWithViewPager(mViewPager);
    }

    //暂时去掉时间线功能
    private void removeTimeLine() {
        Iterator iterator = listTab.iterator();
        while (iterator.hasNext()) {
            ChildForm childForm = (ChildForm) iterator.next();
            if (childForm.getId().equals("-1")) {
                iterator.remove();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case START_CHOOSEFORUM:
                if (resultCode == RESULT_OK) {
                    notifyTab();
                }
                break;
            case NewThreadActivity.REQUEST_NEW_THREAD:
                if (resultCode != RESULT_OK)
                    break;
                if (null == currentFragment) {
                    if (listFragment.size() > 0) {
                        currentFragment = (NormalContentFragment) listFragment.get(0);
                    } else {
                        break;
                    }
                }
                currentFragment.refresh();
                break;
        }
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {                                         //如果两次按键时间间隔大于2秒，则不退出
                    ToastUtils.SnakeShowShort(mViewPager, "再按一次退出程序");
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {                                                    //两次按键小于2秒时，退出应用
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_btn_layout1:
                Intent intent = new Intent(MainActivity.this, ChoseForumActivity.class);
                MainActivity.this.startActivityForResult(intent, START_CHOOSEFORUM);
                break;
            case R.id.left_btn_layout2:
                SetActivity.gotoSetActivity(MainActivity.this);
                break;
            case R.id.left_btn_layout3:
                CollectionActivity.gitoCollectionActivity(MainActivity.this);
                break;
            case R.id.head_btn_left:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.img_cover:
                ImageActivity.gotoImageActivity(MainActivity.this, coverBitmap, imgCover);
                break;
            case R.id.fb_new:
//                ToastUtils.SnakeShowShort(cdlContent, "本肥肥还没做这个功能,你在乱点什么啦(　^ω^)");
                break;
            default:
                break;
        }
    }
}
