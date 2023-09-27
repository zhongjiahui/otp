package com.zjh.otp.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.zjh.otp.app.R;
import com.zjh.otp.app.databinding.ActivityOtpExportSelectBinding;
import com.zjh.otp.base.BaseActivity;
import com.zjh.otp.bean.OtpAccountBean;
import com.zjh.otp.util.CommonConstant;
import com.zjh.otp.util.PageRouter;

import java.util.ArrayList;
import java.util.List;

import com.zjh.otp.TOTP;
import com.zjh.otp.TOTPEntity;

public class OtpExportSelectActivity extends BaseActivity implements OtpSelectAdapter.OnItemCheckListener {

    private ActivityOtpExportSelectBinding mBinding;
    private List<OtpAccountBean> mDataList;
    private OtpSelectAdapter adapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.background);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp_export_select);
        mBinding.otpExportSelectActionbar.btnBack.setOnClickListener(v -> finish());
        mBinding.otpExportSelectActionbar.textTitle.setText(getString(R.string.export_account));
        mBinding.otpExportSelectBtn.setOnClickListener(v -> goToNext());
        mBinding.otpExportSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataList != null && !mDataList.isEmpty()) {
                    for (OtpAccountBean otpAccountBean : mDataList) {
                        otpAccountBean.setHasSelect(mBinding.otpExportSelectAll.isChecked());
                    }
                }
                adapter.notifyDataSetChanged();
                refreshSelectCount(false);
            }
        });
        adapter = new OtpSelectAdapter(this);
        mBinding.otpExportSelectList.setAdapter(adapter);
        mBinding.otpExportSelectList.setLayoutManager(new LinearLayoutManager(this));
        setButtonEnable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOtpData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonConstant.REQUEST_OTP_EXPORT && resultCode == CommonConstant.RESULT_OTP_EXPORT) {
            setResult(CommonConstant.RESULT_OTP_EXPORT);
            finish();
        }
    }

    private void getOtpData() {
        if (mDataList != null) {
            return;
        }
        new Thread(() -> {
            List<TOTPEntity> totpList = TOTP.getTotpList(OtpExportSelectActivity.this);
            mDataList = new ArrayList<>();
            for (TOTPEntity entity : totpList) {
                mDataList.add(new OtpAccountBean(entity));
            }
            runOnUiThread(() -> {
                adapter.refreshData(mDataList);
                refreshSelectCount(true);
            });
        }).start();
    }

    private void refreshSelectCount(boolean refreshCheck) {
        if (mDataList == null) {
            if (refreshCheck) {
                mBinding.otpExportSelectAll.setChecked(false);
            }
            mBinding.otpExportSelectCount.setText(getString(R.string.has_check, "0", "0"));
            setButtonEnable(false);
            return;
        }

        int selectCount = 0;
        for (OtpAccountBean otpAccountBean : mDataList) {
            if (otpAccountBean.isHasSelect()) {
                selectCount++;
            }
        }
        if (refreshCheck) {
            mBinding.otpExportSelectAll.setChecked(selectCount == mDataList.size());
        }
        mBinding.otpExportSelectCount.setText(getString(R.string.has_check,
                String.valueOf(selectCount),
                String.valueOf(mDataList.size())));
        setButtonEnable(selectCount != 0);
    }

    private void setButtonEnable(boolean enable) {
        mBinding.otpExportSelectBtn.setEnabled(enable);
        mBinding.otpExportSelectBtn.setClickable(enable);
    }

    private void goToNext() {
        String data = "";
        if (mDataList != null) {
            List<TOTPEntity> resultList = new ArrayList<>();
            for (OtpAccountBean otpAccountBean : mDataList) {
                if (otpAccountBean.isHasSelect()) {
                    TOTPEntity totpEntity = otpAccountBean.getTotpEntity();
                    totpEntity.setPlatform("Android");
                    resultList.add(totpEntity);
                }
            }
            Gson gson = new Gson();
            data = gson.toJson(resultList);
        }
        PageRouter.navigateOtpExportQrCode(OtpExportSelectActivity.this, data);
    }

    @Override
    public void onItemCheck() {
        refreshSelectCount(true);
    }
}
