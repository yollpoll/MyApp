package com.example.xlm.mydrawerdemo.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xlm.mydrawerdemo.Activity.ImageActivity;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.ChildArticleAdapter;
import com.example.xlm.mydrawerdemo.base.MyApplication;
import com.example.xlm.mydrawerdemo.bean.Reply;
import com.example.xlm.mydrawerdemo.http.Port;
import com.example.xlm.mydrawerdemo.utils.Tools;
import com.example.xlm.mydrawerdemo.utils.TransFormContent;

import java.util.List;

/**
 * Created by 鹏祺 on 2016/9/2.
 */
public class ReplyDialog extends Dialog {
    private static ReplyDialog instance;
    private Context mContext;

    public ReplyDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public ReplyDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext=context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    protected ReplyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static ReplyDialog getInstance(Context context) {
        if (instance == null) {
            instance = new ReplyDialog(context, R.style.NoTitleDialog);

        }
        return instance;
    }

    public void show(final Reply reply, final List<Reply> list) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_dialog_reply, null);
        // 去掉对话框顶部栏
        setContentView(view);
        this.setCanceledOnTouchOutside(true);

        TextView tvId = (TextView) view.findViewById(R.id.tv_id);
        TextView tvName = (TextView) view.findViewById(R.id.tv_username);
        TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        final ImageView imgContent = (ImageView) view.findViewById(R.id.img_content);
        if ("1".equals(reply.getAdmin())) {
            tvName.setTextColor(mContext.getResources().getColor(R.color.textRed));
            tvName.setText(reply.getUserid());
        } else {
            String[] userId = reply.getUserid().split("-");
            String userIdStr = "";
            for (int i = 0; i < userId.length; i++) {
                if (i != 0) {
                    userId[i] = "<br>(" + userId[i] + ")";
                }
                userIdStr += userId[i];
            }
            tvName.setText(Html.fromHtml(userIdStr));
            tvName.setTextColor(mContext.getResources().getColor(R.color.textGrey));
        }
        tvId.setText("No." + reply.getId());
        tvTime.setText(Tools.replaceTime(reply.getNow()));
        TransFormContent.trans(Html.fromHtml(reply.getContent()), tvContent, new TransFormContent.OnClickListener() {
            @Override
            public void onClick(String s) {
                Reply reply = ChildArticleAdapter.getRelay(s,list);
                if (null != reply) {
                    ReplyDialog.getInstance(getContext()).show(reply,list);
                }else {
                    //串里没有，直接跳转
//                    ChildArticleActivity.gotoChildArticleActivity(mContext, s.substring(5, s.length() - 1), null);
                }
            }
        });
        if (TextUtils.isEmpty(reply.getImg())) {
            imgContent.setVisibility(View.GONE);
        } else {
            imgContent.setVisibility(View.VISIBLE);
            Glide.with(MyApplication.getInstance())
                    .load(Port.getThumbUrl() + reply.getImg() + reply.getExt())
                    .centerCrop()
                    .crossFade()
                    .error(R.mipmap.icon_yygq)
                    .into(imgContent);
            imgContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = Port.getImg() + reply.getImg() + reply.getExt();
                    ImageActivity.gotoImageActivity((Activity) mContext, url, reply.getImg() + reply.getExt(), v);
                }
            });
        }

        if (null != this && !this.isShowing()) {
            this.show();
        }
    }
}
