package com.example.xlm.mydrawerdemo.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.MyApplication;
import com.example.xlm.mydrawerdemo.bean.ChildArticle;
import com.example.xlm.mydrawerdemo.bean.Reply;
import com.example.xlm.mydrawerdemo.http.Port;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;
import com.example.xlm.mydrawerdemo.utils.Tools;
import com.example.xlm.mydrawerdemo.utils.TransFormContent;
import com.example.xlm.mydrawerdemo.view.ReplyDialog;

import java.util.List;

/**
 * Created by 鹏祺 on 2016/1/16.
 */
public class ChildArticleAdapter extends RecyclerView.Adapter<ChildArticleAdapter.ViewHolder> {
    private static List<Reply> list;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ChildArticleAdapter(List<Reply> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public List<Reply> getList() {
        return list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setList(List<Reply> list) {
        this.list = list;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onImageClick(View view, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_childarticle, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Reply item = list.get(position);
        if ("1".equals(item.getAdmin())) {
            holder.tvUsername.setTextColor(context.getResources().getColor(R.color.textRed));
        } else {
            holder.tvUsername.setTextColor(context.getResources().getColor(R.color.textGrey));
        }
        holder.tvUsername.setText(item.getUserid());
        holder.tvId.setText("No." + item.getId());
        holder.tvTime.setText(Tools.replaceTime(item.getNow()));
        Log.i("spq", item.getContent());
//        holder.tvContent.setText(Html.fromHtml(item.getContent()));
        TransFormContent.trans(Html.fromHtml(item.getContent()), holder.tvContent, new TransFormContent.OnClickListener() {
            @Override
            public void onClick(String s) {
                Reply reply = getRelay(s);
                if (null != reply) {
                    new ReplyDialog(getContext()).show(reply);
                }
            }
        });
        if (onItemClickListener != null) {
            holder.layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            });
        }
        if ("".equals(item.getImg())) {
            holder.imgContent.setVisibility(View.GONE);
        } else {
            holder.imgContent.setVisibility(View.VISIBLE);
            Glide.with(context).load(Port.IMG_THUMB_URL + item.getImg() + item.getExt())
                    .into(holder.imgContent);
            holder.imgContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onImageClick(v, holder.getAdapterPosition());
                }
            });
        }
    }

    public void addAll(List<Reply> data) {
        if(data==null)
            return;
        int start = list.size();
        list.addAll(data);
        this.notifyItemRangeInserted(start, list.size());
    }

    public void refresh(List<Reply> data) {
        if(data==null)
            return;
        list.clear();
        list.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvId, tvTime, tvContent;
        ImageView imgContent;
        private RelativeLayout layoutItem;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvId = (TextView) itemView.findViewById(R.id.tv_id);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            layoutItem = (RelativeLayout) itemView.findViewById(R.id.layout_item);
            imgContent = (ImageView) itemView.findViewById(R.id.img_content);
        }
    }

    public static Reply getRelay(String id) {
        for (Reply reply : list) {
            if (("No."+reply.getId()).equals(id)) {
                return reply;
            }
        }
        return null;
    }
}
