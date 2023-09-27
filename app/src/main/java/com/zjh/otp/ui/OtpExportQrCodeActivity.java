package com.zjh.otp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.dommy.qrcode.util.BitmapUtil;
import com.google.zxing.WriterException;
import com.zjh.otp.app.R;
import com.zjh.otp.app.databinding.ActivityOtpExportQrcodeBinding;
import com.zjh.otp.base.BaseActivity;
import com.zjh.otp.util.CommonConstant;
import com.zjh.otp.util.Utils;


public class OtpExportQrCodeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.background);
        ActivityOtpExportQrcodeBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp_export_qrcode);
        mBinding.otpExportQrcodeActionbar.btnBack.setOnClickListener(v -> finish());
        mBinding.otpExportQrcodeActionbar.textTitle.setText(getString(R.string.export_account));
        mBinding.otpExportQrcodeBtn.setOnClickListener(v -> {
            setResult(CommonConstant.RESULT_OTP_EXPORT);
            finish();
        });

        String data = "";
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(CommonConstant.OTP_DATA)) {
            data = intent.getStringExtra(CommonConstant.OTP_DATA);
        }
        try {
            Bitmap bitmap = BitmapUtil.createCodeBitmap(data, (int) Utils.dip2px(this, 176f), (int) Utils.dip2px(this, 176f));
            mBinding.otpExportQrcodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }

}
