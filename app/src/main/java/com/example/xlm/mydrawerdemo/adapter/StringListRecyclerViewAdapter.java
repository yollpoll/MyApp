package com.example.xlm.mydrawerdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.bean.ChildForm;

import java.util.List;

/**
 * Created by spq on 2015/11/18.
 */
public class StringListRecyclerViewAdapter extends RecyclerView.Adapter<StringListRecyclerViewAdapter.ViewHolder> {
    private List<ChildForm> data;
    private Context context;
    private onItemClickListener onItemClickListener;

    public StringListRecyclerViewAdapter(List<ChildForm> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setOnItemClickListener(StringListRecyclerViewAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    public List<ChildForm> getData() {
        return data;
    }

    public void setData(List<ChildForm> data) {
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String title=data.get(position).getName();
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
            }
        });
        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onItemLongClick(v,position);
                return false;
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

    public static class ViewHolder extends ArticleRecyclerAdapter.ViewHolder{
        TextView title;
        RelativeLayout item;
        public ViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.tv_module);
            item= (RelativeLayout) itemView.findViewById(R.id.item);
        }
    }
}
