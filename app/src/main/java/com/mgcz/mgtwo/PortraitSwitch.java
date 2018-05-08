package com.mgcz.mgtwo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.mgcz.mgtwo.util.ShareUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;

import demo.MainActivity;
import layaair.game.browser.ConchJNI;

/**
 * 头像选择
 */

public class PortraitSwitch {

    MainActivity main = MainActivity.getMainActivity();

    @JavascriptInterface
    public void changeHeadImgByLocalPhoto( ) {
        pickFromGallery(main, main.ALBUM);
    }

    @JavascriptInterface
    public void changeHeadImgByTakePhoto( ) {
        startCarmera(main.TACK_PHOTO);
    }

    /**
     * 拍照
     * @param requestCode
     */
    public void startCarmera(int requestCode){
        File file = new File(main.getExternalCacheDir(), "img.jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(main,"com.mgcz.mgtwo.fileprovider", file));
        }else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        main.startActivityForResult(intent, requestCode);
    }

    /**
     * 进入相册
     * @param context
     * @param requestCode
     */
    public void pickFromGallery(Activity context, int requestCode) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        context.startActivityForResult(pickIntent, requestCode);
    }

}



