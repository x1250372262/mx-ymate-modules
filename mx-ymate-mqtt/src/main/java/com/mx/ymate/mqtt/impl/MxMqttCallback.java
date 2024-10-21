package com.mx.ymate.mqtt.impl;

import net.ymate.platform.core.IApplication;
import net.ymate.platform.core.YMP;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @Author: mengxiang
 * @Date 2024/04/07
 * @Time: 11:19
 * @Description:
 */
public class MxMqttCallback implements MqttCallback {

    private static final Log LOG = LogFactory.getLog(MxMqttCallback.class);

    private static final IApplication APPLICATION = YMP.get();

    @Override
    public void connectionLost(Throwable cause) {
        if (LOG.isWarnEnabled() && APPLICATION.isDevEnv()) {
            LOG.warn("连接丢失，原因：" + cause.getMessage());
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if (LOG.isInfoEnabled() && APPLICATION.isDevEnv()) {
            LOG.info("收到消息，主题：" + topic + "，内容：" + new String(message.getPayload()));
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}
