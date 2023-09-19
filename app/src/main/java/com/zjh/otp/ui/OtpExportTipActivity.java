package com.zjh.otp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.zjh.otp.R;
import com.zjh.otp.base.BaseActivity;
import com.zjh.otp.databinding.ActivityOtpExportTipBinding;
import com.zjh.otp.util.CommonConstant;
import com.zjh.otp.util.PageRouter;


public class OtpExportTipActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.background);
        ActivityOtpExportTipBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp_export_tip);
        mBinding.otpExportTipActionbar.btnBack.setOnClickListener(v -> finish());
        mBinding.otpExportTipActionbar.textTitle.setText(getString(R.string.export_account));
        mBinding.otpExportTipBtn.setOnClickListener(v -> PageRouter.navigateOtpExportSelect(OtpExportTipActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonConstant.REQUEST_OTP_EXPORT && resultCode == CommonConstant.RESULT_OTP_EXPORT) {
            finish();
        }
    }
}
