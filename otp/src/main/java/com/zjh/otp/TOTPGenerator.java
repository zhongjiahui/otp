package com.zjh.otp;

import java.nio.ByteBuffer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TOTPGenerator {

    public static final int TIME_STEP = 30;
    public static final int CODE_DIGITS = 6;
    public static final String ALGORITHM = "SHA1";

    public static String getQRCodeStr(String user, String secret) {
        String format = "otpauth://totp/%s?secret=%s";
        return String.format(format, user, secret);
    }

    public static String generateTOTP(String secret) {
        return generateTOTP(secret, getCurrentInterval(), CODE_DIGITS, ALGORITHM);
    }

    public static String generateTOTP(String secret, int digits) {
        return generateTOTP(secret, getCurrentInterval(), digits, ALGORITHM);
    }

    public static String generateTOTP(String secret, int period, int digits, String algorithm) {
        if (period == 0){
            period = TIME_STEP;
        }
        if (digits == 0){
            period = CODE_DIGITS;
        }
        if (algorithm == null){
            algorithm = ALGORITHM;
        }
        return generateTOTP(secret, getCurrentInterval(period), digits, algorithm);
    }

    public static boolean verify(String secret, String code) {
        return verify(secret, code, CODE_DIGITS, ALGORITHM);
    }

    public static boolean verify(String secret, String code, int digits, String algorithm) {
        long currentInterval = getCurrentInterval();
        for (int i = 0; i <= 1; i++) {
            String tmpCode = generateTOTP(secret, currentInterval - i, digits, algorithm);
            if (tmpCode.equals(code)) {
                return true;
            }
        }
        return false;
    }

    public static int getRemainingSeconds() {
        return TIME_STEP - (int) (System.currentTimeMillis() / 1000 % TIME_STEP);
    }

    public static int getRemainingMilliSeconds() {
        return TIME_STEP * 1000 - (int) (System.currentTimeMillis() % (TIME_STEP * 1000));
    }

    private static String generateTOTP(String secret, long currentInterval, int digits, String algorithm) {
        if (digits < 1 || digits > 18) {
            throw new UnsupportedOperationException("不支持" + digits + "位数的动态口令");
        }
        byte[] content = ByteBuffer.allocate(8).putLong(currentInterval).array();
        byte[] hash = hmacsha("Hmac"+algorithm, content, secret);
        if(hash == null){
            return "";
        }
        int offset = hash[hash.length - 1] & 0xf;
        int binary =
                ((hash[offset] & 0x7f) << 24) |
                        ((hash[offset + 1] & 0xff) << 16) |
                        ((hash[offset + 2] & 0xff) << 8) |
                        (hash[offset + 3] & 0xff);
        long digitsPower = Long.parseLong(rightPadding("1", digits + 1));
        long code = binary % digitsPower;
        return leftPadding(Long.toString(code), digits);
    }

    private static long getCurrentInterval() {
        return System.currentTimeMillis() / 1000 / TIME_STEP;
    }

    private static long getCurrentInterval(int period) {
        return System.currentTimeMillis() / 1000 / period;
    }

    private static String leftPadding(String value, int length) {
        while (value.length() < length) {
            value = "0" + value;
        }
        return value;
    }

    private static String rightPadding(String value, int length) {
        StringBuilder valueBuilder = new StringBuilder(value);
        while (valueBuilder.length() < length) {
            valueBuilder.append("0");
        }
        value = valueBuilder.toString();
        return value;
    }

    private static byte[] hmacsha(String crypto, byte[] content, String key) {
        try {
            byte[] byteKey = Base32.decode(key);
            Mac hmac = Mac.getInstance(crypto);
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, crypto);
            hmac.init(keySpec);
            return hmac.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
