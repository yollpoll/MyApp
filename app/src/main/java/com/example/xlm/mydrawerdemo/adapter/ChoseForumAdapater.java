package com.example.xlm.mydrawerdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.bean.ChildForm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 鹏祺 on 2016/3/31.
 */
public class ChoseForumAdapater extends RecyclerView.Adapter<ChoseForumAdapater.ViewHolder>{
    private List<ChildForm> forms;
    private Context context;
    private OnClickListener onClickListener;

    public ChoseForumAdapater(List<ChildForm> forms, Context context) {
        this.forms = forms;
        this.context = context;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_recycler_chose_forum, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ChildForm item=forms.get(position);
        holder.tvChoseForum.setText(item.getName());
        holder.rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onItemClick(v, holder.getAdapterPosition(), holder.checkBoxForum);
                }
            }
        });
        if(item.isChecked()){
            holder.checkBoxForum.setChecked(true);
        }else {
            holder.checkBoxForum.setChecked(false);
        }
        holder.checkBoxForum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(onClickListener!=null)
                    onClickListener.onChcked(item,isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return forms.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvChoseForum;
        CheckBox checkBoxForum;
        RelativeLayout rlRoot;
        public ViewHolder(View itemView) {
            super(itemView);
            rlRoot= (RelativeLayout) itemView.findViewById(R.id.rl_root);
            tvChoseForum= (TextView) itemView.findViewById(R.id.tv_forum);
            checkBoxForum= (CheckBox) itemView.findViewById(R.id.check_forum);
        }
    }
    public interface OnClickListener{
        void onItemClick(View view,int position,CheckBox checkBox);
        void onChcked(ChildForm childForm,boolean isChecked);
    }
}
