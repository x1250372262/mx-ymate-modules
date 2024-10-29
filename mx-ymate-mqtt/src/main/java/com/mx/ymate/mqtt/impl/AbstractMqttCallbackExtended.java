package com.mx.ymate.mqtt.impl;

import net.ymate.platform.log.Logs;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;

/**
 * @Author: mengxiang.
 * @Date 2024/10/29.
 * @Time: 15:04.
 * @Description:
 */
public abstract class AbstractMqttCallbackExtended implements MqttCallbackExtended {


    @Override
    public void connectComplete(boolean reconnect, String serverUri) {
        Logs.get().getLogger().info("连接恢复...");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
