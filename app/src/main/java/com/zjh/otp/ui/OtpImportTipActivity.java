package com.zjh.otp.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.dommy.qrcode.util.Constant;
import com.dommy.qrcode.util.ScanUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zjh.otp.app.R;
import com.zjh.otp.app.databinding.ActivityOtpImportTipBinding;
import com.zjh.otp.base.BaseActivity;
import com.zjh.otp.manager.OTPManager;
import com.zjh.otp.util.CommonConstant;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.zjh.otp.TOTP;
import com.zjh.otp.TOTPEntity;
import com.zjh.otp.util.ToastUtils;

public class OtpImportTipActivity extends BaseActivity {

    private List<TOTPEntity> resultList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.background);
        ActivityOtpImportTipBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp_import_tip);
        mBinding.otpImportTipActionbar.btnBack.setOnClickListener(v -> finish());
        mBinding.otpImportTipActionbar.textTitle.setText(getString(R.string.import_account));
        mBinding.otpImportTipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanUtil.startQrCode(OtpImportTipActivity.this, CommonConstant.REQUEST_PERMISSION_CAMERA,
                        CommonConstant.REQUEST_CODE_QR, Constant.PAGE_ADD_ACCOUNT, false, getString(R.string.scan_add_otp_tip));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CommonConstant.REQUEST_PERMISSION_CAMERA) {// 摄像头权限申请
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 获得授权
                ScanUtil.startQrCode(OtpImportTipActivity.this, CommonConstant.REQUEST_PERMISSION_CAMERA,
                        CommonConstant.REQUEST_CODE_QR, Constant.PAGE_ADD_ACCOUNT, false, getString(R.string.scan_add_otp_tip));
            } else {
                // 被禁止授权
                ToastUtils.show(this, getString(com.zjh.scanner.R.string.camera_permission_tips));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
                importAccount(scanResult);
            }
        }
    }

    private void importAccount(String scanResult) {
        Gson gson = new Gson();
        try {
            Type type = new TypeToken<ArrayList<TOTPEntity>>() {
            }.getType();
            resultList = gson.fromJson(scanResult, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (resultList == null || resultList.isEmpty()) {
            ToastUtils.showTopError(OtpImportTipActivity.this, getString(R.string.import_account_error));
            return;
        }
        new Thread(() -> {
            int importCount = 0;
            int existCount = 0;
            List<String> accountList = new ArrayList<>();
            for (TOTPEntity entity : resultList) {
                TOTPEntity data = TOTP.getTotp(this, entity.getAccountId());
                if (data != null) {
                    existCount++;
                    continue;
                }
                TOTP.addTotp(this, entity);
                accountList.add(entity.getAccount());
                importCount++;
            }
            OTPManager.getInstance().addImportOtpHistory(this, accountList);
            if (existCount == 0) {
                showToast(getString(R.string.import_account_success, String.valueOf(importCount)));
            } else {
                showToast(getString(R.string.import_account_success2, String.valueOf(importCount), String.valueOf(existCount)));
            }
        }).start();
    }

    private void showToast(String text) {
        runOnUiThread(() -> {
            ToastUtils.showTop(OtpImportTipActivity.this, text);
            finish();
        });
    }

}
