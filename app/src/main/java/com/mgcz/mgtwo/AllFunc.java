package com.mgcz.mgtwo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.webkit.JavascriptInterface;

import com.mgcz.mgtwo.util.CheckPermissionUtils;
import com.mgcz.mgtwo.util.ShareUtil;
import com.mgcz.mgtwo.util.Util;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import demo.MainActivity;
import layaair.game.browser.ConchJNI;

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
            boolean isSuccess = Util.generateImage(main, str);
            if (isSuccess) {
                ConchJNI.RunJS("isSave('1')");
            } else {
                ConchJNI.RunJS("isSave('0')");
            }
        }
    }

    /**
     * 跳转商城应用
     */
    @JavascriptInterface
    public void intentToShop(){
        String appPackageName = "com.zsl.dream.fruit";
        String downloadAppAddress = "http://download.mengguochengzhen.cn/install_mall.html";
        if (isAvilible(appPackageName)) {
            PackageManager packageManager = main.getPackageManager();  // 当前Activity获得packageManager对象
            Intent intent=new Intent();
            try {
                //下面字符串就是你另外一个应用的包的路径
                intent = packageManager.getLaunchIntentForPackage("com.zsl.dream.fruit");
            } catch (Exception e) {
                e.printStackTrace();
            }
            main.startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(downloadAppAddress);
            intent.setData(content_url);
            main.startActivity(intent);
        }
    }

    /**
     * 判断应用程序是否已安装
     * @param packageName
     * @return
     */
    private boolean isAvilible(String packageName){
        final PackageManager packageManager = main.getPackageManager();//获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名
        //从pinfo中将包名字逐一取出，压入pName list中
        if(pinfo != null){
            for(int i = 0; i < pinfo.size(); i++){
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

}
