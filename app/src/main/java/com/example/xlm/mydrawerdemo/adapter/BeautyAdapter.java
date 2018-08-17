package com.example.xlm.mydrawerdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avos.avoscloud.AVFile;
import com.bumptech.glide.Glide;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseViewHolder;

import java.util.List;

public class BeautyAdapter extends FooterAdapter<List<AVFile>, BaseViewHolder> {
    private List<AVFile> list;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public BeautyAdapter(List<AVFile> list,OnItemClickListener onItemClickListener) {
        super(list);
        this.onItemClickListener=onItemClickListener;
        this.list = list;
    }

    @Override
    protected void onBindContentViewHolder(BaseViewHolder holder, final int position) {
        AVFile avFile = list.get(position);
        BeautyHolder beautyHolder = ((BeautyHolder) holder);
//        RelativeLayout.LayoutParams layoutParams =
//                (RelativeLayout.LayoutParams) beautyHolder.ivBeauty.getLayoutParams();
//        float width = ScreentUtils.getScreenWidth(context) / 4 - Tools.calculateDpToPx(10, context);
//        layoutParams.width = (int) width;
//        beautyHolder.ivBeauty.setLayoutParams(layoutParams);

        Glide.with(context).load(avFile.getThumbnailUrl(true, 300, 300))
                .centerCrop()
                .crossFade()
                .into(((BeautyHolder) holder).ivBeauty);
        ((BeautyHolder) holder).ivBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(v,position);
            }
        });
    }

    @Override
    protected void onBindFooterViewHolder(BaseViewHolder holder, int position) {

    }


    @Override
    protected BeautyHolder onCreateContentViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_beauty, parent, false);
        return new BeautyHolder(view);
    }

    @Override
    protected BaseViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_adapter, parent, false);
        return new BaseViewHolder(view);
    }

    private static class BeautyHolder extends BaseViewHolder {
        public ImageView ivBeauty;

        public BeautyHolder(View itemView) {
            super(itemView);
            ivBeauty = (ImageView) itemView.findViewById(R.id.iv_beauty);
        }
    }
    public interface OnItemClickListener{
        void onClick(View view,int position);
    }
}
