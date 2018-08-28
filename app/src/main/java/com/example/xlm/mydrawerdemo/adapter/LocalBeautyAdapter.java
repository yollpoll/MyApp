package com.example.xlm.mydrawerdemo.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseViewHolder;
import com.example.xlm.mydrawerdemo.bean.LocalBeautyBean;

import java.util.List;

public class LocalBeautyAdapter extends FooterAdapter<List<LocalBeautyBean>, BaseViewHolder> {
    private List<LocalBeautyBean> list;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public LocalBeautyAdapter(List<LocalBeautyBean> localBeautyBeans,
                              OnItemClickListener onItemClickListener) {
        super(localBeautyBeans);
        this.onItemClickListener = onItemClickListener;
        this.list = localBeautyBeans;
    }

    @Override
    protected void onBindContentViewHolder(BaseViewHolder holder, final int position) {
        LocalBeautyBean item = list.get(position);
        LocalBeautyHolder localBeautyHolder = (LocalBeautyHolder) holder;
        localBeautyHolder.ivBeauty.setImageBitmap(item.getBitmap());
        localBeautyHolder.tvBeauty.setText(item.getName());
        localBeautyHolder.ivBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position,v);
            }
        });
//        ViewGroup.LayoutParams params = ((LocalBeautyHolder) holder).cardRoot.getLayoutParams();
//        params.width = (ScreentUtils.getScreenWidth(context) / 3)-Tools.calculateDpToPx(20,context);
////        params.height = params.width + Tools.calculateDpToPx(20, context);
//        ((LocalBeautyHolder) holder).cardRoot.setLayoutParams(params);
    }

    @Override
    protected void onBindFooterViewHolder(BaseViewHolder holder, int position) {

    }

    @Override
    protected BaseViewHolder onCreateContentViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_local_beauty, parent, false);
        return new LocalBeautyHolder(view);
    }

    @Override
    protected BaseViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_adapter, parent, false);
        return new BaseViewHolder(view);
    }

    private static class LocalBeautyHolder extends BaseViewHolder {
        public ImageView ivBeauty;
        public TextView tvBeauty;
        LinearLayout llRoot;
        CardView cardRoot;

        public LocalBeautyHolder(View itemView) {
            super(itemView);
            ivBeauty = (ImageView) itemView.findViewById(R.id.iv_beauty);
            tvBeauty = (TextView) itemView.findViewById(R.id.tv_beauty_name);
            llRoot= (LinearLayout) itemView.findViewById(R.id.ll_root);
            cardRoot= (CardView) itemView.findViewById(R.id.card_root);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position,View view);
    }
}

