package com.mx.ymate.mqtt.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mx.ymate.mqtt.Mqtt;
import com.mx.ymate.mqtt.bean.MqttConfig;
import com.mx.ymate.mqtt.bean.MqttContext;
import com.mx.ymate.mqtt.enums.QosEnum;
import com.mx.ymate.mqtt.event.MqttEvent;
import net.ymate.platform.commons.util.RuntimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.mx.ymate.mqtt.IMqttConfig.MQTT_VERSION_3_1;
import static com.mx.ymate.mqtt.IMqttConfig.MQTT_VERSION_3_1_1;
import static org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory.*;

/**
 * @Author: xujianpeng.
 * @Date 2025/7/21.
 * @Time: 13:26.
 * @Description:
 */
public class MqttClient {

    private final MqttConfig mqttConfig;

    private static final Log LOG = LogFactory.getLog(MqttClient.class);

    private MqttConnectOptions options;

    private volatile boolean connected = false;

    private MqttAsyncClient mqttAsyncClient;

    private MqttContext mqttContext;

    private List<String> topicList;


    public MqttClient(MqttConfig mqttConfig) {
        this.mqttConfig = mqttConfig;
    }

    public MqttAsyncClient getClient() {
        return mqttAsyncClient;
    }

    public synchronized MqttClient init() {
        // 连接参数
        options = new MqttConnectOptions();
        options.setAutomaticReconnect(mqttConfig.isAutomaticReconnection());
        options.setCleanSession(mqttConfig.isCleanSession());
        options.setConnectionTimeout(mqttConfig.getConnectionTimeout());
        options.setKeepAliveInterval(mqttConfig.getKeepAliveInterval());
        options.setMaxReconnectDelay(mqttConfig.getReconnectDelay());
        options.setMaxInflight(mqttConfig.getMaxInflight());

        //设置mqtt版本
        if (MQTT_VERSION_3_1_1.equals(mqttConfig.getVersion())) {
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        } else if (MQTT_VERSION_3_1.equals(mqttConfig.getVersion())) {
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        } else {
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_DEFAULT);
        }

        //设置用户名密码
        String userName = mqttConfig.getUserName();
        String password = mqttConfig.getPassword();
        if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
            options.setUserName(userName);
            options.setPassword(password.toCharArray());
        }

        //设置SSL配置文件
        if (mqttConfig.isSslEnabled()) {
            Properties sslProps = new Properties();
            sslProps.setProperty(SSLPROTOCOL, mqttConfig.getSslProtocol());
            sslProps.setProperty(TRUSTSTORE, mqttConfig.getSslTruststore());
            sslProps.setProperty(TRUSTSTOREPWD, mqttConfig.getSslTruststorePassword());
            sslProps.setProperty(TRUSTSTORETYPE, mqttConfig.getSslTruststoreType());
            String sslKeyStore = mqttConfig.getSslKeystore();
            if (StrUtil.isNotBlank(sslKeyStore)) {
                sslProps.setProperty(KEYSTORE, sslKeyStore);
                sslProps.setProperty(KEYSTOREPWD, mqttConfig.getSslKeystorePassword());
                sslProps.setProperty(KEYSTORETYPE, mqttConfig.getSslKeystoreType());
            }
            options.setSSLProperties(sslProps);
        }

        //设置遗嘱消息
        if (mqttConfig.isWillEnabled()) {
            options.setWill(mqttConfig.getWillTopic(), mqttConfig.getWillPayload().getBytes(),
                    mqttConfig.getWillQos(), mqttConfig.isWillRetained());
        }
        LOG.info(StrUtil.format("初始化MQTT[{}]成功", mqttConfig.getName()));
        return this;
    }

    private synchronized void doConnect(Map<String, Object> extras) {
        String name = mqttConfig.getName();
        if (connected) {
            LOG.warn(StrUtil.format("MQTT[{}]已连接，忽略重复连接", name));
            return;
        }
        try {
            if (StringUtils.isNotBlank(mqttConfig.getStorageDir())) {
                mqttAsyncClient = new MqttAsyncClient(mqttConfig.getUrl(), mqttConfig.getClientId(), new MqttDefaultFilePersistence(mqttConfig.getStorageDir()));
            } else {
                mqttAsyncClient = new MqttAsyncClient(mqttConfig.getUrl(), mqttConfig.getClientId());
            }
        } catch (Exception e) {
            LOG.error(StrUtil.format("MQTT[{}]初始化失败", name), e);
            return;
        }
        mqttContext = MqttContext.builder()
                .clientName(name)
                .clientId(mqttConfig.getClientId())
                .putExtras(extras)
                .build();
        mqttAsyncClient.setManualAcks(mqttConfig.isManualAcks());
        mqttAsyncClient.setCallback(new MqttCallbackHandler(mqttConfig.getCallback(), mqttContext));
        try {
            mqttAsyncClient.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Mqtt.get().fireEvent(MqttEvent.EVENT.MQTT_CONNECT_SUCCESS, mqttContext);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    LOG.error(StrUtil.format("MQTT[{}]服务连接失败", mqttConfig.getName()), exception);
                    Mqtt.get().fireEvent(MqttEvent.EVENT.MQTT_CONNECT_FAIL, mqttContext);
                }
            });
        } catch (MqttException e) {
            LOG.error(StrUtil.format("MQTT[{}]服务连接失败", mqttConfig.getName()), e);
            return;
        }
        connected = true;
        LOG.info(StrUtil.format("MQTT[{}]服务连接成功", mqttConfig.getName()));
    }

    public void connect(Map<String, Object> extras) {
        doConnect(extras);
    }

    public void connect() {
        doConnect(null);
    }

    public void disconnect() {
        String name = mqttConfig.getName();
        try {
            if (mqttAsyncClient != null && mqttAsyncClient.isConnected()) {
                mqttAsyncClient.disconnect(null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Mqtt.get().fireEvent(MqttEvent.EVENT.MQTT_DISCONNECT_SUCCESS, mqttContext);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Mqtt.get().fireEvent(MqttEvent.EVENT.MQTT_DISCONNECT_FAIL, mqttContext);
                    }
                });
                connected = false;
                LOG.info(StrUtil.format("MQTT[{}]释放成功", name));
            } else {
                LOG.warn(StrUtil.format("MQTT[{}]已断开或未连接", name));
            }
        } catch (MqttException e) {
            LOG.error(StrUtil.format("MQTT[{}]释放失败", name), e);
        }
    }

    public void destroy() {
        disconnect();
        connected = false;
        options = null;
        mqttAsyncClient = null;
        mqttContext = null;
        LOG.info("Netty Client 已停止");
    }

    public void subscribe(String topic, QosEnum qosEnum) {
        try {
            mqttAsyncClient.subscribe(topic, qosEnum.getValue(), null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    if(topicList == null){
                        topicList = new ArrayList<>();
                    }
                    if(!topicList.contains(topic)){
                        topicList.add(topic);
                    }
                    Mqtt.get().fireEvent(MqttEvent.EVENT.MQTT_SUBSCRIBE_SUCCESS, mqttContext);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Mqtt.get().fireEvent(MqttEvent.EVENT.MQTT_SUBSCRIBE_FAIL, mqttContext);
                }
            });
        } catch (MqttException e) {
            LOG.error("主题: " + topic + "订阅失败", e);
            throw RuntimeUtils.wrapRuntimeThrow(e);
        }
    }

    public void subscribe(String[] topics, QosEnum qosEnum) {
        for (String topic : topics) {
            subscribe(topic, qosEnum);
        }
    }

    public void subscribe(List<String> topics, QosEnum qosEnum) {
        for (String topic : topics) {
            subscribe(topic, qosEnum);
        }
    }

    public void unSubscribe(){
        if(CollUtil.isEmpty(topicList)){
            return;
        }
        for(String topic : topicList){
            unSubscribe(topic);
        }
    }

    public void unSubscribe(String topic) {
        try {
            mqttAsyncClient.unsubscribe(topic, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Mqtt.get().fireEvent(MqttEvent.EVENT.MQTT_UN_SUBSCRIBE_SUCCESS, mqttContext);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Mqtt.get().fireEvent(MqttEvent.EVENT.MQTT_UN_SUBSCRIBE_FAIL, mqttContext);
                }
            });
        } catch (MqttException e) {
            LOG.error("主题: " + topic + "取消订阅失败", e);
            throw RuntimeUtils.wrapRuntimeThrow(e);
        }
    }

    public void unSubscribe(String[] topics) {
        for (String topic : topics) {
            unSubscribe(topic);
        }
    }

    public void unSubscribe(List<String> topics) {
        for (String topic : topics) {
            unSubscribe(topic);
        }
    }

    public void publish(String topic, byte[] payload, QosEnum qos, boolean retained) {
        try {
            mqttAsyncClient.publish(topic, payload, qos.getValue(), retained, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Mqtt.get().fireEvent(MqttEvent.EVENT.MQTT_PUBLISH_SUCCESS, mqttContext);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Mqtt.get().fireEvent(MqttEvent.EVENT.MQTT_PUBLISH_FAIL, mqttContext);
                }
            });
        } catch (MqttException e) {
            LOG.error("主题: " + topic + "发布数据失败", e);
            throw RuntimeUtils.wrapRuntimeThrow(e);
        }
    }

    public void publish(String topic, String payload, QosEnum qos, boolean retained) {
        publish(topic, payload.getBytes(), qos, retained);
    }

    public void publish(String topic, String payload, QosEnum qos) {
        publish(topic, payload.getBytes(), qos, false);
    }


    public void ack(MqttMessage message) {
        try {
            if (mqttConfig.isManualAcks() && message.getQos() == QosEnum.QOS_AT_LEAST_ONCE.getValue()) {
                mqttAsyncClient.messageArrivedComplete(message.getId(), message.getQos());
            }
            LOG.info("消息确认成功");
        } catch (MqttException e) {
            LOG.info("消息确认失败");
            throw RuntimeUtils.wrapRuntimeThrow(e);
        }
    }
}
