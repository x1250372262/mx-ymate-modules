/*
 * Copyright 2022 the original author or authors.
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
package com.mx.ymate.netty;

import com.mx.ymate.netty.bean.ClientConfig;
import com.mx.ymate.netty.bean.ServerConfig;
import com.mx.ymate.netty.bean.WebsocketConfig;
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
public interface INettyConfig extends IInitialization<INetty> {

    String ENABLED = "enabled";
    String DEFAULT_NAME = "default";
    int HEART_BEAT_TIME_ITEM_COUNT = 3;

    /**
     * 模块是否已启用, 默认值: true
     *
     * @return 返回false表示禁用
     */
    boolean isEnabled();

    /**
     * 是否自动初始化服务端 默认false
     *
     * @return
     */
    boolean serverAutoInit();

    /**
     * 是否自动初始化客户端 默认false
     *
     * @return
     */
    boolean clientAutoInit();

    /**
     * 是否自动初始化websocket 默认false
     *
     * @return
     */
    boolean websocketAutoInit();

    /**
     * 根据服务地址获取服务名
     *
     * @param address
     * @return
     */
    String getServerName(String address);

    /**
     *
     * 服务端配置
     *
     * @return
     */
    List<ServerConfig> serverConfigList();

    /**
     * 某个服务端配置
     *
     * @param serverName
     * @return
     */
    ServerConfig serverConfig(String serverName);

    /**
     *
     * 客户端配置
     *
     * @return
     */
    List<ClientConfig> clientConfigList();

    /**
     * 某个客户端配置
     *
     * @param clientName
     * @return
     */
    ClientConfig clientConfig(String clientName);


    /**
     * websocket配置
     *
     * @param
     * @return
     */
    WebsocketConfig websocketConfig();

}
