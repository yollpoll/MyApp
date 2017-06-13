package com.example.xlm.mydrawerdemo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.example.xlm.mydrawerdemo.API.FormListService;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.ChooseTagAdapter;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.bean.ChildForm;
import com.example.xlm.mydrawerdemo.bean.Form;
import com.example.xlm.mydrawerdemo.http.Httptools;
import com.example.xlm.mydrawerdemo.utils.SPUtiles;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by 鹏祺 on 2017/6/13.
 */

public class ChooseTagActivity extends BaseActivity {
    private RecyclerView rvTag;
    private Retrofit retrofit;
    private List<ChildForm> listForums = new ArrayList<>();
    private ChooseTagAdapter mAdapter;

    public static void gotoChooseTagActivity(Activity activity, String tagId, Pair<View, String>... pairs) {
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, pairs);
        Intent intent = new Intent(activity, ChooseTagActivity.class);
        activity.startActivity(intent, options.toBundle());
    }

    private void initView() {
        rvTag = (RecyclerView) findViewById(R.id.rv_tag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tag);
        getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        initView();
        initData();
    }

    private void initData() {
        retrofit = Httptools.getInstance().getRetrofit();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        listForums = SPUtiles.getTags();
        mAdapter = new ChooseTagAdapter(listForums, onItemClickListener);
        rvTag.setAdapter(mAdapter);
        rvTag.setLayoutManager(gridLayoutManager);
        rvTag.setItemAnimator(new DefaultItemAnimator());
    }

    private ChooseTagAdapter.OnItemClickListener onItemClickListener = new ChooseTagAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = getIntent();
            intent.putExtra("tagId", listForums.get(position).getId());
            intent.putExtra("tagName", listForums.get(position).getName());
            ChooseTagActivity.this.setResult(RESULT_OK, intent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ChooseTagActivity.this.finishAfterTransition();
            } else {
                ChooseTagActivity.this.finish();
            }
        }
    };

}
