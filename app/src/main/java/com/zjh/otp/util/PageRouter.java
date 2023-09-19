package com.zjh.otp.util;

import android.app.Activity;
import android.content.Intent;

import com.zjh.otp.ui.OtpExportQrCodeActivity;
import com.zjh.otp.ui.OtpExportSelectActivity;
import com.zjh.otp.ui.OtpExportTipActivity;
import com.zjh.otp.ui.OtpHistoryActivity;
import com.zjh.otp.ui.OtpImportTipActivity;

import cn.zjh.otp.ui.InputKeyActivity;

public class PageRouter {


    /**
     * OTP 手动输入密钥
     */
    public static void navigateOtpInput(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, InputKeyActivity.class);
        activity.startActivityForResult(intent, CommonConstant.REQUEST_OTP_INPUT);
    }

    /**
     * OTP 导入账号 - 提示页
     */
    public static void navigateOtpImportTip(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, OtpImportTipActivity.class);
        activity.startActivity(intent);
    }

    /**
     * OTP 导出账号 - 提示页
     */
    public static void navigateOtpExportTip(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, OtpExportTipActivity.class);
        activity.startActivity(intent);
    }

    /**
     * OTP 导出账号 - 选择页面
     */
    public static void navigateOtpExportSelect(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, OtpExportSelectActivity.class);
        activity.startActivityForResult(intent, CommonConstant.REQUEST_OTP_EXPORT);
    }

    /**
     * OTP 导出账号 - 二维码页面
     */
    public static void navigateOtpExportQrCode(Activity activity, String data) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, OtpExportQrCodeActivity.class);
        intent.putExtra(CommonConstant.OTP_DATA, data);
        activity.startActivityForResult(intent, CommonConstant.REQUEST_OTP_EXPORT);
    }

    /**
     * OTP 操作历史
     */
    public static void navigateOtpHistory(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, OtpHistoryActivity.class);
        activity.startActivity(intent);
    }


}
