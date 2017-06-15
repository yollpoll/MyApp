package com.example.xlm.mydrawerdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.xlm.mydrawerdemo.R;

import java.util.List;

/**
 * Created by 鹏祺 on 2017/6/15.
 */

public class PicEmojiAdapter extends RecyclerView.Adapter<PicEmojiAdapter.ViewHolder> {
    private List<Integer> list;
    private OnItemClickListenr onItemClickListenr;

    public PicEmojiAdapter(List<Integer> list, OnItemClickListenr onItemClickListenr) {
        this.list = list;
        this.onItemClickListenr = onItemClickListenr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pic_emoji, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        int item = list.get(position);
        holder.imgPicEmoji.setImageResource(item);
        if (null != onItemClickListenr) {
            holder.imgPicEmoji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListenr.onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPicEmoji;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPicEmoji = (ImageView) itemView.findViewById(R.id.img_pic_emoji);
        }
    }
}
