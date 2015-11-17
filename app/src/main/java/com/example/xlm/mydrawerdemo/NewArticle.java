package com.example.xlm.mydrawerdemo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 鹏祺 on 2015/11/14.
 */
public class NewArticle extends BaseActivity {
    private Toolbar mToolbar;
    private RelativeLayout head_btn_left;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        initView();
        initData();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
    private void initView(){
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        head_btn_left= (RelativeLayout) findViewById(R.id.head_btn_left);
        title= (TextView) findViewById(R.id.tv_title);
        head_btn_left.setOnClickListener(this);
        title.setVisibility(View.VISIBLE);
        head_btn_left.setVisibility(View.VISIBLE);
    }

    private void initData(){
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        title.setText("发表新帖");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.head_btn_left:
                this.finish();
                break;
            default:
                break;
        }
    }
}
