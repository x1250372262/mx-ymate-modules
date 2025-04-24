package com.mx.ymate.mqtt.enums;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public enum QosEnum {

    /**
     * QOS_AT_MOST_ONCE 最多一次，有可能重复或丢失
     */
    QOS_AT_MOST_ONCE(0),

    /**
     * QOS_AT_LEAST_ONCE 至少一次，有可能重复
     */
    QOS_AT_LEAST_ONCE(1),

    /**
     * QOS_EXACTLY_ONCE 只有一次，确保消息只到达一次（用于比较严格的计费系统）
     */
    QOS_EXACTLY_ONCE(2);

    private final int value;

    QosEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
