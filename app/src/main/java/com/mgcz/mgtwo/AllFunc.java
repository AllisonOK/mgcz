package com.mgcz.mgtwo;

import android.os.Build;
import android.webkit.JavascriptInterface;

import com.mgcz.mgtwo.util.CheckPermissionUtils;
import com.mgcz.mgtwo.util.ShareUtil;
import com.mgcz.mgtwo.util.Util;
import com.umeng.socialize.bean.SHARE_MEDIA;

import demo.MainActivity;

/**
 * 所有方法调用的汇总
 * Created by admin on 2018/4/27.
 */

public class AllFunc {

    MainActivity main = MainActivity.getMainActivity();


    /**
     * 保存base64字符串图片到本地
     * @param str
     */
    @JavascriptInterface
    public void savePicToLocal(String str) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            main.mQrCodePic = str;
            CheckPermissionUtils.checkStoragePermissions(main);
        } else {
            Util.generateImage(main, str);
        }
    }

}
