package com.sft.urchin.station.event;

import com.mx.ymate.mqtt.impl.DefaultMqttCallbackExtended;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @Author: xujianpeng.
 * @Date 2024/10/29.
 * @Time: 09:32.
 * @Description:
 */
public class UrchinMqttCallback extends DefaultMqttCallbackExtended {

    private static final Log LOG = LogFactory.getLog(UrchinMqttCallback.class);

    @Override
    public void connectionLost(Throwable cause) {
        LOG.error("连接丢失", cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String mqttValue = new String(message.getPayload());
        LOG.info("topic=========" + topic+",内容======="+mqttValue);
//        if (StringUtils.isNotBlank(topic) && StringUtils.isNotBlank(mqttValue)) {
//            LOG.debug("发送数据时间:" + DateTimeUtils.formatTime(System.currentTimeMillis(), DateTimeUtils.YYYY_MM_DD_HH_MM_SS_SSS));
//            ReceiveDataHandler.handler(topic, mqttValue);
//        }
    }
}
