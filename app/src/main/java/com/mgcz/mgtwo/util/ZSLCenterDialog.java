package com.mgcz.mgtwo.util;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Window;

import com.mgcz.mgtwo.R;


/**
 * Copyright 2017 陕西醉丝路广告文化传播有限公司
 * <p>
 * 一句话功能简述
 * 功能详细描述
 *
 * @author wangzhipeng
 * @version 0.1 2017/7/3 8:51
 */
public class ZSLCenterDialog extends Dialog {
    private boolean mIsRound = false;
    private Context mContext;

    public ZSLCenterDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public ZSLCenterDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected ZSLCenterDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }


    public ZSLCenterDialog(int layout, Context context) {
        super(context);
        __initDialog(layout);
    }

    public ZSLCenterDialog(int layout, Context context, boolean isRound) {
        super(context, R.style.MyDialogStyle);
        this.mIsRound = isRound;
        __initDialog(layout);
    }

    private void __initDialog(int layout) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layout);
        Window window = getWindow();
        if (!mIsRound) {
            setCanceledOnTouchOutside(true);
            window.setBackgroundDrawableResource(R.drawable.dialog_bg_circle);
        } else {
            setCanceledOnTouchOutside(false);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }
}
