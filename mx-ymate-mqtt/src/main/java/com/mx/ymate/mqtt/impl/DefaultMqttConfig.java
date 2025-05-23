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
package com.mx.ymate.mqtt.impl;

import com.mx.ymate.mqtt.IMqtt;
import com.mx.ymate.mqtt.IMqttConfig;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.commons.util.UUIDUtils;
import net.ymate.platform.core.configuration.IConfigReader;
import net.ymate.platform.core.module.IModuleConfigurer;
import net.ymate.platform.log.Logs;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static com.mx.ymate.mqtt.MqttConstant.MQTT_VERSION_3_1_1;
import static org.eclipse.paho.client.mqttv3.MqttConnectOptions.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public final class DefaultMqttConfig implements IMqttConfig {

    private boolean enabled = true;

    private boolean autoConnect = true;

    private boolean initialized;

    private String url;

    private String clientId;

    private String userName;

    private String password;

    private MqttCallbackExtended callback;

    private boolean cleanSession;

    private boolean manualAcks;

    private int connectionTimeout;

    private int keepAliveInterval;

    private int maxInflight;

    private String version;

    private boolean automaticReconnection;

    private int reconnectDelay;

    private Properties sslProperties;

    private String storageDir;

    private String willTopic;

    private String willPayload;

    private int willQos;

    private boolean willRetained;


    public static DefaultMqttConfig create(IModuleConfigurer moduleConfigurer) {
        return new DefaultMqttConfig(moduleConfigurer);
    }


    private DefaultMqttConfig() {
    }

    private DefaultMqttConfig(IModuleConfigurer moduleConfigurer) {
        IConfigReader configReader = moduleConfigurer.getConfigReader();
        enabled = configReader.getBoolean(ENABLED, true);
        autoConnect = configReader.getBoolean(AUTO_CONNECT, true);
        url = configReader.getString(URL);
        clientId = configReader.getString(CLIENT_ID);
        if (clientId.contains(EX_TIME)) {
            clientId = clientId.replace(EX_TIME, BlurObject.bind(DateTimeUtils.currentTimeMillis()).toStringValue());
        } else if (clientId.contains(EX_UUID)) {
            clientId = clientId.replace(EX_UUID, UUIDUtils.UUID());
        }
        if (StringUtils.isBlank(clientId)) {
            clientId = "mqttClientId-" + UUIDUtils.UUID();
        }
        userName = configReader.getString(USER_NAME);
        password = configReader.getString(PASSWORD);
        callback = configReader.getClassImpl(CALLBACK, MqttCallbackExtended.class);
        if (callback == null) {
            Logs.get().getLogger().error("请指定mqttCallback类");
            throw new NullArgumentException(CALLBACK);
        }
        cleanSession = configReader.getBoolean(CLEAN_SESSION, CLEAN_SESSION_DEFAULT);
        manualAcks = configReader.getBoolean(MANUAL_ACKS, false);
        connectionTimeout = configReader.getInt(CONNECTION_TIMEOUT, CONNECTION_TIMEOUT_DEFAULT);
        keepAliveInterval = configReader.getInt(KEEP_ALIVE_INTERVAL, KEEP_ALIVE_INTERVAL_DEFAULT);
        maxInflight = configReader.getInt(MAX_INFLIGHT, MAX_INFLIGHT_DEFAULT);
        version = configReader.getString(VERSION, MQTT_VERSION_3_1_1);
        automaticReconnection = configReader.getBoolean(AUTOMATIC_RECONNECTION, false);
        reconnectDelay = configReader.getInt(RECONNECT_DELAY, 128000);
        String sslPath = configReader.getString(SSL_PROPERTIES);
        if (StringUtils.isNotBlank(sslPath)) {
            Properties p = new Properties();
            try {
                p.load(new FileInputStream(sslPath));
                this.sslProperties = p;
            } catch (FileNotFoundException e) {
                Logs.get().getLogger().error("MQTT SSL配置文件未找到,请检查文件[" + sslPath + "]是否存在", e);
            } catch (IOException e) {
                Logs.get().getLogger().error("MQTT SSL配置文件[" + sslPath + "]读取失败", e);
            }
        }
        storageDir = configReader.getString(STORAGE_DIR);
        willTopic = configReader.getString(WILL_TOPIC);
        willPayload = configReader.getString(WILL_PAYLOAD);
        willQos = configReader.getInt(WILL_QOS);
        willRetained = configReader.getBoolean(WILL_RETAINED);

    }

    @Override
    public void initialize(IMqtt owner) throws Exception {
        if (!initialized) {
            initialized = true;
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean autoConnect() {
        return autoConnect;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public String clientId() {
        return clientId;
    }

    @Override
    public String userName() {
        return userName;
    }

    @Override
    public String password() {
        return password;
    }

    @Override
    public MqttCallbackExtended callback() {
        return callback;
    }

    @Override
    public boolean cleanSession() {
        return cleanSession;
    }

    @Override
    public boolean manualAcks() {
        return manualAcks;
    }

    @Override
    public int connectionTimeout() {
        return connectionTimeout;
    }

    @Override
    public int keepAliveInterval() {
        return keepAliveInterval;
    }

    @Override
    public int maxInflight() {
        return maxInflight;
    }

    @Override
    public String version() {
        return version;
    }

    @Override
    public boolean automaticReconnection() {
        return automaticReconnection;
    }

    @Override
    public int reconnectDelay() {
        return reconnectDelay;
    }

    @Override
    public Properties sslProperties() {
        return sslProperties;
    }

    @Override
    public String storageDir() {
        return storageDir;
    }

    @Override
    public String willTopic() {
        return willTopic;
    }

    @Override
    public String willPayload() {
        return willPayload;
    }

    @Override
    public int willQos() {
        return willQos;
    }

    @Override
    public boolean willRetained() {
        return willRetained;
    }

    @Override
    public boolean isHasWill() {
        return StringUtils.isNotBlank(willTopic) && StringUtils.isNotBlank(willPayload);
    }

}