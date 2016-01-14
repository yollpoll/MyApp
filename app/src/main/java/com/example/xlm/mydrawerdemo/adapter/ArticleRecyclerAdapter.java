package com.example.xlm.mydrawerdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.bean.Article;
import com.example.xlm.mydrawerdemo.bean.reply;
import com.example.xlm.mydrawerdemo.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spq on 2015/11/12.
 */
public class ArticleRecyclerAdapter extends RecyclerView.Adapter<ArticleRecyclerAdapter.ViewHolder> {
    private List<Article> data;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ArticleRecyclerAdapter(List<Article> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public abstract interface OnItemClickListener {
        void onClick(View view, int position);
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
        //初始化textSwitcher
        Animation in = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        holder.comment.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(context);
                return textView;
            }
        });
        holder.comment.setInAnimation(in);
        holder.comment.setOutAnimation(out);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Article article = data.get(position);
        if (article != null) {
            Spanned contentSpanned = Html.fromHtml(article.getContent());
            holder.content.setText(contentSpanned);
            holder.time.setText(Tools.replaceTime(article.getNow()));
            holder.id.setText("No." + article.getId());
            if ("0".equals(article.getAdmin())) {
                //黑名字
                holder.sender.setTextColor(context.getResources().getColor(R.color.textGrey));
            } else if ("1".equals(article.getAdmin())) {
                //红名
                holder.sender.setTextColor(context.getResources().getColor(R.color.textRed));
            }
            holder.sender.setText(article.getUserid());
            if (article.getReplys().size() > 0) {
                List<Spanned> replySpanneds = new ArrayList<>();
                for (reply r : article.getReplys()) {
                    replySpanneds.add(Html.fromHtml(r.getContent()));
                }
                Tools.changeText(holder.comment, replySpanneds, context);
            }
            holder.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView content, id, time, sender;
        RelativeLayout item_layout;
        TextSwitcher comment;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.tv_title);
            item_layout = (RelativeLayout) itemView.findViewById(R.id.item_recyclerview);
            id = (TextView) itemView.findViewById(R.id.tv_id);
            comment = (TextSwitcher) itemView.findViewById(R.id.tv_comment);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            sender = (TextView) itemView.findViewById(R.id.tv_sender_id);
        }
    }
}
