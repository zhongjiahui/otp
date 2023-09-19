package cn.authing.otp.ui;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import cn.authing.otp.Base32;
import cn.authing.otp.R;
import cn.authing.otp.TOTP;
import cn.authing.otp.TOTPBindResult;
import cn.authing.otp.util.ToastUtils;

public class InputKeyActivity extends AppCompatActivity {

    private String errKeyText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        setContentView(R.layout.activity_input_key);
        initView();
    }

    private void initActionBar() {
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.hide();
        }
        Window window = getWindow();
        UiModeManager uiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        if (uiModeManager.getNightMode() != UiModeManager.MODE_NIGHT_YES) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.otp_white));
    }

    private void initView() {
        errKeyText = getString(R.string.key_invalid);
        EditText accountEdit = findViewById(R.id.edit_account);
        accountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    accountEdit.setBackgroundResource(R.drawable.background_button_outline);
                } else {
                    accountEdit.setBackgroundResource(R.drawable.background_edit_text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        EditText keyEdit = findViewById(R.id.edit_key);
        TextView keyTip = findViewById(R.id.key_tip);
        TextView errorKeyTip = findViewById(R.id.key_error_tip);
        keyEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    keyTip.setVisibility(View.VISIBLE);
                    errorKeyTip.setVisibility(View.GONE);
                    keyEdit.setBackgroundResource(R.drawable.background_edit_text);
                    //return;
                }

//                if (checkSecret(s.toString())) {
//                    keyTip.setVisibility(View.VISIBLE);
//                    errorKeyTip.setVisibility(View.GONE);
//                    keyEdit.setBackgroundResource(R.drawable.background_edit_text);
//                } else {
//                    keyTip.setVisibility(View.GONE);
//                    errorKeyTip.setVisibility(View.VISIBLE);
//                    errorKeyTip.setText(errKeyText);
//                    keyEdit.setBackgroundResource(R.drawable.background_button_outline);
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_add_account).setOnClickListener(v -> {
            String account = accountEdit.getText().toString().trim();
            boolean accountVisible = true;
            if (TextUtils.isEmpty(account)) {
                accountEdit.setBackgroundResource(R.drawable.background_button_outline);
                accountVisible = false;
            }
            String secret = keyEdit.getText().toString().trim();
            boolean secretVisible = true;
            if (checkSecret(secret)) {
                keyTip.setVisibility(View.VISIBLE);
                errorKeyTip.setVisibility(View.GONE);
                keyEdit.setBackgroundResource(R.drawable.background_edit_text);
            } else {
                keyTip.setVisibility(View.GONE);
                errorKeyTip.setVisibility(View.VISIBLE);
                errorKeyTip.setText(errKeyText);
                keyEdit.setBackgroundResource(R.drawable.background_button_outline);
                secretVisible = false;
            }

            if (!accountVisible || !secretVisible) {
                return;
            }
            String oauthStr = "otpauth://totp/" + account + "?secret=" + secret;
            bindAccount(oauthStr);
        });
    }

    private void bindAccount(String data) {
        new Thread(() -> {
            TOTPBindResult bindResult = TOTP.bind(InputKeyActivity.this, data);
            runOnUiThread(() -> {
                if (bindResult.getCode() == TOTPBindResult.BIND_SUCCESS) {
                    Intent intent = new Intent();
                    String account = "";
                    if (bindResult.getNewTotp() != null){
                        account = bindResult.getNewTotp().getAccount();
                    }
                    intent.putExtra("account", account);
                    setResult(1008, intent);
                    finish();
                } else {
                    ToastUtils.show(InputKeyActivity.this, bindResult.getMessage());
                }
            });
        }).start();
    }

    private boolean checkSecret(String secret) {
        try {
            Base32.decode(secret);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if ("non canonical Base32 string length".equals(e.getMessage())) {
                errKeyText = getString(R.string.key_invalid_length);
            } else {
                errKeyText = getString(R.string.key_invalid);
            }
        }
        return false;
    }
}
