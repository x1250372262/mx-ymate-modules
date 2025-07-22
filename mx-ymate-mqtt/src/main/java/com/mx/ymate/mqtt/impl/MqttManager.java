package com.mx.ymate.mqtt.impl;

import cn.hutool.core.util.StrUtil;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.mqtt.bean.MqttConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.mx.ymate.mqtt.IMqttConfig.DEFAULT_NAME;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/9.
 * @Time: 15:31.
 * @Description:
 */
public class MqttManager {

    private final Map<String, MqttClient> MQTT_MAP = new ConcurrentHashMap<>();
    private final Log LOG = LogFactory.getLog(MqttManager.class);
    private final List<MqttConfig> configList;

    public MqttManager(List<MqttConfig> configList) {
        this.configList = configList;
    }


    public void initAll() {
        for (MqttConfig mqttConfig : configList) {
            String name = mqttConfig.getName();
            if (MQTT_MAP.containsKey(name)) {
                LOG.warn(StrUtil.format("MQTT[{}] 已初始化，忽略重复初始化", name));
                continue;
            }
            MqttClient result = new MqttClient(mqttConfig).init();
            if (result == null) {
                LOG.error(StrUtil.format("MQTT[{}] 初始化失败", name));
                continue;
            }
            MQTT_MAP.put(name, result);
        }
    }

    public void init(String name) {
        MqttConfig targetConfig = configList.stream()
                .filter(cfg -> cfg.getName().equals(name))
                .findFirst()
                .orElse(null);

        if (targetConfig == null) {
            LOG.error(StrUtil.format("未找到名称为 [{}] 的MQTT配置", name));
            return;
        }

        if (MQTT_MAP.containsKey(name)) {
            LOG.warn(StrUtil.format("MQTT[{}] 已初始化，忽略重复初始化", name));
            return;
        }
        MqttClient result = new MqttClient(targetConfig).init();
        if (result == null) {
            LOG.error(StrUtil.format("MQTT[{}] 初始化失败", name));
            return;
        }
        MQTT_MAP.put(name, result);
    }

    public MqttClient get(String name) {
        MqttClient mqttClient = MQTT_MAP.get(name);
        if (mqttClient == null) {
            LOG.error(StrUtil.format("MQTT[{}] 没有初始化，请先初始化", name));
            throw new RuntimeException(StrUtil.format("MQTT[{}] 没有初始化，请先初始化", name));
        }
        return mqttClient;
    }

    public MqttClient get() {
        return get(DEFAULT_NAME);
    }

    public MqttAsyncClient getMqttAsyncClient(String name) {
        return get(name).getClient();
    }

    public void connectAll() {
        MQTT_MAP.forEach((name, mqttClient) -> mqttClient.connect(null));
    }

    public void connectAll(Map<String, Object> extras) {
        MQTT_MAP.forEach((name, mqttClient) -> mqttClient.connect(extras));
    }

    public void disconnectAll() {
        MQTT_MAP.forEach((name, mqttClient) -> mqttClient.disconnect());
    }

    public void destroyAll() {
        MQTT_MAP.forEach((name, mqttClient) -> mqttClient.destroy());
    }


    public MxResult add(MqttConfig targetConfig) {
        String name = targetConfig.getName();
        if (MQTT_MAP.containsKey(name)) {
            return MxResult.fail().msg(StrUtil.format("MQTT[{}] 已存在", name));
        }
        MqttClient result = new MqttClient(targetConfig).init();
        if (result == null) {
            return MxResult.fail().msg(StrUtil.format("MQTT[{}] 初始化失败", name));
        }
        MQTT_MAP.put(name, result);
        return MxResult.ok();
    }

    public MxResult addAndConnect(MqttConfig targetConfig) {
        return addAndConnect(targetConfig,null);
    }

    public MxResult addAndConnect(MqttConfig targetConfig, Map<String, Object> extras) {
        MxResult mxResult = add(targetConfig);
        if(mxResult.isSuccess()){
            get(targetConfig.getName()).connect(extras);
        }
        return mxResult;
    }


    public MxResult remove(String name) {
        MqttClient mqttClient = MQTT_MAP.remove(name);
        if (mqttClient == null) {
            return MxResult.ok();
        }
        mqttClient.unSubscribe();
        mqttClient.disconnect();
        mqttClient.destroy();
        return MxResult.ok();
    }

}
