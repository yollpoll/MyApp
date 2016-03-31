package com.example.xlm.mydrawerdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.R;

import java.util.List;

/**
 * Created by 鹏祺 on 2016/2/28.
 */
public class StringAdapter extends RecyclerView.Adapter<StringAdapter.ViewHolder>  {
    private List<String> data;
    private Context context;

    public StringAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_module, null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvContent.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvContent;
        public ViewHolder(View itemView) {
            super(itemView);
            tvContent= (TextView) itemView.findViewById(R.id.tv_module);
        }
    }
}
