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
import com.mx.ymate.mqtt.impl.MqttManager;
import net.ymate.platform.core.IApplication;
import net.ymate.platform.core.beans.annotation.Ignored;
import net.ymate.platform.core.support.IDestroyable;
import net.ymate.platform.core.support.IInitialization;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;

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
     * 发布事件
     *
     * @param event
     * @param mqttContext
     */
    void fireEvent(MqttEvent.EVENT event, MqttContext mqttContext);


    /**
     * 获取manager对象
     *
     * @return
     */
    MqttManager getManager();


}
