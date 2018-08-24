package com.example.xlm.mydrawerdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 鹏祺 on 2018/3/9.
 */

public abstract class FooterAdapter<DATA extends List, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    public final static int TYPE_FOOTER = 1;
    public final static int TYPE_CONTENT = 0;

    public final static int FOOTER_TYPE_NORMAL = 0;
    public final static int FOOTER_TYPE_LOADING = 1;
    public final static int FOOTER_TYPE_NOMORE = 2;

    public DATA data;
    private int mFooterStatus = 0;//0隐藏，1加载中，2没有更多
    private Object objectFooter;

    public FooterAdapter(DATA data) {
        this.data = data;
        objectFooter = new Object();
    }

    @Override
    public int getItemViewType(int position) {
        switch (mFooterStatus) {
            case 0:
                return TYPE_CONTENT;
            case 1:
                if (position == data.size() - 1)
                    return TYPE_FOOTER;
                return TYPE_CONTENT;
            case 2:
                if (position == data.size() - 1)
                    return TYPE_FOOTER;
                return TYPE_CONTENT;
            default:
                return TYPE_CONTENT;
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_CONTENT:
                return onCreateContentViewHolder(parent, viewType);
            default:
                return onCreateFooterViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_CONTENT:
                onBindContentViewHolder(holder, position);
                break;
            case TYPE_FOOTER:
                onBindFooterViewHolder(holder, position);
                break;
        }
    }

    public void setNomore() {
        if (mFooterStatus != 2) {
            mFooterStatus = 2;
            data.add(objectFooter);
            notifyItemInserted(data.size()-1);
        }
    }

    public void setLoading() {
        if (mFooterStatus != 1) {
            mFooterStatus = 1;
            data.add(objectFooter);
            notifyItemInserted(data.size() - 1);
        }
    }

    public void setNormal() {
        if (mFooterStatus != 0) {
            mFooterStatus = 0;
            data.remove(objectFooter);
            notifyItemRemoved(data.size());
        }
    }

    /**
     * @return 0隐藏，1加载中，2没有更多
     */
    public int getStatus() {
        return mFooterStatus;
    }

    protected abstract void onBindContentViewHolder(VH holder, int position);

    protected abstract void onBindFooterViewHolder(VH holder, int position);

    protected abstract VH onCreateContentViewHolder(ViewGroup parent, int viewType);

    protected abstract VH onCreateFooterViewHolder(ViewGroup parent, int viewType);

}
