package com.mx.ymate.upload.enums;

import java.util.Objects;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public enum AdapterEnum {

    /**
     * 本地适配器
     */
    LOCAL("local"),

    /**
     * minio适配器
     */
    MINIO("minio"),

    /**
     * 七牛云
     */
    QI_NIU("qiniu"),

    /**
     * 腾讯云
     */
    TX("tx"),

    /**
     * 阿里云
     */
    ALI("ali"),

    /**
     * 其他
     */
    OTHER("other");

    private final String value;

    AdapterEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AdapterEnum fromValue(String value) {
        for (AdapterEnum adapterEnum : AdapterEnum.values()) {
            if (Objects.equals(adapterEnum.getValue(), value)) {
                return adapterEnum;
            }
        }
        return OTHER;
    }
}
