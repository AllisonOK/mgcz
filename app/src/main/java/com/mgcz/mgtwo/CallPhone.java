package com.mgcz.mgtwo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.mgcz.mgtwo.util.Util;

import demo.MainActivity;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by admin on 2018/4/10.
 */

public class CallPhone {

    MainActivity main = MainActivity.getMainActivity();

    /**
     * 调用拨号功能
     *
     * @param phone 电话号码
     */
    @JavascriptInterface
    public void callPhone(String phone) {
        Intent intent = new Intent(main, CallPhoneAct.class);
        intent.putExtra("phoneNumber", phone);
        main.startActivity(intent);
    }

    /**
     * 复制内容到剪贴板
     */
    @JavascriptInterface
    public void paste(String content) {
        main.copy(content);
    }


}
