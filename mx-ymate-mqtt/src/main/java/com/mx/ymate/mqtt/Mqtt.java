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
import com.mx.ymate.mqtt.event.MqttEvent;
import com.mx.ymate.mqtt.impl.DefaultMqttConfig;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.core.*;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.IModuleConfigurer;
import net.ymate.platform.core.module.impl.DefaultModuleConfigurer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import java.util.List;
import java.util.Properties;

import static com.mx.ymate.mqtt.MqttConstant.MQTT_VERSION_3_1;
import static com.mx.ymate.mqtt.MqttConstant.MQTT_VERSION_3_1_1;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public final class Mqtt implements IModule, IMqtt {

    private static final Log LOG = LogFactory.getLog(Mqtt.class);

    private static volatile IMqtt instance;

    private IApplication owner;

    private IMqttConfig config;

    private MqttAsyncClient mqttAsyncClient;


    private boolean initialized;

    public static IMqtt get() {
        IMqtt inst = instance;
        if (inst == null) {
            synchronized (Mqtt.class) {
                inst = instance;
                if (inst == null) {
                    instance = inst = YMP.get().getModuleManager().getModule(Mqtt.class);
                }
            }
        }
        return inst;
    }

    public Mqtt() {
    }

    public Mqtt(IMqttConfig config) {
        this.config = config;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Override
    public void initialize(IApplication owner) throws Exception {
        if (!initialized) {
            this.owner = owner;
            if (config == null) {
                IApplicationConfigureFactory configureFactory = owner.getConfigureFactory();
                if (configureFactory != null) {
                    IApplicationConfigurer configurer = configureFactory.getConfigurer();
                    IModuleConfigurer moduleConfigurer = configurer == null ? null : configurer.getModuleConfigurer(MODULE_NAME);
                    if (moduleConfigurer != null) {
                        config = DefaultMqttConfig.create(moduleConfigurer);
                    } else {
                        config = DefaultMqttConfig.create(DefaultModuleConfigurer.createEmpty(MODULE_NAME));
                    }
                }
            }
            if (!config.isInitialized()) {
                config.initialize(this);
            }
//            if (config.isEnabled() && config.autoConnect()) {
//                //等待框架启动成功
//                connect();
//            }
            initialized = true;
            YMP.showVersion("初始化 mx-ymate-mqtt-mqtt-${version} 模块成功", new Version(1, 0, 0, Mqtt.class, Version.VersionType.Release));
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void close() throws Exception {
        if (initialized) {
            initialized = false;
            if (config.isEnabled() && config.autoConnect()) {
                disconnect();
            }
            config = null;
            owner = null;
        }
    }

    @Override
    public IApplication getOwner() {
        return owner;
    }

    @Override
    public IMqttConfig getConfig() {
        return config;
    }

    @Override
    public MqttAsyncClient getClient() {
        return mqttAsyncClient;
    }

    private void fireEvent(MqttEvent.EVENT event) {
        owner.getEvents().fireEvent(new MqttEvent(owner, event));
    }


    private MqttConnectOptions getOptions() {
        // 连接参数
        MqttConnectOptions options = new MqttConnectOptions();
        mqttAsyncClient.setManualAcks(config.manualAcks());
        options.setAutomaticReconnect(config.automaticReconnection());
        options.setCleanSession(config.cleanSession());
        options.setConnectionTimeout(config.connectionTimeout());
        options.setKeepAliveInterval(config.keepAliveInterval());
        options.setMaxReconnectDelay(config.reconnectDelay());
        options.setMaxInflight(config.maxInflight());

        //设置mqtt版本
        if (MQTT_VERSION_3_1_1.equals(config.version())) {
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        } else if (MQTT_VERSION_3_1.equals(config.version())) {
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        } else {
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_DEFAULT);
        }

        //设置用户名密码
        if (StringUtils.isNotBlank(config.userName()) && StringUtils.isNotBlank(config.password())) {
            options.setUserName(config.userName());
            options.setPassword(config.password().toCharArray());
        }

        //设置遗嘱消息
        if (config.isHasWill()) {
            options.setWill(config.willTopic(), config.willPayload().getBytes(), config.willQos(), config.willRetained());
        }

        //设置SSL配置文件
        Properties sslProperties = config.sslProperties();
        if (sslProperties != null && !sslProperties.isEmpty()) {
            options.setSSLProperties(sslProperties);
        }
        return options;
    }

    @Override
    public void connect() {
        try {
            if (StringUtils.isNotBlank(config.storageDir())) {
                mqttAsyncClient = new MqttAsyncClient(config.url(), config.clientId(), new MqttDefaultFilePersistence(config.storageDir()));
            } else {
                mqttAsyncClient = new MqttAsyncClient(config.url(), config.clientId());
            }
        } catch (Exception e) {
            LOG.error("MQTT客户端初始化失败", e);
            return;
        }
        mqttAsyncClient.setCallback(config.callback());
        try {
            mqttAsyncClient.connect(getOptions(), null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    fireEvent(MqttEvent.EVENT.MQTT_CONNECT_SUCCESS);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    fireEvent(MqttEvent.EVENT.MQTT_CONNECT_FAIL);
                }
            });
        } catch (MqttException e) {
            LOG.error("MQTT服务连接失败", e);
            return;
        }
        LOG.info("MQTT服务连接成功");
    }

    @Override
    public void disconnect() {
        try {
            if (mqttAsyncClient != null && mqttAsyncClient.isConnected()) {
                mqttAsyncClient.disconnect(null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        fireEvent(MqttEvent.EVENT.MQTT_DISCONNECT_SUCCESS);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        fireEvent(MqttEvent.EVENT.MQTT_DISCONNECT_FAIL);
                    }
                });
                LOG.info("MQTT服务释放成功");
            } else {
                LOG.warn("MQTT客户端已断开或未连接");
            }
        } catch (MqttException e) {
            LOG.error("MQTT服务释放失败", e);
        }
    }

    @Override
    public void subscribe(String topic, QosEnum qosEnum) {
        try {
            mqttAsyncClient.subscribe(topic, qosEnum.getValue(), null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    fireEvent(MqttEvent.EVENT.MQTT_SUBSCRIBE_SUCCESS);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    fireEvent(MqttEvent.EVENT.MQTT_SUBSCRIBE_FAIL);
                }
            });
        } catch (MqttException e) {
            LOG.error("主题: " + topic + "订阅失败", e);
            throw RuntimeUtils.wrapRuntimeThrow(e);
        }
    }

    @Override
    public void subscribe(String[] topics, QosEnum qosEnum) {
        for (String topic : topics) {
            subscribe(topic, qosEnum);
        }
    }

    @Override
    public void subscribe(List<String> topics, QosEnum qosEnum) {
        for (String topic : topics) {
            subscribe(topic, qosEnum);
        }
    }

    @Override
    public void unSubscribe(String topic) {
        try {
            mqttAsyncClient.unsubscribe(topic, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    fireEvent(MqttEvent.EVENT.MQTT_UN_SUBSCRIBE_SUCCESS);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    fireEvent(MqttEvent.EVENT.MQTT_UN_SUBSCRIBE_FAIL);
                }
            });
        } catch (MqttException e) {
            LOG.error("主题: " + topic + "取消订阅失败", e);
            throw RuntimeUtils.wrapRuntimeThrow(e);
        }
    }

    @Override
    public void unSubscribe(String[] topics) {
        for (String topic : topics) {
            unSubscribe(topic);
        }
    }

    @Override
    public void unSubscribe(List<String> topics) {
        for (String topic : topics) {
            unSubscribe(topic);
        }
    }

    @Override
    public void publish(String topic, byte[] payload, QosEnum qos, boolean retained) {
        try {
            mqttAsyncClient.publish(topic, payload, qos.getValue(), retained, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    fireEvent(MqttEvent.EVENT.MQTT_PUBLISH_SUCCESS);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    fireEvent(MqttEvent.EVENT.MQTT_PUBLISH_FAIL);
                }
            });
        } catch (MqttException e) {
            LOG.error("主题: " + topic + "发布数据失败", e);
            throw RuntimeUtils.wrapRuntimeThrow(e);
        }
    }

    @Override
    public void publish(String topic, String payload, QosEnum qos, boolean retained) {
        publish(topic, payload.getBytes(), qos, retained);
    }

    @Override
    public void publish(String topic, String payload, QosEnum qos) {
        publish(topic, payload.getBytes(), qos, false);
    }


    @Override
    public void ack(MqttMessage message) {

        try {
            if (config.manualAcks() && message.getQos() == QosEnum.QOS_AT_LEAST_ONCE.getValue()) {
                mqttAsyncClient.messageArrivedComplete(message.getId(), message.getQos());
            }
            LOG.info("消息确认成功");
        } catch (MqttException e) {
            LOG.info("消息确认失败");
            throw RuntimeUtils.wrapRuntimeThrow(e);
        }
    }
}
