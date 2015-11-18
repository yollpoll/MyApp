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
 * Created by spq on 2015/11/18.
 */
public class StringListRecyclerViewAdapter extends RecyclerView.Adapter<StringListRecyclerViewAdapter.ViewHolder> {
    private List<String> data;
    private Context context;

    public StringListRecyclerViewAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_module,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title=data.get(position);
        if(title!=null){
            holder.title.setText(title);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerAdapter.ViewHolder{
        TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.tv_module);
        }
    }
}
