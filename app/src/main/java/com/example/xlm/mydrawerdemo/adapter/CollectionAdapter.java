package com.example.xlm.mydrawerdemo.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.bean.CollectionBean;
import com.example.xlm.mydrawerdemo.bean.Reply;
import com.example.xlm.mydrawerdemo.http.Port;
import com.example.xlm.mydrawerdemo.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 鹏祺 on 2017/5/25.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {
    private List<CollectionBean> list;
    private Context context;
    private OnItemClickListener onItemClickListener;


    public CollectionAdapter(List<CollectionBean> list, OnItemClickListener onItemClickListener) {
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CollectionBean item = list.get(position);
        if (item != null) {
            Spanned contentSpanned = Html.fromHtml(item.getContent());
            holder.content.setText(contentSpanned);
            holder.time.setText(Tools.replaceTime(item.getNow()));
            holder.id.setText("No." + item.getId());
            if ("0".equals(item.getAdmin())) {
                //黑名字
                holder.sender.setTextColor(context.getResources().getColor(R.color.textGrey));
            } else if ("1".equals(item.getAdmin())) {
                //红名
                holder.sender.setTextColor(context.getResources().getColor(R.color.textRed));
            }
            holder.sender.setText(item.getUserid());
            holder.comment.setVisibility(View.GONE);
            holder.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, holder.getAdapterPosition());
                }
            });
            holder.item_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onLongClick(v, holder.getAdapterPosition());
                    return true;
                }
            });
            if ("".equals(item.getImg())) {
                holder.imgContent.setVisibility(View.GONE);
            } else {
                holder.imgContent.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Port.IMG_THUMB_URL + item.getImg() + item.getExt())
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
        if (item.isShowCheck()) {
            Tools.setViewMargin(holder.cardRoot, false, context, -200, 200, 0, 0);
        } else {
            Tools.setViewMargin(holder.cardRoot, false, context, 0, 0, 0, 0);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onItemClickListener.onCheck(isChecked, holder.getAdapterPosition());
            }
        });
        holder.checkBox.setChecked(item.isCheck());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView content, id, time, sender;
        public RelativeLayout item_layout;
        public TextSwitcher comment;
        public ImageView imgContent;
        public CheckBox checkBox;
        public CardView cardRoot;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.tv_title);
            item_layout = (RelativeLayout) itemView.findViewById(R.id.item_recyclerview);
            id = (TextView) itemView.findViewById(R.id.tv_id);
            comment = (TextSwitcher) itemView.findViewById(R.id.tv_comment);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            sender = (TextView) itemView.findViewById(R.id.tv_sender_id);
            imgContent = (ImageView) itemView.findViewById(R.id.img_content);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            cardRoot = (CardView) itemView.findViewById(R.id.card_root);
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);

        void onImageClick(View view, int position);

        void onLongClick(View view, int position);

        void onCheck(boolean isChecked, int position);
    }
}
