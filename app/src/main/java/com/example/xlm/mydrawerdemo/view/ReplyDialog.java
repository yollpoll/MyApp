package com.example.xlm.mydrawerdemo.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.adapter.ChildArticleAdapter;
import com.example.xlm.mydrawerdemo.base.MyApplication;
import com.example.xlm.mydrawerdemo.bean.Reply;
import com.example.xlm.mydrawerdemo.http.Port;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;
import com.example.xlm.mydrawerdemo.utils.Tools;
import com.example.xlm.mydrawerdemo.utils.TransFormContent;

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
    }

    protected ReplyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static ReplyDialog getInstance(Context context) {
        if (instance == null) {
            instance = new ReplyDialog(context);
        }
        return instance;
    }

    public void show(final Reply reply) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_dialog_reply, null);
        setContentView(view);
        this.setCanceledOnTouchOutside(true);

        TextView tvId = (TextView) view.findViewById(R.id.tv_id);
        TextView tvName = (TextView) view.findViewById(R.id.tv_username);
        TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        final ImageView imgContent = (ImageView) view.findViewById(R.id.img_content);
        if ("1".equals(reply.getAdmin())) {
            tvName.setTextColor(mContext.getResources().getColor(R.color.textRed));
        } else {
            tvName.setTextColor(mContext.getResources().getColor(R.color.textGrey));
        }
        tvName.setText(reply.getUserid());
        tvId.setText("No." + reply.getId());
        tvTime.setText(Tools.replaceTime(reply.getNow()));
        TransFormContent.trans(Html.fromHtml(reply.getContent()), tvContent, new TransFormContent.OnClickListener() {
            @Override
            public void onClick(String s) {
                Reply reply = ChildArticleAdapter.getRelay(s);
                if (null != reply) {
                    ReplyDialog.getInstance(getContext()).show(reply);
                }
            }
        });
        if (TextUtils.isEmpty(reply.getImg())) {
            imgContent.setVisibility(View.GONE);
        } else {

            Glide.with(MyApplication.getInstance()).load(Port.IMG_THUMB_URL + reply.getImg() + reply.getExt())
                    .asBitmap()
                    .into(new Target<Bitmap>() {
                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            Log.i("spq", "onLoadStarted");
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            Log.i("spq", "onLoadFailed");
                            imgContent.setVisibility(View.GONE);
                        }

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Log.i("spq", "onResourceReady");
                            imgContent.setImageBitmap(resource);
                            imgContent.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                            Log.i("spq", "onLoadCleared");
                        }

                        @Override
                        public void getSize(SizeReadyCallback cb) {

                        }

                        @Override
                        public void setRequest(Request request) {

                        }

                        @Override
                        public Request getRequest() {
                            return null;
                        }

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onStop() {

                        }

                        @Override
                        public void onDestroy() {

                        }
                    });
        }

        if (null != this && !this.isShowing()) {
            this.show();
        }
    }
}
