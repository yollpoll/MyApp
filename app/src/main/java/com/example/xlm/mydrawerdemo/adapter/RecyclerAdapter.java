package com.example.xlm.mydrawerdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.bean.Article;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by spq on 2015/11/12.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<Article> data;
    private Context context;

    public RecyclerAdapter(List<Article> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public List<Article> getData() {
        return data;
    }

    public void setData(List<Article> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = data.get(position);
        if (article != null) {
            holder.content.setText(article.getContent());
            holder.time.setText(article.getTime());
            holder.id.setText(article.getId());
            //TODO先用第一条评论写死
            holder.comment.setText(article.getCommentContent().get(1));
            holder.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView content, id, comment, time;
        RelativeLayout item_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.tv_title);
            item_layout = (RelativeLayout) itemView.findViewById(R.id.item_recyclerview);
            id = (TextView) itemView.findViewById(R.id.tv_id);
            comment = (TextView) itemView.findViewById(R.id.tv_comment);
            time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
