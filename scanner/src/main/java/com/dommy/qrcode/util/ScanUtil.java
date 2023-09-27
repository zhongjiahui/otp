package com.dommy.qrcode.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.activity.CaptureActivity;

public class ScanUtil {

    public static void startQrCode(Activity activity, int permissionRequestCode, int requestCode,
                                   int pageType, boolean debugMode) {
        startQrCode(activity, permissionRequestCode, requestCode, pageType, debugMode, "");
    }

    public static void startQrCode(Activity activity, int permissionRequestCode, int requestCode,
                                   int pageType, boolean debugMode, String tips) {
        if (null == activity) {
            return;
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            boolean hasRequest = SharedPreferencesUtils.getBoolean(activity, SharedPreferencesUtils.SP_HAS_REQUEST_CARME_PERMISSION);
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA) && hasRequest) {
                ToastUtils.show(activity, activity.getString(com.zjh.scanner.R.string.camera_permission_tips));
                return;
            }
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, permissionRequestCode);
            SharedPreferencesUtils.putBoolean(activity, SharedPreferencesUtils.SP_HAS_REQUEST_CARME_PERMISSION, true);
            return;
        }

        Intent intent = new Intent(activity, CaptureActivity.class);
        intent.putExtra("pageType", pageType);
        intent.putExtra("debugMode", debugMode);
        intent.putExtra("tips", tips);
        activity.startActivityForResult(intent, requestCode);
    }

}
