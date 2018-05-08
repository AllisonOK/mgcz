package com.mgcz.mgtwo.util;

import android.app.Activity;
import android.util.Log;

import com.mgcz.mgtwo.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import layaair.game.browser.ConchJNI;

/**
 * 分享工具类
 */

public class ShareUtil {

    private static final String shareTitle = "梦果成真";
    private static final String shareDescription = "你玩过动动手指就能赚钱的游戏吗？ 你做过投资不亏本的项目吗？全球首创越玩越赚钱的农场经营手游横空问世了！";
    private static final String linkUrl = "https://download.mengguochengzhen.cn";

    public static void shareToPlatform(Activity activity, SHARE_MEDIA share_media) {
        UMImage image = new UMImage(activity,
                R.mipmap.ic_launcher);
        UMWeb web = new UMWeb(linkUrl);
        web.setTitle(shareTitle);
        web.setThumb(image);
        web.setDescription(shareDescription);
        new ShareAction(activity)
                .setPlatform(share_media)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
//                        ConchJNI.RunJS("isShare('" + "999" + "')");
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        ConchJNI.RunJS("isShare('" + "1" + "')");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        ConchJNI.RunJS("isShare('" + "0" + "')");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        ConchJNI.RunJS("isShare('" + "0" + "')");
                    }
                }).withMedia(web).share();
    }

}
