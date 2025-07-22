package com.mx.ymate.mqtt.bean;

import com.mx.ymate.dev.util.ConfigUtil;
import com.mx.ymate.mqtt.impl.DefaultMqttCallbackExtended;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.commons.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;

import static com.mx.ymate.mqtt.IMqttConfig.*;
import static com.mx.ymate.mqtt.IMqttConfig.MQTT_VERSION_3_1_1;
import static org.eclipse.paho.client.mqttv3.MqttConnectOptions.*;
import static org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory.DEFAULT_PROTOCOL;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/5.
 * @Time: 13:05.
 * @Description:
 */
public class MqttConfig {

    /**
     * 客户端名称
     */
    private String name;

    /**
     * 连接地址
     */
    private String url;

    /**
     * 客户端id {time}代表增加默认时间戳 {uuid}代表增加随机uuid  没有则取本身
     */
    private String clientId;

    /**
     * 用户名默认不设置
     */
    private String userName;

    /**
     * 密码默认不设置
     */
    private String password;

    /**
     * mqtt回调实现类 不自动重连继承 AbstractMqttCallbackExtended即可
     * 自动重连继承AbstractAutoConnectMqttCallbackExtended会有自动订阅的功能
     */
    private MqttCallbackExtended callback;

    /**
     * 是否清理session，false时可接收离线消息 true忽略离线消息 默认true
     */
    private boolean cleanSession;

    /**
     * 是否手动确认消息 默认false
     */
    private boolean manualAcks;

    /**
     * 超时时间 默认30s
     */
    private int connectionTimeout;

    /**
     * 心跳时间间隔 默认60s 值为0将禁用客户端中的保活处理
     */
    private int keepAliveInterval;

    /**
     * 设置“最大空中流量”。请在高流量环境中增加此值。默认值为10
     */
    private int maxInflight;

    /**
     * 版本 默认3.1.1
     */
    private String version;

    /**
     * 是否自动重连 默认false
     */
    private boolean automaticReconnection;

    /**
     * 重新连接之间等待的最长时间
     */
    private int reconnectDelay;

    /**
     * 消息保存方式 如果不设置，默认保存在内存中，设置了则保存着指定的目录下
     */
    private String storageDir;

    //ssl配置
    /**
     * 是否启用 SSL 加密连接 默认false
     */
    private boolean sslEnabled;

    /**
     * 服务端证书文件
     */
    private String sslTruststore;

    /**
     * 服务端证书密码
     */
    private String sslTruststorePassword;

    /**
     * 服务端加密类型
     */
    private String sslTruststoreType;

    /**
     * 本地证书文件（KeyStore），用于向服务端提供客户端身份认证（可选）。
     */
    private String sslKeystore;

    /**
     * 本地证书密码
     */
    private String sslKeystorePassword;

    /**
     * 本地加密类型
     */
    private String sslKeystoreType;

    /**
     * 使用的 SSL/TLS 协议版本 默认TLSv1.2
     */
    private String sslProtocol;

    //遗嘱消息
    /**
     * 是否启用遗嘱消息 默认false
     */
    private boolean willEnabled;
    /**
     * 要发布到的主题
     */
    private String willTopic;

    /**
     * 消息的字节有效负载
     */
    private String willPayload;

    /**
     * qos–在（0、1或2）发布消息的服务质量
     */
    private int willQos;

    /**
     * 是否应保留消息
     */
    private boolean willRetained;

    private static String buildClientId(String clientId) {
        if (clientId.contains(EX_TIME)) {
            clientId = clientId.replace(EX_TIME, BlurObject.bind(DateTimeUtils.currentTimeMillis()).toStringValue());
        } else if (clientId.contains(EX_UUID)) {
            clientId = clientId.replace(EX_UUID, UUIDUtils.UUID());
        }
        if (StringUtils.isBlank(clientId)) {
            clientId = "mqttClientId-" + UUIDUtils.UUID();
        }
        return clientId;
    }


    public static MqttConfig buildConfig(String name, ConfigUtil configUtil) {
        MqttConfig mqttConfig = new MqttConfig();
        mqttConfig.setName(name);
        mqttConfig.setUrl(configUtil.getString(URL));
        mqttConfig.setClientId(buildClientId(configUtil.getString(CLIENT_ID)));
        mqttConfig.setUserName(configUtil.getString(USER_NAME));
        mqttConfig.setPassword(configUtil.getString(PASSWORD));
        MqttCallbackExtended callback = configUtil.getClassImpl(CALLBACK, MqttCallbackExtended.class);
        if (callback == null) {
            callback = new DefaultMqttCallbackExtended();
        }
        mqttConfig.setCallback(callback);
        mqttConfig.setCleanSession(configUtil.getBoolean(CLEAN_SESSION, CLEAN_SESSION_DEFAULT));
        mqttConfig.setManualAcks(configUtil.getBoolean(MANUAL_ACKS, false));
        mqttConfig.setConnectionTimeout(configUtil.getInt(CONNECTION_TIMEOUT, CONNECTION_TIMEOUT_DEFAULT));
        mqttConfig.setKeepAliveInterval(configUtil.getInt(KEEP_ALIVE_INTERVAL, KEEP_ALIVE_INTERVAL_DEFAULT));
        mqttConfig.setMaxInflight(configUtil.getInt(MAX_INFLIGHT, MAX_INFLIGHT_DEFAULT));
        mqttConfig.setVersion(configUtil.getString(VERSION, MQTT_VERSION_3_1_1));
        mqttConfig.setAutomaticReconnection(configUtil.getBoolean(AUTOMATIC_RECONNECTION, false));
        mqttConfig.setReconnectDelay(configUtil.getInt(RECONNECT_DELAY, 128000));
        mqttConfig.setStorageDir(configUtil.getString(STORAGE_DIR));
        boolean sslEnabled = configUtil.getBool(SSL_ENABLED, false);
        mqttConfig.setSslEnabled(sslEnabled);
        if (sslEnabled) {
            mqttConfig.setSslTruststore(configUtil.getString(SSL_TRUSTSTORE));
            mqttConfig.setSslTruststorePassword(configUtil.getString(SSL_TRUSTSTORE_PASSWORD));
            mqttConfig.setSslTruststoreType(configUtil.getString(SSL_TRUSTSTORE_TYPE));
            String sslKeyStore = configUtil.getString(SSL_KEYSTORE);
            if (StringUtils.isNotBlank(sslKeyStore)) {
                mqttConfig.setSslKeystore(sslKeyStore);
                mqttConfig.setSslKeystorePassword(configUtil.getString(SSL_KEYSTORE_PASSWORD));
                mqttConfig.setSslKeystoreType(configUtil.getString(SSL_KEYSTORE_TYPE));
            }
            mqttConfig.setSslProtocol(configUtil.getString(SSL_PROTOCOL, DEFAULT_PROTOCOL));
        }
        boolean willEnabled = configUtil.getBool(configUtil.getString(WILL_ENABLED), false);
        mqttConfig.setWillEnabled(willEnabled);
        if (willEnabled) {
            mqttConfig.setWillTopic(configUtil.getString(WILL_TOPIC));
            mqttConfig.setWillPayload(configUtil.getString(WILL_PAYLOAD));
            mqttConfig.setWillQos(configUtil.getInt(WILL_QOS));
            mqttConfig.setWillRetained(configUtil.getBoolean(WILL_RETAINED, false));
        }
        return mqttConfig;
    }

    public MqttConfig() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MqttCallbackExtended getCallback() {
        return callback;
    }

    public void setCallback(MqttCallbackExtended callback) {
        this.callback = callback;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public boolean isManualAcks() {
        return manualAcks;
    }

    public void setManualAcks(boolean manualAcks) {
        this.manualAcks = manualAcks;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getKeepAliveInterval() {
        return keepAliveInterval;
    }

    public void setKeepAliveInterval(int keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
    }

    public int getMaxInflight() {
        return maxInflight;
    }

    public void setMaxInflight(int maxInflight) {
        this.maxInflight = maxInflight;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isAutomaticReconnection() {
        return automaticReconnection;
    }

    public void setAutomaticReconnection(boolean automaticReconnection) {
        this.automaticReconnection = automaticReconnection;
    }

    public int getReconnectDelay() {
        return reconnectDelay;
    }

    public void setReconnectDelay(int reconnectDelay) {
        this.reconnectDelay = reconnectDelay;
    }

    public String getStorageDir() {
        return storageDir;
    }

    public void setStorageDir(String storageDir) {
        this.storageDir = storageDir;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public String getSslTruststore() {
        return sslTruststore;
    }

    public void setSslTruststore(String sslTruststore) {
        this.sslTruststore = sslTruststore;
    }

    public String getSslTruststorePassword() {
        return sslTruststorePassword;
    }

    public void setSslTruststorePassword(String sslTruststorePassword) {
        this.sslTruststorePassword = sslTruststorePassword;
    }

    public String getSslTruststoreType() {
        return sslTruststoreType;
    }

    public void setSslTruststoreType(String sslTruststoreType) {
        this.sslTruststoreType = sslTruststoreType;
    }

    public String getSslKeystore() {
        return sslKeystore;
    }

    public void setSslKeystore(String sslKeystore) {
        this.sslKeystore = sslKeystore;
    }

    public String getSslKeystorePassword() {
        return sslKeystorePassword;
    }

    public void setSslKeystorePassword(String sslKeystorePassword) {
        this.sslKeystorePassword = sslKeystorePassword;
    }

    public String getSslKeystoreType() {
        return sslKeystoreType;
    }

    public void setSslKeystoreType(String sslKeystoreType) {
        this.sslKeystoreType = sslKeystoreType;
    }

    public String getSslProtocol() {
        return sslProtocol;
    }

    public void setSslProtocol(String sslProtocol) {
        this.sslProtocol = sslProtocol;
    }

    public boolean isWillEnabled() {
        return willEnabled;
    }

    public void setWillEnabled(boolean willEnabled) {
        this.willEnabled = willEnabled;
    }

    public String getWillTopic() {
        return willTopic;
    }

    public void setWillTopic(String willTopic) {
        this.willTopic = willTopic;
    }

    public String getWillPayload() {
        return willPayload;
    }

    public void setWillPayload(String willPayload) {
        this.willPayload = willPayload;
    }

    public int getWillQos() {
        return willQos;
    }

    public void setWillQos(int willQos) {
        this.willQos = willQos;
    }

    public boolean isWillRetained() {
        return willRetained;
    }

    public void setWillRetained(boolean willRetained) {
        this.willRetained = willRetained;
    }
}
