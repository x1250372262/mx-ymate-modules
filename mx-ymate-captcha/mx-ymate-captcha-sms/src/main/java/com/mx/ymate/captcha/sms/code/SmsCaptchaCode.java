package com.mx.ymate.captcha.sms.code;

import com.mx.ymate.dev.code.ICode;

/**
 * @Author: mengxiang.
 * @create: 2021-07-02 16:58
 * @Description:
 */
public enum SmsCaptchaCode implements ICode {

    /**
     * 短信错误码
     */
    CODE_ERROR("CAS00", "验证码错误"),
    EXPIRE_ERROR("SMS01", "验证码已过期");


    private final String code;
    private final String msg;

    SmsCaptchaCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }
}
