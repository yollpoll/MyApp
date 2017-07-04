package com.example.xlm.mydrawerdemo.base;

import android.annotation.SuppressLint;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.xlm.mydrawerdemo.utils.Tools;

/**
 * Created by 鹏祺 on 2017/7/3.
 */

@SuppressLint("ValidFragment")
public class BaseDialogFragment extends DialogFragment implements View.OnClickListener{
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.horizontalMargin = Tools.calculateDpToPx(50, getActivity());
        window.setAttributes(layoutParams);
    }

    @Override
    public void onClick(View v) {

    }
}
