package com.mx.ymate.mqtt.impl;

import com.mx.ymate.mqtt.Mqtt;
import com.mx.ymate.mqtt.bean.MqttContext;
import com.mx.ymate.mqtt.event.MqttEvent;
import net.ymate.platform.log.Logs;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @Author: mengxiang.
 * @Date 2024/10/29.
 * @Time: 15:04.
 * @Description:
 */
public class MqttCallbackHandler implements MqttCallbackExtended {


    private final MqttCallbackExtended delegate;
    private final MqttContext mqttContext;

    public MqttCallbackHandler(MqttCallbackExtended delegate, MqttContext mqttContext) {
        this.delegate = delegate;
        this.mqttContext = mqttContext;
    }

    @Override
    public void connectComplete(boolean reconnect, String serverUri) {
        Logs.get().getLogger().info("连接恢复: " + serverUri + " 是否为重连: " + reconnect);
        if (delegate != null) {
            delegate.connectComplete(reconnect, serverUri);
        }
        if (reconnect) {
            // 重连成功事件
            Mqtt.get().fireEvent(MqttEvent.EVENT.MQTT_RECONNECT_SUCCESS, mqttContext);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        if (delegate != null) {
            delegate.connectionLost(cause);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if (delegate != null) {
            delegate.messageArrived(topic, message);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        if (delegate != null) {
            delegate.deliveryComplete(token);
        }
    }
}
