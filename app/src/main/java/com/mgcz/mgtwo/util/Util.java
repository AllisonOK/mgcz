package com.mgcz.mgtwo.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sun.misc.BASE64Decoder;


/**
 * Created by admin on 2018/4/12.
 */

public class Util {

    //app版本
    public static String getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1";
    }

    /**
     * base64字符串转化成图片
     *
     * @param imgStr base64字符串
     * @return
     */
    public static boolean generateImage(Context context, String imgStr) {
        //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return false;

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }

            //先通过Environment（环境）的getExternalStorageState()方法来获取手机环境下的外置存储卡的状态，判断是否为挂载状态。
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //如果为挂载状态，那么就通过Environment的getExternalStorageDirectory()方法来获取
                String path = Environment.getExternalStorageDirectory() + "/mgcz_game";
                //新建一个File对象，把我们要建的文件路径传进去。
                File file = new File(path);
                //判断文件是否存在，如果存在就删除。
                if (file.exists()) {
                    boolean ia = file.delete();
                }
                try {
                    //通过文件的对象file的createNewFile()方法来创建文件
                    try {
                        boolean ha = file.mkdirs();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //新建一个FileOutputStream()，把文件的路径传进去
                    FileOutputStream fileOutputStream = new FileOutputStream(path + "/mgcz_qr.jpg");
                    //通过输出流对象写入字节数组
                    fileOutputStream.write(b);
                    fileOutputStream.flush();
                    //关流
                    fileOutputStream.close();
                    syncAlbum(context, path + "/mgcz_qr.jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 同步刷新系统相册
     *
     * @param imageUrl
     */
    private static void syncAlbum(Context context, String imageUrl) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            final Uri contentUri = Uri.fromFile(new File(imageUrl));
            scanIntent.setData(contentUri);
            context.sendBroadcast(scanIntent);
        } else {
            //4.4开始不允许发送"Intent.ACTION_MEDIA_MOUNTED"广播, 否则会出现: Permission Denial: not allowed to send broadcast android.intent.action.MEDIA_MOUNTED from pid=15410, uid=10135
            final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + imageUrl));
            context.sendBroadcast(intent);
        }
    }
}
