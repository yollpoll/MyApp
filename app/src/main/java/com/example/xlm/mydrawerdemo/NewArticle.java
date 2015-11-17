package com.example.xlm.mydrawerdemo;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.bean.Article;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by 鹏祺 on 2015/11/14.
 */
public class NewArticle extends BaseActivity {
    private Toolbar mToolbar;
    private RelativeLayout head_btn_left;
    private TextView title;
    private Button btnSubmit;
    private EditText edTitle,edContent;
    private EventBus eventBus;
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
        btnSubmit= (Button) findViewById(R.id.btn_submit);
        title= (TextView) findViewById(R.id.tv_title);
        edTitle= (EditText) findViewById(R.id.ed_title);
        edContent= (EditText) findViewById(R.id.ed_content);

        btnSubmit.setOnClickListener(this);
        head_btn_left.setOnClickListener(this);
        title.setVisibility(View.VISIBLE);
        head_btn_left.setVisibility(View.VISIBLE);
    }

    private void initData(){
        eventBus=EventBus.getDefault();
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
            case R.id.btn_submit:
                if(edTitle.getText().toString().length()<=0){
                    Toast.makeText(NewArticle.this,"请输入标题",Toast.LENGTH_SHORT).show();
                    break;
                }else if(edContent.getText().toString().length()<=5){
                    Toast.makeText(NewArticle.this,"字数最低不能低于5个子哦",Toast.LENGTH_SHORT).show();
                    break;
                }
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String time=simpleDateFormat.format(new Date());
                Article article =new Article("spq",edContent.getText().toString(),time,new ArrayList<String>());
                eventBus.post(article);
                this.finish();
                break;
            default:
                break;
        }
    }
}
