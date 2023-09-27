package com.zjh.otp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import com.dommy.qrcode.util.Constant;
import com.dommy.qrcode.util.ScanUtil;
import com.zjh.otp.app.databinding.ActivityMainBinding;
import com.zjh.otp.base.BaseActivity;
import com.zjh.otp.manager.OTPManager;
import com.zjh.otp.util.CommonConstant;
import com.zjh.otp.util.PageRouter;
import com.zjh.otp.util.UIUtils;
import com.zjh.otp.util.Utils;

import java.util.ArrayList;
import java.util.List;

import com.zjh.otp.app.R;
import com.zjh.otp.ui.AuthenticatorFragment;
import com.zjh.otp.util.ToastUtils;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding mBinding;

    private AuthenticatorFragment authenticatorFragment;

    private PopupWindow addPopupWindow;
    private PopupWindow morePopupWindow;

    private boolean hasOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initBar();
        initFragment();
    }

    private void initBar(){
        mBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.imgAdd.startAnimation(UIUtils.getRotateAnimation(0, 45));
                showAddPopupWindow();
            }
        });
        mBinding.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.imgMore.startAnimation(UIUtils.getRotateAnimation(0, 90));
                showMorePopupWindow();
            }
        });
        mBinding.imgEditMode.setOnClickListener(v -> {
            showEditMode(false);
        });
    }

    private void initFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        authenticatorFragment = new AuthenticatorFragment();
        fragmentTransaction.add(R.id.authenticator, authenticatorFragment);
        fragmentTransaction.show(authenticatorFragment);
        fragmentTransaction.commit();
    }


    private void showAddPopupWindow() {
        if (addPopupWindow == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.layout_popup_add, null, false);
            addPopupWindow = new PopupWindow(contentView,
                    ViewGroup.LayoutParams.MATCH_PARENT, Utils.getScreenHeight(this) - mBinding.actionBar.getBottom(), true);
            ImageView point = contentView.findViewById(R.id.img_point);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) point.getLayoutParams();
            params.setMarginStart(mBinding.imgAdd.getRight() - (mBinding.imgAdd.getWidth() / 2) - Utils.dip2px(this, 8) - (point.getWidth() / 2));
            contentView.findViewById(R.id.layout_scan).setOnClickListener(v -> {
                ScanUtil.startQrCode(this, CommonConstant.REQUEST_PERMISSION_CAMERA,
                        CommonConstant.REQUEST_CODE_QR, Constant.PAGE_ADD_ACCOUNT, false);
                addPopupWindow.dismiss();
            });
            contentView.findViewById(R.id.layout_input).setOnClickListener(v -> {
                PageRouter.navigateOtpInput(MainActivity.this);
                addPopupWindow.dismiss();
            });
            contentView.setOnClickListener(v -> addPopupWindow.dismiss());
            addPopupWindow.setOnDismissListener(() -> {
                mBinding.imgAdd.startAnimation(UIUtils.getRotateAnimation(90, 0));
                mBinding.imgAdd.clearAnimation();
            });
            addPopupWindow.setBackgroundDrawable(ResourcesCompat.getDrawable(
                    getResources(), R.drawable.background_popupwindow, null));
            addPopupWindow.setTouchable(true);
            addPopupWindow.setOutsideTouchable(true);
            addPopupWindow.setFocusable(true);
        }
        if (addPopupWindow.isShowing()) {
            return;
        }
        addPopupWindow.showAsDropDown(mBinding.actionBar);
    }


    private void showMorePopupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_popup_more, null, false);
        morePopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, Utils.getScreenHeight(this) - mBinding.actionBar.getBottom(), true);
        ImageView point = contentView.findViewById(R.id.img_point);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) point.getLayoutParams();
        params.setMarginStart(mBinding.imgMore.getRight() - (mBinding.imgMore.getWidth() / 2) - Utils.dip2px(this, 8) - (point.getWidth() / 2));
        contentView.findViewById(R.id.layout_edit).setVisibility(hasOTP ? View.VISIBLE : View.GONE);
        contentView.findViewById(R.id.layout_edit).setOnClickListener(v -> {
            morePopupWindow.dismiss();
            showEditMode(true);
        });
        contentView.findViewById(R.id.layout_import).setOnClickListener(v -> {
            PageRouter.navigateOtpImportTip(MainActivity.this);
            morePopupWindow.dismiss();
        });
        contentView.findViewById(R.id.layout_export).setVisibility(hasOTP ? View.VISIBLE : View.GONE);
        contentView.findViewById(R.id.layout_export).setOnClickListener(v -> {
            PageRouter.navigateOtpExportTip(MainActivity.this);
            morePopupWindow.dismiss();
        });
        contentView.findViewById(R.id.layout_history).setVisibility(hasOTP ? View.VISIBLE : View.GONE);
        contentView.findViewById(R.id.layout_history).setOnClickListener(v -> {
            PageRouter.navigateOtpHistory(MainActivity.this);
            morePopupWindow.dismiss();
        });
        contentView.setOnClickListener(v -> morePopupWindow.dismiss());
        morePopupWindow.setOnDismissListener(() -> {
            mBinding.imgMore.startAnimation(UIUtils.getRotateAnimation(45, 0));
            mBinding.imgMore.clearAnimation();
        });
        morePopupWindow.setBackgroundDrawable(ResourcesCompat.getDrawable(
                getResources(), R.drawable.background_popupwindow, null));
        morePopupWindow.setTouchable(true);
        morePopupWindow.setOutsideTouchable(true);
        morePopupWindow.setFocusable(true);
        if (morePopupWindow.isShowing()) {
            return;
        }
        morePopupWindow.showAsDropDown(mBinding.actionBar);
    }


    private void showEditMode(boolean isEditMode) {
        mBinding.imgEditMode.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
        mBinding.imgAdd.setVisibility(isEditMode ? View.GONE : View.VISIBLE);
        mBinding.imgMore.setVisibility(isEditMode ? View.GONE : View.VISIBLE);
        if (authenticatorFragment == null) {
            return;
        }
        authenticatorFragment.setCurrentMode(isEditMode ? AuthenticatorFragment.MODE_EDIT : AuthenticatorFragment.MODE_DEFAULT);
        authenticatorFragment.setListener(totpEntity -> {
            OTPManager.getInstance().addDeleteOtpHistory(MainActivity.this, totpEntity.getAccount());
            refreshView();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        new Thread(() -> {
            List<TOTPEntity> otpList = TOTP.getTotpList(this);
            runOnUiThread(() -> {
                if (otpList.isEmpty()) {
                    hasOTP = false;
                    mBinding.imgEditMode.setVisibility(View.GONE);
                    mBinding.imgAdd.setVisibility(View.VISIBLE);
                    mBinding.imgMore.setVisibility(View.VISIBLE);
                    setWhiteTheme();
                } else {
                    hasOTP = true;
                    setLightTheme();
                    if (authenticatorFragment != null) {
                        int currentMode = authenticatorFragment.getCurrentMode();
                        mBinding.imgEditMode.setVisibility(currentMode == AuthenticatorFragment.MODE_DEFAULT ? View.GONE : View.VISIBLE);
                        mBinding.imgAdd.setVisibility(currentMode == AuthenticatorFragment.MODE_DEFAULT ? View.VISIBLE : View.GONE);
                        mBinding.imgMore.setVisibility(currentMode == AuthenticatorFragment.MODE_DEFAULT ? View.VISIBLE : View.GONE);
                    }
                }
            });
        }).start();
    }

    private void setWhiteTheme() {
        setStatusBarColor(R.color.white);
        mBinding.actionBar.setBackgroundResource(R.color.white);
    }

    private void setLightTheme() {
        setStatusBarColor(R.color.background);
        mBinding.actionBar.setBackgroundResource(R.color.background);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
                bindOTPAccount(scanResult);
            }

            if (requestCode == CommonConstant.REQUEST_OTP_INPUT && resultCode == CommonConstant.RESULT_OTP_INPUT) {
                String account = data.getStringExtra("account");
                if (account != null) {
                    addToOtpHistory(account);
                }
            }
        }
    }

    private void bindOTPAccount(String data) {
        new Thread(() -> {
            TOTPBindResult bindResult = TOTP.bind(MainActivity.this, data);
            runOnUiThread(() -> {
                if (authenticatorFragment == null) {
                    return;
                }
                authenticatorFragment.setListener(totpEntity -> {
                    OTPManager.getInstance().addDeleteOtpHistory(MainActivity.this, totpEntity.getAccount());
                    refreshView();
                });
                if (bindResult.getCode() == TOTPBindResult.BIND_SUCCESS) {
                    setLightTheme();
                    authenticatorFragment.refreshTotpData();
                    // 添加到操作历史
                    if (bindResult.getNewTotp() != null) {
                        addToOtpHistory(bindResult.getNewTotp().getAccount());
                    }
                } else if (bindResult.getCode() == TOTPBindResult.BIND_FAILURE) {
                    ToastUtils.show(this, bindResult.getMessage());
                } else if (bindResult.getCode() == TOTPBindResult.UPDATED_ACCOUNT) {
                    ToastUtils.show(this, bindResult.getMessage());
                    authenticatorFragment.refreshTotpData();
                }
            });
        }).start();
    }

    private void addToOtpHistory(String account) {
        List<String> accountList = new ArrayList<>();
        accountList.add(account);
        OTPManager.getInstance().addImportOtpHistory(this, accountList);
    }
}