package com.mgcz.mgtwo;

import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

import demo.MainActivity;
import layaair.game.browser.ConchJNI;
import layaair.game.browser.ExportJavaFunction;

/**
 * Created by admin on 2018/4/8.
 */

public class AliCloudBindAccount {

    MainActivity main = MainActivity.getMainActivity();
    CloudPushService mPushService = PushServiceFactory.getCloudPushService();

    /**
     * 绑定账户接口
     * 1. 绑定账号后,可以在服务端通过账号进行推送
     * 2. 一个设备只能绑定一个账号
     */
    public void bindAccount(String account){
        mPushService.bindAccount(account, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                Log.i("onSuccess", s);
//                ExportJavaFunction.CallBackToJS (main, "GetDeviceId", mPushService.getDeviceId());
                ConchJNI.RunJS("GetDeviceId('" + mPushService.getDeviceId() +"')");
            }

            @Override
            public void onFailed(String s, String s1) {
                Log.i("onFailed", s);
//                ExportJavaFunction.CallBackToJS (main, "GetDeviceId", "0");
                ConchJNI.RunJS("GetDeviceId('" + mPushService.getDeviceId() +"')");
            }
        });
    }

    /**
     * 解绑账户接口
     * 1. 调用该接口后,设备与账号的绑定关系解除
     */
    private void unBindAccount() {
        mPushService.unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                Log.i("onSuccess", s);
            }

            @Override
            public void onFailed(String errorCode, String errorMsg) {
                Log.i("onFailed", errorMsg);
            }
        });
    }


}
