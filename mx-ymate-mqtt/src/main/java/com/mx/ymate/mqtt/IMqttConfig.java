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

import com.mx.ymate.mqtt.bean.MqttConfig;
import net.ymate.platform.core.beans.annotation.Ignored;
import net.ymate.platform.core.support.IInitialization;

import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Ignored
public interface IMqttConfig extends IInitialization<IMqtt> {

    String ENABLED = "enabled";
    String NAME = "name";
    String DEFAULT_NAME = "default";
    String AUTO_INIT = "autoInit";
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
    String STORAGE_DIR = "storageDir";
    String SSL_ENABLED = "ssl.enabled";
    String SSL_TRUSTSTORE = "ssl.truststore";
    String SSL_TRUSTSTORE_PASSWORD = "ssl.truststore_password";
    String SSL_TRUSTSTORE_TYPE = "ssl.truststore_type";
    String SSL_KEYSTORE = "ssl.keystore";
    String SSL_KEYSTORE_PASSWORD = "ssl.keystore_password";
    String SSL_KEYSTORE_TYPE = "ssl.keystore_type";
    String SSL_PROTOCOL = "ssl.protocol";
    String WILL_ENABLED = "will.enabled";
    String WILL_TOPIC = "will.topic";
    String WILL_PAYLOAD = "will.payload";
    String WILL_QOS = "will.qos";
    String WILL_RETAINED = "will.retained";

    String EX_TIME = "{time}";
    String EX_UUID = "{uuid}";

    String MQTT_VERSION_3_1_1 = "3.1.1";
    String MQTT_VERSION_3_1 = "3.1";

    /**
     * 模块是否已启用, 默认值: true
     *
     * @return 返回false表示禁用
     */
    boolean isEnabled();

    /**
     * 是否自动初始化
     * @return
     */
    boolean autoInit();

    /**
     * 配置列表
     * @return
     */
    List<MqttConfig> configList();

    /**
     * 某一个配置
     * @param name
     * @return
     */
    MqttConfig mqttConfig(String name);
}