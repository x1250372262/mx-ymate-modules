package com.mx.ymate.dev.code;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 错误码枚举
 */
public enum Code implements II18nCode {

    /**
     * 通用错误码
     */
    SUCCESS("00000", "MX.CODE.COMMON.SUCCESS", "操作成功"),
    ERROR("M0000", "MX.CODE.COMMON.ERROR", "操作失败"),
    NO_DATA("M0001", "MX.CODE.COMMON.NO_DATA", "数据不存在"),
    FIELDS_EXISTS("M0002", "MX.CODE.COMMON.FIELDS_EXISTS", "{}已存在"),
    VERSION_NOT_SAME("M0003", "MX.CODE.COMMON.VERSION_NOT_SAME", "数据被修改了，请刷新页面重试"),
    NOT_LOGIN("M0004", "MX.CODE.COMMON.NOT_LOGIN", "用户未授权登录或会话已过期，请重新登录"),
    NOT_PERMISSION("M0005", "MX.CODE.COMMON.NOT_PERMISSION", "没有访问的权限"),
    SYSTEM_ERROR("M0006", "MX.CODE.COMMON.SYSTEM_ERROR", "系统繁忙"),
    INVALID_PARAMETER("M0007", "MX.CODE.COMMON.INVALID_PARAMETER", "参数验证无效"),
    NOT_REPEAT_REQUEST("M0008", "MX.CODE.COMMON.NOT_REPEAT_REQUEST", "{}秒内请勿重复请求"),
    NOT_SUPPORT_REQUEST("M0009", "MX.CODE.COMMON.NOT_SUPPORT_REQUEST", "不支持{}请求"),
    SERVER_HTTP_METHOD_ERROR("M0010", "MX.CODE.COMMON.SERVER_HTTP_METHOD_ERROR", "请求方式错误"),
    SERVER_REQUEST_ERROR("M0011", "MX.CODE.COMMON.SERVER_REQUEST_ERROR", "请求失败"),
    LOCKED("M0012", "MX.CODE.COMMON.LOCKED", "账户处于挂机锁屏模式，请先解锁。");

    private final String code;
    private final String i18nKey;
    private final String msg;

    Code(String code, String i18nKey, String msg) {
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
