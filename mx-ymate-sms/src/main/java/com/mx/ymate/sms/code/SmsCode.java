package com.mx.ymate.sms.code;

import com.mx.ymate.dev.code.ICode;

/**
 * @Author: mengxiang.
 * @create: 2021-07-02 16:58
 * @Description:
 */
public enum SmsCode implements ICode {

    /**
     * 短信错误码
     */
    SEND_ERROR("SMS00", "短信发送失败"),
    PARAMS_NOT_EMPTY("SMS01", "参数不能为空"),
    CHANEL_ERROR("SMS02", "请检查配置文件是否正确");


    private final String code;
    private final String msg;

    SmsCode(String code, String msg) {
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
