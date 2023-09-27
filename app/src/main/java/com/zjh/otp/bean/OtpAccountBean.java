package com.zjh.otp.bean;

import com.zjh.otp.TOTPEntity;

public class OtpAccountBean {

    private boolean hasSelect;
    private TOTPEntity totpEntity;

    public OtpAccountBean(TOTPEntity totpEntity) {
        this.totpEntity = totpEntity;
    }

    public boolean isHasSelect() {
        return hasSelect;
    }

    public void setHasSelect(boolean hasSelect) {
        this.hasSelect = hasSelect;
    }

    public TOTPEntity getTotpEntity() {
        return totpEntity;
    }

    public void setTotpEntity(TOTPEntity totpEntity) {
        this.totpEntity = totpEntity;
    }
}
