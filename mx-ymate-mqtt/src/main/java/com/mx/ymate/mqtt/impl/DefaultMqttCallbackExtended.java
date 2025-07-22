package com.mx.ymate.mqtt.impl;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @Author: xujianpeng.
 * @Date 2025/7/21.
 * @Time: 14:43.
 * @Description:
 */
public class DefaultMqttCallbackExtended implements MqttCallbackExtended {

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    @Override
    public void connectComplete(boolean reconnect, String serverUri) {

    }
}