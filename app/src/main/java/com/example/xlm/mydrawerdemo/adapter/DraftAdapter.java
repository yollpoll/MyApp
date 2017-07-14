package com.example.xlm.mydrawerdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.bean.Draft;

import java.util.List;

/**
 * Created by 鹏祺 on 2017/7/14.
 */

public class DraftAdapter extends RecyclerView.Adapter<DraftAdapter.ViewHolder> {
    private List<Draft> list;

    public DraftAdapter(List<Draft> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draft, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Draft item = list.get(position);
        holder.tvDate.setText(item.getDate());
        holder.tvContent.setText(item.getContent());
//        holder.imgContent.setImageBitmap(item.getPicture());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvContent;
        ImageView imgContent;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            imgContent = (ImageView) itemView.findViewById(R.id.img_content);
        }
    }
}
