package com.example.xlm.mydrawerdemo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

/**
 * Created by 鹏祺 on 2015/11/14.
 */
public class NewArticle extends BaseActivity {
    private Toolbar mToolbar;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_new);
        initView();
        initData();
        Toast.makeText(this,"发帖",Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private void initView(){
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("发帖");

    }
    private void initData(){

    }
}
