package com.example.xlm.mydrawerdemo.Activity;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xlm.mydrawerdemo.API.FormListService;
import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.bean.ChildForm;
import com.example.xlm.mydrawerdemo.bean.Form;
import com.example.xlm.mydrawerdemo.fragment.ChooseEmojiDialogFragment;
import com.example.xlm.mydrawerdemo.http.Httptools;
import com.example.xlm.mydrawerdemo.utils.SPUtiles;
import com.example.xlm.mydrawerdemo.utils.ToastUtils;
import com.example.xlm.mydrawerdemo.utils.Tools;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by 鹏祺 on 2017/6/7.
 */

public class NewThreadActivity extends BaseActivity implements View.OnLongClickListener {
    private ImageView imgShowMoreTitle;
    private LinearLayout llMoreTitle;
    private Toolbar mToolbar;
    private EditText edtContent;
    private TextView tvTag;
    private CardView cardContent;
    private RelativeLayout rlRoot;
    private RelativeLayout rlTag;
    private String tagName, tagId;
    private Retrofit retrofit;
    private List<ChildForm> listTag = new ArrayList<>();
    private ImageView imgEmoji, imgPic, imgDraw, imgSend;
    private ImageView imgPicContent;

    public static void gotoNewThreadActivity(Context context, String tagName, String tagId) {
        Intent intent = new Intent(context, NewThreadActivity.class);
        intent.putExtra("tagName", tagName);
        intent.putExtra("tagId", tagId);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ChooseTagActivity.CHOOSE_TAG:
                if (resultCode == RESULT_OK) {
                    tagName = data.getStringExtra("tagName");
                    tagId = data.getStringExtra("tagId");
                    tvTag.setText(tagName);
                }
                break;
            case Tools.PIC_FROM_PHOTO:
                if (resultCode != RESULT_OK) {
                    ToastUtils.SnakeShowShort(rlRoot, "获取图片失败");
                    break;
                }
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    imgPicContent.setVisibility(View.VISIBLE);
                    imgPicContent.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case Tools.PIC_FROM_CAMERA:
                if (resultCode != RESULT_OK) {
                    ToastUtils.SnakeShowShort(rlRoot, "拍照失败");
                    break;
                }
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(Tools.imgUri, "image/*");
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Tools.imgUri);
                NewThreadActivity.this.startActivityForResult(intent, Tools.CROP_PHOTO);
                break;
            case Tools.CROP_PHOTO:
                if (resultCode != RESULT_OK) {
                    ToastUtils.SnakeShowShort(rlRoot, "剪裁失败");
                    break;
                }
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Tools.imgUri));
                    imgPicContent.setVisibility(View.VISIBLE);
                    imgPicContent.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_thread);
        initView();
        initData();
    }

    private void initView() {
        imgShowMoreTitle = (ImageView) findViewById(R.id.img_show_more_title);
        llMoreTitle = (LinearLayout) findViewById(R.id.ll_more_title);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        edtContent = (EditText) findViewById(R.id.edt_content);
        tvTag = (TextView) findViewById(R.id.tv_tag);
        cardContent = (CardView) findViewById(R.id.card_content);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        rlTag = (RelativeLayout) findViewById(R.id.rl_choose_tag);
        imgEmoji = (ImageView) findViewById(R.id.img_emoji);
        imgPic = (ImageView) findViewById(R.id.img_pic);
        imgDraw = (ImageView) findViewById(R.id.img_draw);
        imgSend = (ImageView) findViewById(R.id.img_send);
        imgPicContent = (ImageView) findViewById(R.id.img_pic_content);

        imgPicContent.setOnLongClickListener(this);
        imgSend.setOnClickListener(this);
        imgShowMoreTitle.setOnClickListener(this);
        tvTag.setOnClickListener(this);
        imgEmoji.setOnClickListener(this);
        imgPic.setOnClickListener(this);
        imgDraw.setOnClickListener(this);
        initTitle();
    }

    private void initTitle() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("发串");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewThreadActivity.this.finish();
            }
        });
    }

    private void initData() {
        tagName = getIntent().getStringExtra("tagName");
        tagId = getIntent().getStringExtra("tagId");

        retrofit = Httptools.getInstance().getRetrofit();
        tvTag.setText(tagName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvTag.setTransitionName(tagId);
        }
        getTagData();
        getKeyBoardHeight();
    }

    private void getTagData() {
        FormListService formList = retrofit.create(FormListService.class);
        Call<List<Form>> formsCall = formList.getFormList();
        formsCall.enqueue(new Callback<List<Form>>() {
            @Override
            public void onResponse(Response<List<Form>> response, Retrofit retrofit) {
                List<Form> forms = response.body();
                if (forms != null) {
                    for (int i = 0; i < forms.size(); i++) {
                        listTag.addAll(forms.get(i).getForums());
                    }
                }
                SPUtiles.saveTags(listTag);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    //获得软键盘高度
    private void getKeyBoardHeight() {
        rlRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rlRoot.getWindowVisibleDisplayFrame(r);

                int screenHeight = rlRoot.getRootView().getHeight();
                int heightDifference = screenHeight - (r.bottom - r.top);
                Log.d("spq", "Size    " + heightDifference);
            }
        });
    }

    public void setTagName(String name) {
        tvTag.setText(name);
    }

    //更多标题参数
    private void showMoreTitle() {
        llMoreTitle.setVisibility(View.VISIBLE);
        ObjectAnimator anim = ObjectAnimator.ofFloat(imgShowMoreTitle, "rotation", 0f, 180f);
        anim.setDuration(400);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();

        //layoutAnimation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.new_thread_anim);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animation);
        layoutAnimationController.setDelay(.1f);
        layoutAnimationController.setInterpolator(new AccelerateDecelerateInterpolator());
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_REVERSE);
        llMoreTitle.setLayoutAnimation(layoutAnimationController);
        llMoreTitle.startLayoutAnimation();
        edtContent.startAnimation(animation);
        imgPicContent.startAnimation(animation);
    }

    private void dismisMoreTitle() {
        //动画监听
        Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llMoreTitle.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        //img anim
        ObjectAnimator anim = ObjectAnimator.ofFloat(imgShowMoreTitle, "rotation", 180f, 0f);
        anim.setDuration(400);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();

        //layoutAnimation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.new_thread_anim_close);
        animation.setAnimationListener(mAnimationListener);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animation);
        layoutAnimationController.setDelay(.1f);
        layoutAnimationController.setInterpolator(new AccelerateDecelerateInterpolator());
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        llMoreTitle.setLayoutAnimation(layoutAnimationController);
        llMoreTitle.startLayoutAnimation();

        //edt anim
        edtContent.startAnimation(animation);
        imgPicContent.startAnimation(animation);
    }

    private void chooseTag() {
        Pair<View, String> pair1 = Pair.create((View) tvTag, tagId);
        Pair<View, String> pair2 = Pair.create((View) rlTag, "tag_group");
        ChooseTagActivity.gotoChooseTagActivity(this, tagId, pair1);
    }

    private void showEmojiDialog() {
        new ChooseEmojiDialogFragment(onEmojiClickListener).show(getSupportFragmentManager(), "chooseEmoji");
    }

    private ChooseEmojiDialogFragment.OnEmojiClickListener onEmojiClickListener = new ChooseEmojiDialogFragment.OnEmojiClickListener() {
        @Override
        public void onClick(String word, int id) {
            if (TextUtils.isEmpty(word)) {
                //选择了图片
                if (id == 0)
                    return;
                imgPicContent.setImageResource(id);
                imgPicContent.setVisibility(View.VISIBLE);
            } else {
                //选择了文字
                edtContent.append(word);
            }
        }
    };

    private void choosePhote() {
        Tools.showChoosePicDialog(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_show_more_title:
                if (llMoreTitle.getVisibility() == View.VISIBLE) {
                    dismisMoreTitle();
                } else {
                    showMoreTitle();
                }
                break;
            case R.id.tv_tag:
                chooseTag();
                break;
            case R.id.img_emoji:
                showEmojiDialog();
                break;
            case R.id.img_pic:
                choosePhote();
                break;
            case R.id.img_draw:
                DrawingActivity.gotoDrawingActivity(NewThreadActivity.this);
                break;
            case R.id.img_send:

                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.img_pic_content:
                imgPicContent.setVisibility(View.GONE);
                break;
        }
        return false;
    }
}
