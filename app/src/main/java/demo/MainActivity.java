package demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import layaair.autoupdateversion.AutoUpdateAPK;
import layaair.game.IMarket.IPlugin;
import layaair.game.IMarket.IPluginRuntimeProxy;
import layaair.game.Market.GameEngine;
import layaair.game.browser.ConchJNI;
import layaair.game.config.config;

import com.kevin.crop.UCrop;
import com.mgcz.mgtwo.PortraitCropActivity;
import com.mgcz.mgtwo.R;
import com.mgcz.mgtwo.util.CheckPermissionUtils;
import com.mgcz.mgtwo.util.Util;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.widget.Toast;


public class MainActivity extends Activity {
    public static final int AR_CHECK_UPDATE = 1;
    public static final int ALBUM = 2;
    public static final int TACK_PHOTO = 3;
    private IPlugin mPlugin = null;
    private IPluginRuntimeProxy mProxy = null;
    boolean isLoad = false;
    boolean isExit = false;
    // 拍照临时图片
    public String mTempPhotoPath;
    // 剪切后图像文件
    private Uri mDestinationUri;
    public String mQrCodePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*
         * 如果不想使用更新流程，可以屏蔽checkApkUpdate函数，直接打开initEngine函数
         */
        checkApkUpdate(this);
        mainActivity = this;
        //initEngine();
        mDestinationUri = Uri.fromFile(new File(getCacheDir(), "cropImage.jpeg"));
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
    }

    public void initEngine() {
        mProxy = new RuntimeProxy(this);
        mPlugin = new GameEngine(this);
        mPlugin.game_plugin_set_runtime_proxy(mProxy);
        mPlugin.game_plugin_set_option("localize", "false");
//        mPlugin.game_plugin_set_option("gameUrl", "http://dream2test.mengguochengzhen.cn/index.html");
        mPlugin.game_plugin_set_option("gameUrl", "http://dream2.mengguochengzhen.cn/index.html");
        mPlugin.game_plugin_init(3);
        View gameView = mPlugin.game_plugin_get_view();
        this.setContentView(gameView);
        isLoad = true;
    }

    public boolean isOpenNetwork(Context context) {
        if (!config.GetInstance().m_bCheckNetwork)
            return true;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager.getActiveNetworkInfo() != null && (connManager.getActiveNetworkInfo().isAvailable() && connManager.getActiveNetworkInfo().isConnected());
    }

    public void settingNetwork(final Context context, final int p_nType) {
        AlertDialog.Builder pBuilder = new AlertDialog.Builder(context);
        pBuilder.setTitle("连接失败，请检查网络或与开发商联系").setMessage("是否对网络进行设置?");
        // 退出按钮
        pBuilder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface p_pDialog, int arg1) {
                Intent intent;
                try {
                    String sdkVersion = android.os.Build.VERSION.SDK;
                    if (Integer.valueOf(sdkVersion) > 10) {
                        intent = new Intent(
                                android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                    } else {
                        intent = new Intent();
                        ComponentName comp = new ComponentName(
                                "com.android.settings",
                                "com.android.settings.WirelessSettings");
                        intent.setComponent(comp);
                        intent.setAction("android.intent.action.VIEW");
                    }
                    ((Activity) context).startActivityForResult(intent, p_nType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        pBuilder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ((Activity) context).finish();
            }
        });
        AlertDialog alertdlg = pBuilder.create();
        alertdlg.setCanceledOnTouchOutside(false);
        alertdlg.show();
    }

    public void checkApkUpdate(Context context, final ValueCallback<Integer> callback) {
        if (isOpenNetwork(context)) {
            // 自动版本更新
            if ("0".equals(config.GetInstance().getProperty("IsHandleUpdateAPK", "0")) == false) {
                Log.e("0", "==============Java流程 checkApkUpdate");
                new AutoUpdateAPK(context, new ValueCallback<Integer>() {
                    @Override
                    public void onReceiveValue(Integer integer) {
                        Log.e("", ">>>>>>>>>>>>>>>>>>");
                        callback.onReceiveValue(integer);
                    }
                });
            } else {
                Log.e("0", "==============Java流程 checkApkUpdate 不许要自己管理update");
                callback.onReceiveValue(1);
            }
        } else {
            settingNetwork(context, AR_CHECK_UPDATE);
        }
    }

    public void checkApkUpdate(Context context) {
        InputStream inputStream = getClass().getResourceAsStream("/assets/config.ini");
        config.GetInstance().init(inputStream);
        checkApkUpdate(context, new ValueCallback<Integer>() {
            @Override
            public void onReceiveValue(Integer integer) {
                if (integer.intValue() == 1) {
                    initEngine();
                } else {
                    finish();
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK) return;

        if (requestCode == AR_CHECK_UPDATE) {
            checkApkUpdate(this);
        } else if (requestCode == ALBUM) {
            final Uri selectedUri = intent.getData();
            if (selectedUri != null) {
                startCropActivity(intent.getData());
            } else {
                Toast.makeText(this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == TACK_PHOTO) {
            //设置文件保存路径这里放在跟目录下
            File picture = new File(getExternalCacheDir() + "/img.jpg");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri = FileProvider.getUriForFile(this, "com.mgcz.mgtwo.fileprovider", picture);
                //裁剪照片
                startCropActivity(uri);
            } else {
                //裁剪照片
                startCropActivity(Uri.fromFile(picture));
            }

        } else if (requestCode == UCrop.REQUEST_CROP) {
            handleCropResult(intent);
        }
    }

    protected void onPause() {
        super.onPause();
        if (isLoad) mPlugin.game_plugin_onPause();
    }

    //------------------------------------------------------------------------------
    protected void onResume() {
        super.onResume();
        if (isLoad) mPlugin.game_plugin_onResume();

    }

    protected void onDestroy() {
        super.onDestroy();
        if (isLoad) mPlugin.game_plugin_onDestory();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public MainActivity() {
        mainActivity = this;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    private static MainActivity mainActivity;

    /**
     * 将bitmap转为Base64字符串
     *
     * @param bitmap
     * @return base64字符串
     */
    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
        byte[] bytes = outputStream.toByteArray();
        //Base64算法加密，当字符串过长（一般超过76）时会自动在中间加一个换行符，字符串最后也会加一个换行符。
        // 导致和其他模块对接时结果不一致。所以不能用默认Base64.DEFAULT，而是Base64.NO_WRAP不换行
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    @SuppressLint("Range")
    public void startCropActivity(Uri uri) {
        UCrop.of(uri, mDestinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(92, 92)
                .withTargetActivity(PortraitCropActivity.class)
                .start(this);
    }

    /**
     * 处理剪切后的返回值
     *
     * @param result
     */
    private void handleCropResult(Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            Bitmap bmp;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                String aimgByte = bitmapToString(bmp);
                String z = "androidGetBase" + "(\"" + aimgByte + "\")";
                ConchJNI.RunJS(z);
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } else {
            Toast.makeText(this, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }

    }


    public void copy(final String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    ClipboardManager mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData copyData = ClipData.newPlainText("复制成功", content);
                    mClipboardManager.setPrimaryClip(copyData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionYes(100)
    private void getSaveYes() {
        Boolean isSuccess = Util.generateImage(MainActivity.this, mQrCodePic);
        if (isSuccess) {
            ConchJNI.RunJS("isSave('1')");
        } else {
            ConchJNI.RunJS("isSave('0')");
        }
    }

    @PermissionNo(100)
    private void getSaveNo() {
        ConchJNI.RunJS("isSave('0')");
    }

}
