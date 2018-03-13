package com.example.xlm.mydrawerdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by 鹏祺 on 2018/3/9.
 */

public abstract class BasicFooterAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_HEAD = 2;

    private int headCount;
    private int contentCount;
    private int footerCount;


    @Override
    public int getItemViewType(int position) {
        if (position < headCount) {
            return TYPE_HEAD;
        } else if (position >= headCount && position < headCount + contentCount) {
            return TYPE_ITEM;
        } else
            return TYPE_FOOTER;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                return onCreateHeadItem(parent, viewType);
            case TYPE_FOOTER:
                return onCreateFooterItem(parent, viewType);
            case TYPE_ITEM:
                return onCreateContentItem(parent, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (headCount > 0 && getItemViewType(position) == TYPE_HEAD) {
            onBindHeadItem(holder, position);
        } else if (contentCount > 0 && getItemViewType(position) == TYPE_ITEM) {
            onBindContentItem(holder, position);
        } else {
            onBindFooterItem(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        headCount = getHeadCount();
        contentCount = getContentCount();
        footerCount = getFooterCount();
        return headCount + contentCount + footerCount;
    }

    /**
     * Notifies that a footer item is inserted.
     *
     * @param position the position of the content item.
     */
    public final void notifyFooterItemInserted(int position) {
        getItemCount();
        int newHeaderItemCount = getHeadCount();
        int newContentItemCount = getContentCount();
        int newFooterItemCount = getFooterCount();
        if (position < 0 || position >= newFooterItemCount) {
            throw new IndexOutOfBoundsException("The given position " + position + " is not within the position bounds for footer items [0 - " + (newFooterItemCount - 1) + "].");
        }
        try {
            notifyItemInserted(position + newHeaderItemCount + newContentItemCount);
        } catch (Throwable e) {
        }
    }

    /**
     * Notifies that multiple footer items are inserted.
     *
     * @param positionStart the position.
     * @param itemCount     the item count.
     */
    public final void notifyFooterItemRangeInserted(int positionStart, int itemCount) {
        getItemCount();
        int newHeaderItemCount = getHeadCount();
        int newContentItemCount = getContentCount();
        int newFooterItemCount = getFooterCount();
        if (positionStart < 0 || itemCount < 0 || positionStart + itemCount > newFooterItemCount) {
            throw new IndexOutOfBoundsException("The given range [" + positionStart + " - " + (positionStart + itemCount - 1) + "] is not within the position bounds for footer items [0 - " + (newFooterItemCount - 1) + "].");
        }
        try {
            notifyItemRangeInserted(positionStart + newHeaderItemCount + newContentItemCount, itemCount);
        } catch (Throwable e) {
        }
    }

    /**
     * Notifies that a footer item is changed.
     *
     * @param position the position.
     */
    public final void notifyFooterItemChanged(int position) {
        if (position < 0 || position >= footerCount) {
            throw new IndexOutOfBoundsException("The given position " + position + " is not within the position bounds for footer items [0 - " + (footerCount - 1) + "].");
        }
        try {
            notifyItemChanged(position + headCount + contentCount);
        } catch (Throwable e) {
        }
    }

    /**
     * Notifies that multiple footer items are changed.
     *
     * @param positionStart the position.
     * @param itemCount     the item count.
     */
    public final void notifyFooterItemRangeChanged(int positionStart, int itemCount) {
        if (positionStart < 0 || itemCount < 0 || positionStart + itemCount > footerCount) {
            throw new IndexOutOfBoundsException("The given range [" + positionStart + " - " + (positionStart + itemCount - 1) + "] is not within the position bounds for footer items [0 - " + (footerCount - 1) + "].");
        }
        try {
            notifyItemRangeChanged(positionStart + headCount + contentCount, itemCount);
        } catch (Throwable e) {
        }
    }

    /**
     * Notifies that an existing footer item is moved to another position.
     *
     * @param fromPosition the original position.
     * @param toPosition   the new position.
     */
    public final void notifyFooterItemMoved(int fromPosition, int toPosition) {
        if (fromPosition < 0 || toPosition < 0 || fromPosition >= footerCount || toPosition >= footerCount) {
            throw new IndexOutOfBoundsException("The given fromPosition " + fromPosition + " or toPosition " + toPosition + " is not within the position bounds for footer items [0 - " + (footerCount - 1) + "].");
        }
        try {
            notifyItemMoved(fromPosition + headCount + contentCount, toPosition + headCount + contentCount);
        } catch (Throwable e) {
        }
    }

    /**
     * Notifies that a footer item is removed.
     *
     * @param position the position.
     */
    public final void notifyFooterItemRemoved(int position) {
        if (position < 0 || position >= footerCount) {
            throw new IndexOutOfBoundsException("The given position " + position + " is not within the position bounds for footer items [0 - " + (footerCount - 1) + "].");
        }
        try {
            notifyItemRemoved(position + headCount + contentCount);
        } catch (Throwable e) {
        }
    }

    /**
     * Notifies that multiple footer items are removed.
     *
     * @param positionStart the position.
     * @param itemCount     the item count.
     */
    public final void notifyFooterItemRangeRemoved(int positionStart, int itemCount) {
        if (positionStart < 0 || itemCount < 0 || positionStart + itemCount > footerCount) {
            throw new IndexOutOfBoundsException("The given range [" + positionStart + " - " + (positionStart + itemCount - 1) + "] is not within the position bounds for footer items [0 - " + (footerCount - 1) + "].");
        }
        try {
            notifyItemRangeRemoved(positionStart + headCount + contentCount, itemCount);
        } catch (Throwable e) {
        }
    }


    protected abstract int getHeadCount();

    protected abstract int getContentCount();

    protected abstract int getFooterCount();

    protected abstract VH onCreateHeadItem(ViewGroup parent, int viewType);

    protected abstract VH onCreateContentItem(ViewGroup parent, int viewType);

    protected abstract VH onCreateFooterItem(ViewGroup parent, int viewType);

    protected abstract void onBindHeadItem(VH headHolder, int position);

    protected abstract void onBindContentItem(VH contentHolder, int position);

    protected abstract void onBindFooterItem(VH footerHolder, int position);


}
