package com.zjh.otp;

public class TOTPEntity {
    public String scheme;
    public String host;
    public String accountId;

    private String uuid;
    private String account;
    private String appname;
    private String secret;
    private int interval;
    private int digits;
    private String algorithm;
    private String issuer;
    private String totpCode;
    private String platform;

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAppName() {
        return appname;
    }

    public void setAppName(String appname) {
        this.appname = appname;
    }

    public String getAccountDetail() {
        if (appname == null){
            return account;
        }
        return appname + "  (" + account + ")" ;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getDigits() {
        return digits;
    }

    public void setDigits(int digits) {
        this.digits = digits;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getTotpCode() {
        totpCode = TOTPGenerator.generateTOTP(secret, interval, digits, algorithm);
        return totpCode;
    }

    public void setTotpCode(String totpCode) {
        this.totpCode = totpCode;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
