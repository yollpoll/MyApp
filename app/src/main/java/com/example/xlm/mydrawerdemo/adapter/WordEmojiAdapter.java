package com.example.xlm.mydrawerdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.R;

import java.util.List;

/**
 * Created by 鹏祺 on 2017/6/15.
 */

public class WordEmojiAdapter extends RecyclerView.Adapter<WordEmojiAdapter.ViewHolder> {
    private List<String> list;
    private OnItemClickListenr onItemClickListenr;

    public WordEmojiAdapter(List<String> list, OnItemClickListenr onItemClickListenr) {
        this.list = list;
        this.onItemClickListenr = onItemClickListenr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word_emoji, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String item = list.get(position);
        holder.tvWordEomji.setText(item);
        if (null != onItemClickListenr) {
            holder.tvWordEomji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListenr.onItemClick(v, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvWordEomji;

        public ViewHolder(View itemView) {
            super(itemView);
            tvWordEomji = (TextView) itemView.findViewById(R.id.tv_word_emoji);
        }
    }
}
