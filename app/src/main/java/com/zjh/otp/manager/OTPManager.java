package com.zjh.otp.manager;

import android.content.Context;

import com.zjh.otp.R;
import com.zjh.otp.database.OtpDataBase;
import com.zjh.otp.database.OtpHistoryEntity;
import com.zjh.otp.util.Utils;

import java.util.List;

public class OTPManager {

    private OTPManager() {

    }

    private static final class OTPInstanceHolder {
        static final OTPManager mInstance = new OTPManager();
    }

    public static OTPManager getInstance() {
        return OTPInstanceHolder.mInstance;
    }

    /**
     * 添加操作历史
     */
    public void addImportOtpHistory(Context context, List<String> accountList) {
        if (accountList == null || accountList.isEmpty()) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < accountList.size(); i++) {
            String account = accountList.get(i);
            if (account == null) {
                continue;
            }
            sb.append("「");
            sb.append(account);
            sb.append("」");
            if (i != accountList.size() - 1) {
                sb.append("、");
            }
        }
        OtpHistoryEntity entity = new OtpHistoryEntity();
        entity.setDescription(context.getString(R.string.import_account_history, sb.toString(), String.valueOf(accountList.size())));
        entity.setTime(Utils.getTimeString());
        new Thread(() -> OtpDataBase.getInstance(context).otpHistoryDao().insertHistory(entity)).start();
    }

    public void addDeleteOtpHistory(Context context, String account) {
        if (account == null || account.isEmpty()) {
            return;
        }
        new Thread(() -> {
            OtpHistoryEntity entity = new OtpHistoryEntity();
            entity.setDescription(context.getString(R.string.delete_account_history, "「" + account + "」"));
            entity.setTime(Utils.getTimeString());
            OtpDataBase.getInstance(context).otpHistoryDao().insertHistory(entity);
        }).start();
    }
}
