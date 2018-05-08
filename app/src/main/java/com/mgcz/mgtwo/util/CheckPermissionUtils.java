package com.mgcz.mgtwo.util;

import android.Manifest;
import android.app.Activity;

import com.yanzhenjie.permission.AndPermission;

/**
 * 6.0以上权限适配
 * Created by li.s on 2017/11/30.
 */

public class CheckPermissionUtils {

    public static void checkStoragePermissions(Activity activity) {
        AndPermission.with(activity)
                .requestCode(100)
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .send();
    }

}
