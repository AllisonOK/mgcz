package com.mgcz.mgtwo;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.mgcz.mgtwo.util.InstallUtils;
import com.mgcz.mgtwo.util.Util;

import demo.MainActivity;
import layaair.game.browser.ConchJNI;

/**
 * Created by  on 201admin8/4/12.
 */

public class AppUpdate {

    MainActivity main = MainActivity.getMainActivity();
    private String currentVersion;

    String url = "http://download.mengguochengzhen.cn/mgcz.apk";

    @JavascriptInterface
    public void getCurrentVersion() {
        currentVersion = Util.getAppVersion(main);
        ConchJNI.RunJS("setVersion('" + currentVersion +"')");

    }

    @JavascriptInterface
    public void updateApk(String downloadUrl) {
        new InstallUtils(main, url, "mengguochengzhen", new InstallUtils.DownloadCallBack() {
            @Override
            public void onStart() {
                Log.i("update_app", "InstallUtils---onStart");
            }

            @Override
            public void onComplete(String path) {
                Log.i("update_app", "InstallUtils---onComplete:" + path);
                InstallUtils.installAPK(main, path, new InstallUtils.InstallCallBack() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(main, "正在安装程序", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(main, "安装失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onLoading(long total, long current) {
                Log.i("update_app", "InstallUtils----onLoading:-----total:" + total + ",current:" + current);
                ConchJNI.RunJS("setAPPLoadingTip(" + (int) (current * 100 / total) + ")");
            }

            @Override
            public void onFail(Exception e) {
                Log.i("update_app", "InstallUtils---onFail:" + e.getMessage());
            }

        }).downloadAPK();
    }

}
