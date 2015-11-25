package com.example.xlm.mydrawerdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.bean.Article;
import com.example.xlm.mydrawerdemo.bean.reply;
import com.example.xlm.mydrawerdemo.utils.Tools;
import com.example.xlm.mydrawerdemo.view.SecretTextView;

import java.util.ArrayList;
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
            Spanned contentSpanned = Html.fromHtml(article.getContent());
            holder.content.setText(contentSpanned);
            holder.time.setText(article.getNow());
            holder.id.setText(article.getId());
            //TODO先写死
            if (article.getReplys().size() > 0) {
//                Spanned childContentSpanned = Html.fromHtml(article.getReplys().get(0).getContent());
//                holder.comment.setText(childContentSpanned);
                List<Spanned> replySpanneds=new ArrayList<>();
                for(reply r:article.getReplys()){
                    replySpanneds.add(Html.fromHtml(r.getContent()));
                }
                Tools.changeText(holder.comment,replySpanneds);
            }
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
        TextView content, id, time;
        SecretTextView comment;
        RelativeLayout item_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.tv_title);
            item_layout = (RelativeLayout) itemView.findViewById(R.id.item_recyclerview);
            id = (TextView) itemView.findViewById(R.id.tv_id);
            comment = (SecretTextView) itemView.findViewById(R.id.tv_comment);
            time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
