package com.dommy.qrcode.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    private static final String SP_NAME = "authing_mobile";

    public static final String SP_HAS_REQUEST_CARME_PERMISSION = "has_request_carme_permission";
    public static final String SP_HAS_REQUEST_PHONE_PERMISSION = "has_request_phone_permission";

    public static void putBoolean(Context context, String key, Boolean value){
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static Boolean getBoolean(Context context, String key){
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }


}
