package com.mx.ymate.captcha.sms;

import java.io.Serializable;

/**
 * @Author: xujianpeng.
 * @Date 2024/10/15.
 * @Time: 14:00.
 * @Description:
 */
public class SmsBean  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String mobile;

    private long expire;

    public SmsBean(String code, String mobile, long expire) {
        this.code = code;
        this.mobile = mobile;
        this.expire = expire;
    }

    public SmsBean(String code, String mobile) {
        this.code = code;
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
