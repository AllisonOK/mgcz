package com.mgcz.mgtwo;

import android.*;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

/**
 * 拨打电话
 * Created by admin on 2018/4/11.
 */

public class CallPhoneAct extends Activity {

    private String phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_call_phone);

        phone = getIntent().getStringExtra("phoneNumber");
        TextView phoneText = (TextView) findViewById(R.id.phone);
        phoneText.setText(phone);

        findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(CallPhoneAct.this).requestCode(99)
                        .permission(android.Manifest.permission.CALL_PHONE)
                        .send();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 这个Fragment所在的Activity的onRequestPermissionsResult()如果重写了，不能删除super.onRes...
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


    @PermissionYes(99)

    private void getLocationYes() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
        finish();
    }



    @PermissionNo(99)

    private void getLocationNo() {
        Toast.makeText(CallPhoneAct.this, "需要打开手机对该应用的电话权限", Toast.LENGTH_SHORT).show();
        finish();
    }
}
