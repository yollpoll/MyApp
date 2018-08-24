package com.example.xlm.mydrawerdemo.adapter.ViewHolder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseViewHolder;

public class NoMoreHolder extends BaseViewHolder {
    public TextView tvNoMore;
    public ProgressBar progressBar;
    public NoMoreHolder(View itemView) {
        super(itemView);
        tvNoMore= (TextView) itemView.findViewById(R.id.tv_nomore);
        progressBar= (ProgressBar) itemView.findViewById(R.id.progressBar);
    }
}
