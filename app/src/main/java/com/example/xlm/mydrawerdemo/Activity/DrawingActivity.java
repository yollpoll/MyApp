package com.example.xlm.mydrawerdemo.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.example.xlm.mydrawerdemo.R;
import com.example.xlm.mydrawerdemo.base.BaseActivity;
import com.example.xlm.mydrawerdemo.utils.Tools;
import com.example.xlm.mydrawerdemo.view.DrawView;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by 鹏祺 on 2017/6/19.
 * 涂鸦板
 */

public class DrawingActivity extends BaseActivity {
    private DrawView mDrawView;
    private FloatingActionButton flbMenu;
    private FloatingActionMenu actionMenu;

    public static void gotoDrawingActivity(Context context) {
        Intent intent = new Intent(context, DrawingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        initView();
        initData();
    }

    private void initView() {
        mDrawView = (DrawView) findViewById(R.id.draw_view);
        flbMenu = (FloatingActionButton) findViewById(R.id.fb_menu);

        flbMenu.setOnClickListener(this);
    }

    private void initData() {
        initFloatingActionButton();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fb_menu:
                break;
            case R.id.img_color:
                changeColor();
                break;
            case R.id.img_width:
                changeWidth();
                break;
            case R.id.img_submit:
                submit();
                break;
            case R.id.img_save:
                save();
                break;
        }
    }

    int red = 0, blue = 0, green = 0;
    RelativeLayout rlColor;

    private void changeColor() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_choose_color);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        dialog.show();
        SeekBar sbRed = (SeekBar) dialog.findViewById(R.id.seek_red);
        SeekBar sbGreen = (SeekBar) dialog.findViewById(R.id.seek_green);
        SeekBar sbBlue = (SeekBar) dialog.findViewById(R.id.seek_blue);
        rlColor = (RelativeLayout) dialog.findViewById(R.id.rl_color);
        rlColor.setBackgroundColor(Color.rgb(red, green, blue));
        sbRed.setProgress(red *100/ 255);
        sbGreen.setProgress(green *100/ 255);
        sbBlue.setProgress(blue *100/ 255);

        sbRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red = progress * 255 / 100;
                rlColor.setBackgroundColor(Color.rgb(red, green, blue));
                mDrawView.setPaintColor(Color.rgb(red,green,blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                green = progress * 255 / 100;
                rlColor.setBackgroundColor(Color.rgb(red, green, blue));
                mDrawView.setPaintColor(Color.rgb(red,green,blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blue = progress * 255 / 100;
                rlColor.setBackgroundColor(Color.rgb(red, green, blue));
                mDrawView.setPaintColor(Color.rgb(red,green,blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void changeWidth() {

    }

    private void save() {

    }

    private void submit() {

    }

    private void initFloatingActionButton() {
        ImageView imgColor = new ImageView(this);
        imgColor.setImageResource(R.mipmap.icon_draw_color);
        imgColor.setPadding(20, 20, 20, 20);
        imgColor.setId(R.id.img_color);

        ImageView imgWidth = new ImageView(this);
        imgWidth.setImageResource(R.mipmap.icon_draw_width);
        imgWidth.setPadding(20, 20, 20, 20);
        imgWidth.setId(R.id.img_width);

        ImageView imgSave = new ImageView(this);
        imgSave.setImageResource(R.mipmap.icon_draw_save);
        imgSave.setPadding(20, 20, 20, 20);
        imgWidth.setId(R.id.img_save);

        ImageView imgSubmit = new ImageView(this);
        imgSubmit.setImageResource(R.mipmap.icon_draw_submit);
        imgSubmit.setPadding(20, 20, 20, 20);
        imgWidth.setId(R.id.img_submit);

        SubActionButton subColor = new SubActionButton.Builder(this).setContentView(imgColor).build();
        SubActionButton subWidth = new SubActionButton.Builder(this).setContentView(imgWidth).build();
        SubActionButton subSave = new SubActionButton.Builder(this).setContentView(imgSave).build();
        SubActionButton subSubmit = new SubActionButton.Builder(this).setContentView(imgSubmit).build();

        subColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor();
            }
        });
        subWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeWidth();
            }
        });
        subSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        subSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        int size = Tools.calculateDpToPx(50, DrawingActivity.this);
        actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(subColor, size, size)
                .addSubActionView(subWidth, size, size)
                .addSubActionView(subSave, size, size)
                .addSubActionView(subSubmit, size, size)
                // ...
                .attachTo(flbMenu)
                .build();
    }
}
