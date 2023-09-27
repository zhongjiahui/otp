package com.zjh.otp.ui;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zjh.otp.app.R;
import com.zjh.otp.app.databinding.ActivityOtpHistoryBinding;
import com.zjh.otp.base.BaseActivity;
import com.zjh.otp.database.OtpDataBase;
import com.zjh.otp.database.OtpHistoryEntity;

import java.util.Collections;
import java.util.List;

public class OtpHistoryActivity extends BaseActivity {

    private ActivityOtpHistoryBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.background);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp_history);
        mBinding.otpHistoryActionbar.btnBack.setOnClickListener(v -> finish());
        mBinding.otpHistoryActionbar.textTitle.setText(getString(R.string.operation_history));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        new Thread(() -> {
            List<OtpHistoryEntity> otpHistoryEntityList = OtpDataBase
                    .getInstance(OtpHistoryActivity.this).otpHistoryDao().getHistoryList();
            runOnUiThread(() -> {
                if (otpHistoryEntityList.isEmpty()) {
                    mBinding.otpHistoryEmpty.setVisibility(View.VISIBLE);
                    mBinding.otpHistoryList.setVisibility(View.GONE);
                    return;
                }
                mBinding.otpHistoryEmpty.setVisibility(View.GONE);
                mBinding.otpHistoryList.setVisibility(View.VISIBLE);
                OtpHistoryAdapter adapter = new OtpHistoryAdapter();
                mBinding.otpHistoryList.setAdapter(adapter);
                mBinding.otpHistoryList.setLayoutManager(new LinearLayoutManager(OtpHistoryActivity.this));
                Collections.reverse(otpHistoryEntityList);
                adapter.refreshData(otpHistoryEntityList);
            });
        }).start();
    }

}
