package com.zjh.otp;

import android.content.Context;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class TOTP {

    public synchronized static TOTPBindResult bind(Context context, String data) {
        TOTPBindResult result = new TOTPBindResult();
        try {
            URI uri = new URI(data);
            String path = uri.getPath();
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            Map<String, String> map = splitQuery(uri);
            if (map != null) {
                String secret = map.get("secret");
                String algorithm = map.get("algorithm");
                String digitsStr = map.get("digits");
                String periodStr = map.get("period");
                String issuer = map.get("issuer");
                int digits = TOTPGenerator.CODE_DIGITS;
                if (null != digitsStr){
                    try {
                        digits = Integer.parseInt(digitsStr);
                    } catch (Exception ignored) {}
                }
                int period = TOTPGenerator.TIME_STEP;
                if (null != periodStr){
                    try {
                        period = Integer.parseInt(periodStr);
                    } catch (Exception ignored) {}
                }
                if (algorithm == null){
                    algorithm = TOTPGenerator.ALGORITHM;
                }

                if(secret == null){
                    result.setMessage(context.getResources().getString(R.string.qr_exception));
                    return result;
                }

                TOTPEntity newTotp = new TOTPEntity();
                newTotp.setAccountId(path);
                if (path.contains(":")){
                    String[] pathArray = path.split(":");
                    newTotp.setAppName(pathArray.length > 0 ? pathArray[0] : "");
                    newTotp.setAccount(pathArray.length > 1 ? pathArray[1] : "");
                }else {
                    newTotp.setAccount(path);
                }
                newTotp.setSecret(secret);
                newTotp.setAlgorithm(algorithm);
                newTotp.setDigits(digits);
                newTotp.setInterval(period);
                newTotp.setIssuer(issuer);

                DatabaseHelper db = new DatabaseHelper(context);
                TOTPEntity historyTotp = db.getOTP(path);
                if (null != historyTotp && path.equals(historyTotp.getAccountId())){
                    if (secret.equals(historyTotp.getSecret())){
                        result.setCode(TOTPBindResult.BIND_FAILURE);
                        result.setMessage(context.getResources().
                                getString(R.string.the_account_is_bound, historyTotp.getAccountDetail()));
                    }else {
                        result.setCode(TOTPBindResult.UPDATED_ACCOUNT);
                        result.setMessage(context.getResources().
                                getString(R.string.the_account_is_updated, historyTotp.getAccountDetail()));
                        newTotp.setUuid(historyTotp.getUuid());
                        newTotp.setAppName(historyTotp.getAppName());
                        newTotp.setAccount(historyTotp.getAccount());
                        updateTotp(context, newTotp);
                    }
                    return result;
                }

                db.addOTP(newTotp);
                result.setCode(TOTPBindResult.BIND_SUCCESS);
                result.setNewTotp(newTotp);
            }else {
                result.setMessage(context.getResources().getString(R.string.qr_exception));
            }
        } catch (UnsupportedEncodingException | URISyntaxException e) {
            e.printStackTrace();
            result.setMessage(context.getResources().getString(R.string.qr_exception));
        }
        return result;
    }

    private static Map<String, String> splitQuery(URI url) throws UnsupportedEncodingException {
        final Map<String, String> queryPairs = new LinkedHashMap<>();
        final String query = url.getQuery();
        if (TextUtils.isEmpty(query)) {
            return null;
        }

        final String[] pairs = url.getQuery().split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            queryPairs.put(key, value);
        }
        return queryPairs;
    }

    public synchronized static void addTotp(Context context, TOTPEntity totp){
        DatabaseHelper db = new DatabaseHelper(context);
        db.addOTP(totp);
    }

    public synchronized static void updateTotp(Context context, TOTPEntity totp){
        DatabaseHelper db = new DatabaseHelper(context);
        db.updateOTP(totp);
    }

    public synchronized static List<TOTPEntity> getTotpList(Context context){
        DatabaseHelper db = new DatabaseHelper(context);
        return db.getOTPs();
    }

    public synchronized static TOTPEntity getTotp(Context context, String path){
        DatabaseHelper db = new DatabaseHelper(context);
        return db.getOTP(path);
    }

    public synchronized static void deleteTotp(Context context, TOTPEntity totp){
        DatabaseHelper db = new DatabaseHelper(context);
        db.deleteOTP(totp);
    }

    public synchronized static void deleteTotp(Context context, String path){
        DatabaseHelper db = new DatabaseHelper(context);
        db.deleteOTP(path);
    }

}
