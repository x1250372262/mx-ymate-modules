/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mx.ymate.mqtt;

import com.mx.ymate.mqtt.enums.QosEnum;
import net.ymate.platform.core.IApplication;
import net.ymate.platform.core.beans.annotation.Ignored;
import net.ymate.platform.core.support.IDestroyable;
import net.ymate.platform.core.support.IInitialization;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Ignored
public interface IMqtt extends IInitialization<IApplication>, IDestroyable {

    String MODULE_NAME = "module.mqtt";

    /**
     * 获取所属应用容器
     *
     * @return 返回所属应用容器实例
     */
    IApplication getOwner();

    /**
     * 获取配置
     *
     * @return 返回配置对象
     */
    IMqttConfig getConfig();

    /**
     * 返回客户端
     *
     * @return
     */
    MqttAsyncClient getClient();

    /**
     * 连接
     */
    void connect();

    /**
     * 关闭
     */
    void disconnect();

    /**
     * 单个订阅
     *
     * @param topic
     * @param qosEnum
     * @return
     */
    void subscribe(String topic, QosEnum qosEnum);

    /**
     * 批量订阅
     *
     * @param topics
     * @param qosEnum
     * @return
     */
    void subscribe(String[] topics, QosEnum qosEnum);

    /**
     * 批量订阅
     *
     * @param topics
     * @param qosEnum
     * @return
     */
    void subscribe(List<String> topics, QosEnum qosEnum);


    /**
     * 取消订阅
     *
     * @param topic
     * @return
     */
    void unSubscribe(String topic);

    /**
     * 批量取消订阅
     * @param topics
     */
    void unSubscribe(String[] topics);

    /**
     * 批量取消订阅
     * @param topics
     */
    void unSubscribe(List<String> topics);


    /**
     * 发布主题
     *
     * @param topic
     * @param payload
     * @param qos
     * @param retained
     * @return
     */
    void publish(String topic, byte[] payload, QosEnum qos, boolean retained);


    /**
     * 发布主题
     *
     * @param topic
     * @param payload
     * @param qos
     * @param retained
     * @return
     */
    void publish(String topic, String payload, QosEnum qos, boolean retained);

    /**
     * 发布主题
     *
     * @param topic
     * @param payload
     * @param qos
     * @return
     */
    void publish(String topic, String payload, QosEnum qos);


    /**
     * 告诉 MQTT 我已经收到这条消息
     *
     * @param message
     */
    void ack(MqttMessage message);

}
