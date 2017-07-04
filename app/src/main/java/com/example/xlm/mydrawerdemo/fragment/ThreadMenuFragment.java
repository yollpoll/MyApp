package com.example.xlm.mydrawerdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.Activity.NewThreadActivity;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseDialogFragment;

/**
 * Created by 鹏祺 on 2017/7/3.
 */

public class ThreadMenuFragment extends BaseDialogFragment {
    public static final String NEW_THREAD = "new_thread";
    public static final String REPLY = "reply";
    private TextView tvReport, tvReply, tvCopy;
    private OnItemClickListener onItemClickListener;
    private int position;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.dialog_click_child_thread, null);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void initData() {
        position = (int) getArguments().get("position");
    }

    private void initView(View view) {
        tvReport = (TextView) view.findViewById(R.id.tv_report);
        tvReply = (TextView) view.findViewById(R.id.tv_reply);
        tvCopy = (TextView) view.findViewById(R.id.tv_copy);

        tvReport.setOnClickListener(this);
        tvReply.setOnClickListener(this);
        tvCopy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (null != onItemClickListener)
            onItemClickListener.onClick(v.getId(), position, this);
    }

    public interface OnItemClickListener {
        void onClick(int id, int position, DialogFragment dialogFragment);
    }
}
