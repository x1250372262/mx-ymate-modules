package com.mx.ymate.security.base.enums;

import static com.mx.ymate.security.I18nConstant.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 操作日志类型枚举
 */
public enum OperationType {

    /**
     * 未知
     */
    UNKNOWN(LOG_OPERATION_TYPE_UNKNOWN_MSG, LOG_OPERATION_TYPE_UNKNOWN_I18N_KEY),

    /**
     * 新增
     */
    CREATE(LOG_OPERATION_TYPE_CREATE_MSG, LOG_OPERATION_TYPE_CREATE_I18N_KEY),

    /**
     * 修改
     */
    UPDATE(LOG_OPERATION_TYPE_UPDATE_MSG, LOG_OPERATION_TYPE_UPDATE_I18N_KEY),

    /**
     * 删除
     */
    DELETE(LOG_OPERATION_TYPE_DELETE_MSG, LOG_OPERATION_TYPE_DELETE_I18N_KEY),

    /**
     * 其他
     */
    OTHER(LOG_OPERATION_TYPE_OTHER_MSG, LOG_OPERATION_TYPE_OTHER_I18N_KEY),

    /**
     * 登录
     */
    LOGIN(LOG_OPERATION_TYPE_LOGIN_MSG, LOG_OPERATION_TYPE_LOGIN_I18N_KEY);

    private final String value;

    private final String i18nKey;

    OperationType(String value, String i18nKey) {
        this.value = value;
        this.i18nKey = i18nKey;
    }

    public String value() {
        return this.value;
    }

    public String i18nKey() {
        return this.i18nKey;
    }

}
