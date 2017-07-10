package com.example.xlm.mydrawerdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.http.Httptools;

import java.io.File;
import java.util.List;

/**
 * Created by 鹏祺 on 2017/6/30.
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {
    private List<File> files;
    private OnItemClickListenr onItemClickListenr;

    public FileListAdapter(List<File> files, OnItemClickListenr onItemClickListenr) {
        this.files = files;
        this.onItemClickListenr = onItemClickListenr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        File item = files.get(position);
        holder.tvFileName.setText(item.getName());
        if (null != onItemClickListenr)
            holder.tvFileName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListenr.onItemClick(v, holder.getAdapterPosition());
                }
            });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFileName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFileName = (TextView) itemView.findViewById(R.id.tv_file_name);
        }
    }
}
