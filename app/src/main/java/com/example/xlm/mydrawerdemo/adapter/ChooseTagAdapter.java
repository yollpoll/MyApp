package com.example.xlm.mydrawerdemo.adapter;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.bean.ChildForm;

import java.util.List;

/**
 * Created by 鹏祺 on 2017/6/13.
 */

public class ChooseTagAdapter extends RecyclerView.Adapter<ChooseTagAdapter.ViewHolder> {
    private List<ChildForm> list;
    private OnItemClickListener onItemClickListener;

    public ChooseTagAdapter(List<ChildForm> list, OnItemClickListener onItemClickListener) {
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ChildForm item = list.get(position);
        holder.tvTag.setText(item.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.tvTag.setTransitionName(item.getId());
        }
        if (null != onItemClickListener)
            holder.tvTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTag;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTag = (TextView) itemView.findViewById(R.id.tv_tag);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
