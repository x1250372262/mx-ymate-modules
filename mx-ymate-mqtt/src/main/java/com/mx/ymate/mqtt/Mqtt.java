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

import com.mx.ymate.mqtt.bean.MqttContext;
import com.mx.ymate.mqtt.event.MqttEvent;
import com.mx.ymate.mqtt.impl.DefaultMqttConfig;
import com.mx.ymate.mqtt.impl.MqttManager;
import net.ymate.platform.core.*;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.IModuleConfigurer;
import net.ymate.platform.core.module.impl.DefaultModuleConfigurer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    private MqttManager mqttManager;


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
            if (config.isEnabled() && config.autoInit()) {
                //等待框架启动成功
                mqttManager = new MqttManager();
                mqttManager.initAll(config.configList());
            }
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
            if (config.isEnabled() && config.autoInit()) {
                mqttManager.destroyAll();
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
    public MqttManager getManager() {
        return mqttManager;
    }

    @Override
    public void fireEvent(MqttEvent.EVENT event, MqttContext mqttContext) {
        MqttEvent mqttEvent = new MqttEvent(owner, event);
        mqttEvent.addParamExtend("mqttContext", mqttContext);
        owner.getEvents().fireEvent(mqttEvent);
    }


}
