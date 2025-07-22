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

import com.mx.ymate.dev.util.ConfigUtil;
import com.mx.ymate.mqtt.IMqtt;
import com.mx.ymate.mqtt.IMqttConfig;
import com.mx.ymate.mqtt.bean.MqttConfig;
import net.ymate.platform.core.configuration.IConfigReader;
import net.ymate.platform.core.configuration.impl.MapSafeConfigReader;
import net.ymate.platform.core.module.IModuleConfigurer;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public final class DefaultMqttConfig implements IMqttConfig {

    private final Map<String, MqttConfig> CONFIG_MAP = new HashMap<>();

    private boolean enabled;

    private boolean autoInit;

    private boolean initialized;

    private final List<MqttConfig> configList = new ArrayList<>();


    public static DefaultMqttConfig create(IModuleConfigurer moduleConfigurer) {
        return new DefaultMqttConfig(moduleConfigurer);
    }


    private DefaultMqttConfig() {
    }

    private DefaultMqttConfig(IModuleConfigurer moduleConfigurer) {
        IConfigReader allConfigReader = moduleConfigurer.getConfigReader();
        ConfigUtil allConfigUtil = new ConfigUtil(allConfigReader.toMap());
        enabled = allConfigUtil.getBoolean(ENABLED, true);
        autoInit = allConfigUtil.getBoolean(AUTO_INIT, false);
        String[] nameList = StringUtils.split(allConfigUtil.getString(NAME, DEFAULT_NAME), "|");
        for (String name : nameList) {
            if (CONFIG_MAP.containsKey(name)) {
                throw new IllegalArgumentException("重复的MQTT配置名称: " + name);
            }
            Map<String, String> configMap = allConfigUtil.getMap(String.format("%s.", name));
            if (configMap.isEmpty()) {
                continue;
            }
            IConfigReader configReader = MapSafeConfigReader.bind(configMap);
            ConfigUtil configUtil = new ConfigUtil(configReader.toMap());
            MqttConfig mqttConfig = MqttConfig.buildConfig(name, configUtil);
            CONFIG_MAP.put(name, mqttConfig);
            configList.add(mqttConfig);
        }

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
    public boolean autoInit() {
        return autoInit;
    }

    @Override
    public List<MqttConfig> configList() {
        return configList;
    }

    @Override
    public MqttConfig mqttConfig(String name) {
        return CONFIG_MAP.get(name);
    }


}