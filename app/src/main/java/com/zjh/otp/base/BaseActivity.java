package com.zjh.otp.base;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.zjh.otp.R;
import com.zjh.otp.util.Utils;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.hide();
        }
        setStatusBarColor(R.color.white);
    }

    public void setStatusBarColor(int colorResId){
        Utils.setStatusBarColor(this, colorResId);
    }
}
