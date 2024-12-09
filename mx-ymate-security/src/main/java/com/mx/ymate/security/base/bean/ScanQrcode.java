package com.mx.ymate.security.base.bean;

import com.mx.ymate.security.base.enums.ScanQrcodeEnum;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description: 扫码登录二维码信息
 */
public class ScanQrcode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录标识
     */
    private String loginKey;

    /**
     * 过期时间
     */
    private long expire;

    /**
     * 类型枚举
     */
    private ScanQrcodeEnum scanQrcodeEnum;

    /**
     * 登录用户信息
     */
    private LoginResult loginResult;

    public ScanQrcode(String loginKey, long expire, ScanQrcodeEnum scanQrcodeEnum) {
        this.loginKey = loginKey;
        this.expire = expire;
        this.scanQrcodeEnum = scanQrcodeEnum;
    }

    public ScanQrcode() {
    }

    public String getLoginKey() {
        return loginKey;
    }

    public void setLoginKey(String loginKey) {
        this.loginKey = loginKey;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public ScanQrcodeEnum getScanQrcodeEnum() {
        return scanQrcodeEnum;
    }

    public void setScanQrcodeEnum(ScanQrcodeEnum scanQrcodeEnum) {
        this.scanQrcodeEnum = scanQrcodeEnum;
    }

    public LoginResult getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(LoginResult loginResult) {
        this.loginResult = loginResult;
    }
}
