package com.example.xlm.mydrawerdemo.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.OnItemClickListenr;
import com.example.xlm.mydrawerdemo.adapter.PicEmojiAdapter;
import com.example.xlm.mydrawerdemo.adapter.WordEmojiAdapter;
import com.example.xlm.mydrawerdemo.base.BaseDialogFragment;
import com.example.xlm.mydrawerdemo.utils.EmojiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 鹏祺 on 2017/6/15.
 */
@SuppressLint("ValidFragment")
public class ChooseEmojiDialogFragment extends BaseDialogFragment {
    private TabLayout tabEmoji;
    private ViewPager vpEmoji;
    private EmojiPageAdapter mAdapter;
    //viewpager
    private List<String> title = new ArrayList<>();
    private List<View> views = new ArrayList<>();
    //recyclerview
    private List<Integer> listPicEmoji = new ArrayList<>();
    private List<String> listWordEmoji = new ArrayList<>();
    private RecyclerView rvWordEmoji, rvPicEmoji;
    private WordEmojiAdapter wordEmojiAdapter;
    private PicEmojiAdapter picEmojiAdapter;
    private OnEmojiClickListener onEmojiClickListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_choose_emoji, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    public ChooseEmojiDialogFragment() {

    }

    private void initData() {
        title.add("颜文字");
        title.add("芦苇娘");
        //颜文字
        listWordEmoji = EmojiUtils.getWordEmojiList();
        wordEmojiAdapter = new WordEmojiAdapter(listWordEmoji, onWordEmojiItemClickListener);
        rvWordEmoji = new RecyclerView(getActivity());
        rvWordEmoji.setAdapter(wordEmojiAdapter);
        GridLayoutManager wordManager = new GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
        rvWordEmoji.setLayoutManager(wordManager);
        //芦苇娘
        listPicEmoji = EmojiUtils.getPicLwnEmojiList();
        picEmojiAdapter = new PicEmojiAdapter(listPicEmoji, onPicEmojiItemClicklistener);
        rvPicEmoji = new RecyclerView(getActivity());
        rvPicEmoji.setAdapter(picEmojiAdapter);
        GridLayoutManager picManager = new GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
        rvPicEmoji.setLayoutManager(picManager);
        //viewpager
        views.add(rvWordEmoji);
        views.add(rvPicEmoji);
        mAdapter = new EmojiPageAdapter(views, title);
        vpEmoji.setAdapter(mAdapter);
        tabEmoji.setTabMode(TabLayout.MODE_FIXED);
        tabEmoji.setupWithViewPager(vpEmoji);
    }

    private void initView(View view) {
        tabEmoji = (TabLayout) view.findViewById(R.id.tab_emoji);
        vpEmoji = (ViewPager) view.findViewById(R.id.vp_emoji);
    }

    private OnItemClickListenr onWordEmojiItemClickListener = new OnItemClickListenr() {
        @Override
        public void onItemClick(View view, int position) {
            if (null != onEmojiClickListener)
                onEmojiClickListener.onClick(listWordEmoji.get(position), 0,ChooseEmojiDialogFragment.this);
        }
    };
    private OnItemClickListenr onPicEmojiItemClicklistener = new OnItemClickListenr() {
        @Override
        public void onItemClick(View view, int position) {
            if (null != onEmojiClickListener)
                onEmojiClickListener.onClick("", listPicEmoji.get(position),ChooseEmojiDialogFragment.this);
        }
    };

    public ChooseEmojiDialogFragment(OnEmojiClickListener onEmojiClickListener) {
        this.onEmojiClickListener = onEmojiClickListener;
    }

    public interface OnEmojiClickListener {
        void onClick(String word, int id, DialogFragment fragment);
    }

    private class EmojiPageAdapter extends PagerAdapter {
        private List<View> views;
        private List<String> titles;

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);

        }

        public EmojiPageAdapter(List<View> views, List<String> titles) {
            this.titles = titles;
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }
    }

}
