package com.mx.ymate.security.base.enums;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description: 操作日志类型枚举
 */
public enum OperationType {
    /**
     * 未知
     */
    UNKNOWN("未知"),

    /**
     * 新增
     */
    CREATE("新增"),

    /**
     * 修改
     */
    UPDATE("修改"),

    /**
     * 删除
     */
    DELETE("删除"),

    /**
     * 其他
     */
    OTHER("其他"),

    /**
     * 登录
     */
    LOGIN("登录");

    private final String value;

    OperationType(String value) {
        this.value = value;
    }

    public static OperationType valueTo(String value) {
        switch (value) {
            case "未知":
                return UNKNOWN;
            case "新增":
                return CREATE;
            case "修改":
                return UPDATE;
            case "删除":
                return DELETE;
            case "其他":
                return OTHER;
            case "登录":
                return LOGIN;
            default:
                return null;
        }
    }

    public String value() {
        return this.value;
    }

}
