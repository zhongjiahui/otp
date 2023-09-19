package com.zjh.otp.util;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class UIUtils {

    private static final String TAG = UIUtils.class.getSimpleName();

    public static RotateAnimation getRotateAnimation(int fromDegrees, int toDegrees) {
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(100);
        animation.setFillAfter(true);
        return animation;
    }
}
