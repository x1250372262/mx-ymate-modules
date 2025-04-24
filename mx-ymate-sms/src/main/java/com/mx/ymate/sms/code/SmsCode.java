package com.mx.ymate.sms.code;

import com.mx.ymate.dev.code.ICode;
import com.mx.ymate.dev.code.II18nCode;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public enum SmsCode implements II18nCode {

    /**
     * 短信错误码
     */
    SEND_ERROR("SMS00", "MX.CODE.SMS.SEND_ERROR", "短信发送失败"),
    PARAMS_NOT_EMPTY("SMS01", "MX.CODE.SMS.PARAMS_NOT_EMPTY", "参数不能为空"),
    CHANEL_ERROR("SMS02", "MX.CODE.SMS.CHANEL_ERROR", "请检查配置文件是否正确"),
    TEMPLATE_ID_ERROR("SMS03", "MX.CODE.SMS.TEMPLATE_ID_ERROR", "请检查模板id是否配置正确");


    private final String code;
    private final String i18nKey;
    private final String msg;

    SmsCode(String code, String i18nKey, String msg) {
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
