package com.mx.ymate.captcha.sms.code;

import com.mx.ymate.dev.code.II18nCode;

/**
 * @Author: mengxiang.
 * @Date 2024/10/15.
 * @Time: 09:28.
 * @Description:
 */
public enum SmsCaptchaCode implements II18nCode {

    /**
     * 短信错误码
     */
    CODE_ERROR("CAS00", "MX.CODE.SMS_CAPTCHA.CODE_ERROR", "验证码错误"),
    EXPIRE_ERROR("SMS01", "MX.CODE.SMS_CAPTCHA.EXPIRE_ERROR", "验证码已过期");


    private final String code;
    private final String i18nKey;
    private final String msg;

    SmsCaptchaCode(String code, String i18nKey, String msg) {
        this.code = code;
        this.i18nKey = i18nKey;
        this.msg = msg;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String i18nKey() {
        return i18nKey;
    }

    @Override
    public String msg() {
        return msg;
    }
}
