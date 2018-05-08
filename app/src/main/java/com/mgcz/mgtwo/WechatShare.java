package com.mgcz.mgtwo;

import android.webkit.JavascriptInterface;

import com.mgcz.mgtwo.util.ShareUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

import demo.MainActivity;

/**
 * Created by admin on 2018/4/12.
 */

public class WechatShare {
    MainActivity main = MainActivity.getMainActivity();

    @JavascriptInterface
    public void shareFriend() {
        ShareUtil.shareToPlatform(main, SHARE_MEDIA.WEIXIN);
    }

    @JavascriptInterface
    public void shareFriends() {
        ShareUtil.shareToPlatform(main, SHARE_MEDIA.WEIXIN_CIRCLE);
    }

}
