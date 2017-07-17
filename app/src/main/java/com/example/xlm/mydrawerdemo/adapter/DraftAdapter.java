package com.example.xlm.mydrawerdemo.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.bean.Draft;
import com.example.xlm.mydrawerdemo.utils.Tools;

import java.util.List;

/**
 * Created by 鹏祺 on 2017/7/14.
 */

public class DraftAdapter extends RecyclerView.Adapter<DraftAdapter.ViewHolder> {
    private List<Draft> list;
    private Context context;
    private OnItemClickListenr onItemClickListenr;
    private OnItemLongClickListener onItemLongClickListener;
    private OnCheckListener onCheckListener;
    private OnImageClickListener onImageClickListener;

    public DraftAdapter(List<Draft> list, OnItemClickListenr onItemClickListenr,
                        OnItemLongClickListener onItemLongClickListener,
                        OnCheckListener onCheckListener, OnImageClickListener onImageClickListener) {
        this.list = list;
        this.onImageClickListener = onImageClickListener;
        this.onCheckListener = onCheckListener;
        this.onItemLongClickListener = onItemLongClickListener;
        this.onItemClickListenr = onItemClickListenr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draft, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Draft item = list.get(position);
        holder.tvDate.setText(item.getDate());
        holder.tvContent.setText(item.getContent());
        if (null != item.getPicture()) {
            holder.imgContent.setVisibility(View.VISIBLE);
            holder.imgContent.setImageBitmap(item.getPicture());
        } else {
            holder.imgContent.setVisibility(View.GONE);
        }
        if (null != onItemClickListenr)
            holder.cardRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListenr.onItemClick(v, position);
                }
            });
        if (null != onItemLongClickListener)
            holder.cardRoot.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onLongClick(v, position);
                    return true;
                }
            });
        if (null != onCheckListener)
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onCheckListener.onCheck(isChecked, position);
                }
            });
        if (null != onImageClickListener)
            holder.imgContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImageClickListener.onClick(v,position);
                }
            });
        holder.checkBox.setChecked(item.isCheck());
        if (item.isShowCheck()) {
            Tools.setViewMargin(holder.rlContent, false, context, -200, 200, 0, 0);
        } else {
            Tools.setViewMargin(holder.rlContent, false, context, 0, 0, 0, 0);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvContent;
        public ImageView imgContent;
        public CardView cardRoot;
        public CheckBox checkBox;
        public RelativeLayout rlContent;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            imgContent = (ImageView) itemView.findViewById(R.id.img_content);
            cardRoot = (CardView) itemView.findViewById(R.id.card_root);
            rlContent = (RelativeLayout) itemView.findViewById(R.id.rl_content);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }

    public interface OnCheckListener {
        void onCheck(boolean isCheck, int position);
    }

    public interface OnImageClickListener {
        void onClick(View view, int position);
    }
}
