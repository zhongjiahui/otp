package cn.zjh.otp.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import cn.zjh.otp.R;

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


    public static void showTop(Context context, String text) {
        showTop(context, text, 0);
    }

    public static void showTop(Context context, String text, int duration) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toast_top, null);
        TextView tv_msg = view.findViewById(R.id.toast_text);
        tv_msg.setText(text);
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        if (duration != 0){
            controlToastTime(toast, duration);
        }
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setView(view);
        toast.show();
    }

    public static void showTopError(Context context, String text) {
        showTopError(context, text, 0);
    }

    public static void showTopError(Context context, String text, int duration) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toast_top, null);
        view.setBackgroundResource(R.drawable.background_toast_error);
        TextView tv_msg = view.findViewById(R.id.toast_text);
        tv_msg.setText(text);
        tv_msg.setTextColor(context.getColor(R.color.text_yellow));
        ImageView tv_img = view.findViewById(R.id.toast_image);
        tv_img.setImageResource(R.drawable.ic_prompt);
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        if (duration != 0){
            controlToastTime(toast, duration);
        }
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setView(view);
        toast.show();
    }

}
