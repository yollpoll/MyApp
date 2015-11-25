package com.example.xlm.mydrawerdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.bean.ChildFourm;
import com.example.xlm.mydrawerdemo.bean.Form;

import java.util.List;

/**
 * Created by spq on 2015/11/18.
 */
public class StringListRecyclerViewAdapter extends RecyclerView.Adapter<StringListRecyclerViewAdapter.ViewHolder> {
    private List<ChildFourm> data;
    private Context context;

    public StringListRecyclerViewAdapter(List<ChildFourm> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public List<ChildFourm> getData() {
        return data;
    }

    public void setData(List<ChildFourm> data) {
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
        String title=data.get(position).getName();
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
        RelativeLayout item;
        public ViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.tv_module);
            item= (RelativeLayout) itemView.findViewById(R.id.item);
        }
    }
}
