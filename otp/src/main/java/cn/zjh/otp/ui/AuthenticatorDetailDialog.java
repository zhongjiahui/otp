package cn.zjh.otp.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import cn.zjh.otp.R;
import cn.zjh.otp.TOTP;
import cn.zjh.otp.TOTPEntity;
import cn.zjh.otp.util.ToastUtils;


public class AuthenticatorDetailDialog extends Dialog {

    private Context mContext;
    private EditText mAccount;
    private EditText mApplication;
    private TextView mDigits;
    private TextView mInterval;
    private TextView mAlgorithm;
    private TextView mIssuer;

    private TOTPEntity mData;

    public AuthenticatorDetailDialog(Context context) {
        super(context);
        initView(context);
    }

    public AuthenticatorDetailDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    protected AuthenticatorDetailDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = d.getHeight();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
    }

    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.activity_authenticator_detail, null);
        setContentView(view);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        mAccount = view.findViewById(R.id.text_account);
        mApplication = view.findViewById(R.id.text_application);
        mDigits = view.findViewById(R.id.text_digits);
        mInterval = view.findViewById(R.id.text_interval);
        mAlgorithm = view.findViewById(R.id.text_algorithm);
        mIssuer = view.findViewById(R.id.text_issuer);
        findViewById(R.id.btn_back).setOnClickListener(v -> dismiss());
        findViewById(R.id.text_save).setOnClickListener(v -> save());
    }

    private void save() {
        if (null == mData) {
            return;
        }
        String editAccount = mAccount.getText().toString().trim();
        if (TextUtils.isEmpty(editAccount)) {
            ToastUtils.show(mContext, mContext.getString(R.string.account_ame_cannot_be_empty));
            return;
        }
        String editApplication = mApplication.getText().toString().trim();
        if (TextUtils.isEmpty(editApplication)) {
            ToastUtils.show(mContext, mContext.getString(R.string.application_name_cannot_be_empty));
            return;
        }
        if (!TextUtils.isEmpty(mData.getAccount()) && editAccount.equals(mData.getAccount()) &&
                TextUtils.isEmpty(mData.getAppName()) && editApplication.equals(mData.getAppName())) {
            return;
        }
        mData.setAccount(editAccount);
        mData.setAppName(editApplication);
        new Thread(() -> {
            TOTP.updateTotp(mContext, mData);
            dismiss();
        }).start();
    }

    @SuppressLint("DefaultLocale")
    public void setData(TOTPEntity data) {
        if (null == data) {
            return;
        }
        this.mData = data;
        mAccount.setText(data.getAccount());
        mAccount.clearFocus();
        mApplication.setText(data.getAppName());
        mApplication.clearFocus();
        mDigits.setText(String.valueOf(data.getDigits()));
        mInterval.setText(String.format("%ds", data.getInterval()));
        mAlgorithm.setText(data.getAlgorithm());
        mIssuer.setText(data.getIssuer());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        hideKeyboard();
        return super.onTouchEvent(event);
    }

    public void hideKeyboard() {
        if (mContext == null){
            return;
        }
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm!=null&&getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null){
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
