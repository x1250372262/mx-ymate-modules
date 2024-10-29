package com.mx.ymate.mqtt.impl;

import cn.hutool.core.collection.CollUtil;
import com.mx.ymate.mqtt.Mqtt;
import com.mx.ymate.mqtt.ResubscribeBean;
import com.mx.ymate.mqtt.enums.QosEnum;
import net.ymate.platform.log.Logs;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;

import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2024/10/29.
 * @Time: 15:04.
 * @Description:
 */
public abstract class AbstractAutoConnectMqttCallbackExtended extends AbstractMqttCallbackExtended {


    @Override
    public void connectComplete(boolean reconnect, String serverUri) {
        if (!reconnect) {
            return;
        }
        ResubscribeBean resubscribeBean = resubscribeBean();
        if (resubscribeBean == null) {
            return;
        }
        List<String> topicList = resubscribeBean.getTopicList();
        QosEnum qosEnum = resubscribeBean.getQosEnum();
        if (CollUtil.isEmpty(topicList) || qosEnum == null) {
            return;
        }
        Logs.get().getLogger().info("连接恢复，重新订阅主题...");
        resubscribeTopics(topicList, qosEnum);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    /**
     * 获取重新订阅bean
     *
     * @return
     */
    public abstract ResubscribeBean resubscribeBean();


    private void resubscribeTopics(List<String> topicList, QosEnum qosEnum) {
        Mqtt.get().subscribe(topicList, qosEnum);
    }

}
