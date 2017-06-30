package com.example.xlm.mydrawerdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.FileListAdapter;
import com.example.xlm.mydrawerdemo.adapter.OnItemClickListenr;
import com.example.xlm.mydrawerdemo.utils.Tools;

import java.io.File;

/**
 * Created by 鹏祺 on 2017/6/30.
 */

public class FIleListFragment extends DialogFragment {
    View rootView;
    private RecyclerView rvFileList;
    private FileListAdapter mAdapter;
    private File[] files;
    private OnFileClickListener onFileClickListener;

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.horizontalMargin = Tools.calculateDpToPx(50, getActivity());
        window.setAttributes(layoutParams);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_file_list, null);
        }
        return rootView;
    }

    public FIleListFragment(File[] files, OnFileClickListener onFileClickListener) {
        this.files = files;
        this.onFileClickListener = onFileClickListener;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initView(View view) {
        rvFileList = (RecyclerView) view.findViewById(R.id.rv_file_list);
    }

    private void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new FileListAdapter(files, onItemClickListenr);
        rvFileList.setLayoutManager(layoutManager);
        rvFileList.setAdapter(mAdapter);
    }

    private OnItemClickListenr onItemClickListenr = new OnItemClickListenr() {
        @Override
        public void onItemClick(View view, int position) {
            if (null != onFileClickListener) {
                onFileClickListener.onClick(files[position]);
            }
        }
    };

    public interface OnFileClickListener {
        void onClick(File file);
    }
}
