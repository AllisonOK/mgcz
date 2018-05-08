package com.mgcz.mgtwo.util;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.mgcz.mgtwo.R;


/**
 * Copyright 2018
 * QQ---> ${QQ}
 * <p>
 * 一句话简述这个类的功能
 * 功能详细描述
 *
 * @author wzp
 * @version 0.1 2018/01/22 13:41
 */
public class CallPhoneDialogUtil implements View.OnClickListener {
    private Dialog mDialog;
    private Activity mActivity;
    private String phone;
    private NoneAddressListener mListener;

    public CallPhoneDialogUtil(Dialog mDialog, Activity mActivity, String phone, NoneAddressListener listener) {
        this.mListener = listener;
        this.mDialog = mDialog;
        this.mActivity = mActivity;
        this.phone = phone;
        __init();
    }

    private void __init() {
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        TextView tvPhone = mDialog.findViewById(R.id.cancle);
        TextView tvCancle = mDialog.findViewById(R.id.cancle);
        TextView tvCall = mDialog.findViewById(R.id.sure);

        tvPhone.setText(phone);

        tvCancle.setOnClickListener(this);
        tvCall.setOnClickListener(this);
        mDialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle:
                mDialog.dismiss();
                if (mListener != null) {
                    mListener.cancle();
                }
                break;
            case R.id.sure:
                phone = formatPhone(phone);
                if (TextUtils.isEmpty(phone)) return;
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(mActivity, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mActivity.startActivity(intent);
                break;
        }

    }

    private String formatPhone(String phone) {
        //手机号格式化xxx xxxx xxxx
        if (phone == null || phone.length() == 0) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < phone.length(); i++) {
            if (i != 3 && i != 8 && phone.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(phone.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
        return sb.toString();
    }

    public interface NoneAddressListener {
        void cancle();
    }
}
