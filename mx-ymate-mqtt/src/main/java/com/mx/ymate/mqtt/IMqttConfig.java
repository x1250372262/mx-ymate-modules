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

import net.ymate.platform.core.beans.annotation.Ignored;
import net.ymate.platform.core.support.IInitialization;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;

import java.util.Properties;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Ignored
public interface IMqttConfig extends IInitialization<IMqtt> {

    String ENABLED = "enabled";
    String AUTO_CONNECT = "autoConnect";
    String URL = "url";
    String CLIENT_ID = "clientId";
    String USER_NAME = "userName";
    String PASSWORD = "password";
    String CALLBACK = "callback";
    String CLEAN_SESSION = "cleanSession";
    String MANUAL_ACKS = "manualAcks";
    String CONNECTION_TIMEOUT = "connectionTimeout";
    String KEEP_ALIVE_INTERVAL = "keepAliveInterval";
    String MAX_INFLIGHT = "maxInflight";
    String VERSION = "version";
    String AUTOMATIC_RECONNECTION = "automaticReconnection";
    String RECONNECT_DELAY = "reconnectDelay";
    String SSL_PROPERTIES = "sslProperties";
    String STORAGE_DIR = "storageDir";
    String WILL_TOPIC = "will.topic";
    String WILL_PAYLOAD = "will.payload";
    String WILL_QOS = "will.qos";
    String WILL_RETAINED = "will.retained";

    String EX_TIME = "{time}";
    String EX_UUID = "{uuid}";


    /**
     * 模块是否已启用, 默认值: true
     *
     * @return 返回false表示禁用
     */
    boolean isEnabled();

    /**
     * 是否自动连接服务 默认true
     *
     * @return
     */
    boolean autoConnect();

    /**
     * 连接地址
     * @return
     */
    String url();

    /**
     * 客户端id {time}代表增加默认时间戳 {uuid}代表增加随机uuid  没有则取本身
     * @return
     */
    String clientId();

    /**
     * 用户名默认不设置
     * @return
     */
    String userName();

    /**
     * 密码默认不设置
     * @return
     */
    String password();

    /**
     * 回调类
     * @return
     */
    MqttCallbackExtended callback();

    /**
     * 是否清理session，false时可接收离线消息 true忽略离线消息 默认true
     * @return
     */
    boolean cleanSession();

    /**
     * 是否手动确认消息 默认false
     * @return
     */
    boolean manualAcks();

    /**
     * 超时时间 默认60s
     * @return
     */
    int connectionTimeout();

    /**
     * 心跳时间间隔 默认60s 值为0将禁用客户端中的保活处理
     * @return
     */
    int keepAliveInterval();

    /**
     * 设置“最大空中流量”。请在高流量环境中增加此值。默认值为10
     * @return
     */
    int maxInflight();

    /**
     * 版本 默认3.1.1
     * @return
     */
    String version();

    /**
     * 是否自动重连 默认false
     * @return
     */
    boolean automaticReconnection();

    /**
     * 重新连接之间等待的最长时间
     * @return
     */
    int reconnectDelay();

    /**
     * SSL配置文件地址
     * @return
     */
    Properties sslProperties();

    /**
     * 消息保存方式 如果不设置，默认保存在内存中，设置了则保存着指定的目录下
     * @return
     */
    String storageDir();

    /**
     * 要发布到的主题
     * @return
     */
    String willTopic();

    /**
     * 消息的字节有效负载
     * @return
     */
    String willPayload();

    /**
     * qos–在（0、1或2）发布消息的服务质量
     * @return
     */
    int willQos();

    /**
     * 是否应保留消息
     * @return
     */
    boolean willRetained();

    /**
     * 是否有遗嘱消息
     * @return
     */
    boolean isHasWill();
}