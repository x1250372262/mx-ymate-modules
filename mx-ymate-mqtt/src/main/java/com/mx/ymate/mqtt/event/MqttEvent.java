package com.mx.ymate.mqtt.event;

import net.ymate.platform.core.event.AbstractEventContext;
import net.ymate.platform.core.event.IEvent;

/**
 * @Author: mengxiang.
 * @Date 2024/10/29.
 * @Time: 15:13.
 * @Description:
 */
public class MqttEvent extends AbstractEventContext<Object, MqttEvent.EVENT> implements IEvent {

    private static final long serialVersionUID = 1L;

    public enum EVENT {

        /**
         * MQTT连接成功
         */
        MQTT_CONNECT_SUCCESS,

        /**
         * MQTT连接失败
         */
        MQTT_CONNECT_FAIL,

        /**
         * MQTT断开成功
         */
        MQTT_DISCONNECT_SUCCESS,

        /**
         * MQTT断开失败
         */
        MQTT_DISCONNECT_FAIL,

        /**
         * MQTT订阅成功
         */
        MQTT_SUBSCRIBE_SUCCESS,

        /**
         * MQTT订阅失败
         */
        MQTT_SUBSCRIBE_FAIL,

        /**
         * MQTT取消订阅成功
         */
        MQTT_UN_SUBSCRIBE_SUCCESS,

        /**
         * MQTT取消订阅失败
         */
        MQTT_UN_SUBSCRIBE_FAIL,

        /**
         * MQTT发布成功
         */
        MQTT_PUBLISH_SUCCESS,

        /**
         * MQTT发布失败
         */
        MQTT_PUBLISH_FAIL,
    }

    public MqttEvent(Object owner, EVENT eventName) {
        super(owner, MqttEvent.class, eventName);
    }
}
