package com.mx.ymate.mqtt.impl;

import net.ymate.platform.log.Logs;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MxMqttCallback implements MqttCallback {
    @Override
    public void connectionLost(Throwable cause) {
        Logs.get().getLogger().warn("连接丢失，原因：" + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Logs.get().getLogger().info("收到消息，主题：" + topic + "，内容：" + new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}
