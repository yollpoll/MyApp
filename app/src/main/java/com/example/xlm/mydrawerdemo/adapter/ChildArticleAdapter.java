package com.example.xlm.mydrawerdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseViewHolder;
import com.example.xlm.mydrawerdemo.bean.Reply;
import com.example.xlm.mydrawerdemo.http.Port;
import com.example.xlm.mydrawerdemo.utils.Tools;
import com.example.xlm.mydrawerdemo.utils.TransFormContent;
import com.example.xlm.mydrawerdemo.view.ReplyDialog;

import java.util.List;

/**
 * Created by 鹏祺 on 2016/1/16.
 */
public class ChildArticleAdapter extends FooterAdapter<List<Reply>, BaseViewHolder> {
    private static List<Reply> list;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ChildArticleAdapter(List<Reply> list) {
        super(list);
        this.list = list;
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

        void onLongClick(View view, int position);
    }

//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        context = parent.getContext();
//        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_childarticle, parent, false);
//        ViewHolder holder = new ViewHolder(view);
//        return holder;
//    }

//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position) {
//        Reply item = list.get(position);
//        if ("1".equals(item.getAdmin())) {
//            holder.tvUsername.setTextColor(context.getResources().getColor(R.color.textRed));
//        } else {
//            if (list.size() > 0 && item.getUserid().equals(list.get(0).getUserid())) {
//                holder.tvUsername.setTextColor(Color.parseColor("#7cb342"));
//            } else {
//                holder.tvUsername.setTextColor(context.getResources().getColor(R.color.textGrey));
//            }
//        }
//        holder.tvUsername.setText(item.getUserid());
//        holder.tvId.setText("No." + item.getId());
//        holder.tvTime.setText(Tools.replaceTime(item.getNow()));
//        Log.i("spq", item.getContent());
////        holder.tvContent.setText(Html.fromHtml(item.getContent()));
//        TransFormContent.trans(Html.fromHtml(item.getContent()), holder.tvContent, new TransFormContent.OnClickListener() {
//            @Override
//            public void onClick(String s) {
//                Reply reply = getRelay(s);
//                if (null != reply) {
//                    new ReplyDialog(getContext()).show(reply);
//                }
//            }
//        });
//        if (onItemClickListener != null) {
//            holder.layoutItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemClickListener.onItemClick(v, holder.getAdapterPosition());
//                }
//            });
//        }
//        if (null != onItemClickListener) {
//            holder.layoutItem.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    onItemClickListener.onLongClick(v, holder.getAdapterPosition());
//                    return true;
//                }
//            });
//        }
//        if ("".equals(item.getImg())) {
//            holder.imgContent.setVisibility(View.GONE);
//        } else {
//            holder.imgContent.setVisibility(View.VISIBLE);
//            Glide.with(context)
//                    .load(Port.getThumbUrl() + item.getImg() + item.getExt())
//                    .centerCrop()
//                    .crossFade()
//                    .error(R.mipmap.icon_yygq)
//                    .into(holder.imgContent);
//
//            holder.imgContent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemClickListener.onImageClick(v, holder.getAdapterPosition());
//                }
//            });
//        }
//    }

    @Override
    protected void onBindContentViewHolder(final BaseViewHolder baseViewHolder, int position) {
        final ChildArticleAdapter.ViewHolder holder = (ViewHolder) baseViewHolder;
        Reply item = list.get(position);
        if ("1".equals(item.getAdmin())) {
            holder.tvUsername.setTextColor(context.getResources().getColor(R.color.textRed));
        } else {
            if (list.size() > 0 && item.getUserid().equals(list.get(0).getUserid())) {
                holder.tvUsername.setTextColor(Color.parseColor("#7cb342"));
            } else {
                holder.tvUsername.setTextColor(context.getResources().getColor(R.color.textGrey));
            }
        }
        String[] userId = item.getUserid().split("-");
        String userIdStr = "";
        for (int i = 0; i < userId.length; i++) {
            if (i != 0) {
                userId[i] = "<br>(" + userId[i] + ")";
            }
            userIdStr += userId[i];
        }
        holder.tvUsername.setText(Html.fromHtml(userIdStr));
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
                } else {
                    //串里没有，直接跳转
//                    ChildArticleActivity.gotoChildArticleActivity(context, s.substring(5, s.length() - 1), null);
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
        if (null != onItemClickListener) {
            holder.layoutItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onLongClick(v, holder.getAdapterPosition());
                    return true;
                }
            });
        }
        if ("".equals(item.getImg())) {
            holder.imgContent.setVisibility(View.GONE);
        } else {
            holder.imgContent.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Port.getThumbUrl() + item.getImg() + item.getExt())
                    .centerCrop()
                    .crossFade()
                    .error(R.mipmap.icon_yygq)
                    .into(holder.imgContent);

            holder.imgContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onImageClick(v, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    protected void onBindFooterViewHolder(BaseViewHolder holder, int position) {

    }

    @Override
    protected ViewHolder onCreateContentViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_childarticle, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    protected BaseViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_adapter, parent, false);
        return new BaseViewHolder(view);
    }

    public void addAll(List<Reply> data) {
        if (data == null)
            return;
        int start = list.size();
        list.addAll(data);
        this.notifyItemRangeInserted(start, list.size());
    }

    public void refresh(List<Reply> data) {
        if (data == null)
            return;
        list.clear();
        list.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends BaseViewHolder {
        TextView tvUsername, tvId, tvTime, tvContent;
        ImageView imgContent;
        private CardView layoutItem;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvId = (TextView) itemView.findViewById(R.id.tv_id);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            layoutItem = (CardView) itemView.findViewById(R.id.layout_item);
            imgContent = (ImageView) itemView.findViewById(R.id.img_content);
        }
    }

    public static Reply getRelay(String id) {
        for (Reply reply : list) {
            if ((">>No." + reply.getId()).equals(id)) {
                return reply;
            }
        }
        return null;
    }
}
