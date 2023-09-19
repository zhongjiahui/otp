package com.dommy.qrcode.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import cn.authing.scanner.R;

public class ToastUtils {

    public static void controlToastTime(final Toast toast, int duration) {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, duration);
    }

    public static void show(Context context, String s) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        TextView tv_msg = (TextView) view.findViewById(R.id.tvToast);
        tv_msg.setText(s);
        Toast toast = Toast.makeText(context, s, Toast.LENGTH_LONG);
        //controlToastTime(toast, 500);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.show();
    }

}
