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
package com.mx.ymate.netty.impl;

import com.mx.ymate.dev.util.ConfigUtil;
import com.mx.ymate.netty.INetty;
import com.mx.ymate.netty.INettyConfig;
import com.mx.ymate.netty.bean.ClientConfig;
import com.mx.ymate.netty.bean.ServerConfig;
import com.mx.ymate.netty.bean.WebsocketConfig;
import net.ymate.platform.core.configuration.IConfigReader;
import net.ymate.platform.core.configuration.impl.MapSafeConfigReader;
import net.ymate.platform.core.module.IModuleConfigurer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public final class DefaultNettyConfig implements INettyConfig {

    private boolean enabled;
    private final Map<String, String> SERVER_NAME_ADDRESS_MAP = new HashMap<>();
    private final Map<String, ServerConfig> SERVER_CONFIG_MAP = new HashMap<>();
    private final List<ServerConfig> serverConfigList = new ArrayList<>();
    private final Map<String, ClientConfig> CLIENT_CONFIG_MAP = new HashMap<>();
    private final List<ClientConfig> clientConfigList = new ArrayList<>();
    private WebsocketConfig websocketConfig;
    private boolean serverAutoInit;
    private boolean clientAutoInit;
    private boolean websocketAutoInit;
    private boolean initialized;


    public static DefaultNettyConfig create(IModuleConfigurer moduleConfigurer) {
        return new DefaultNettyConfig(moduleConfigurer);
    }


    private DefaultNettyConfig() {
    }

    private DefaultNettyConfig(IModuleConfigurer moduleConfigurer) {
        //对于不能自动装配的参数进行设置
        IConfigReader configReader = moduleConfigurer.getConfigReader();
        ConfigUtil configUtil = new ConfigUtil(configReader.toMap());
        enabled = configUtil.getBool(ENABLED, true);
        serverAutoInit = configUtil.getBool(ServerConfig.AUTO_INIT, false);
        clientAutoInit = configUtil.getBool(ClientConfig.AUTO_INIT, false);
        websocketAutoInit = configUtil.getBool(WebsocketConfig.AUTO_INIT, false);
        buildServerConfigList(configUtil);
        buildClientConfigList(configUtil);
        websocketConfig = WebsocketConfig.buildConfig(configUtil);
    }

    /**
     * 构建服务端配置
     *
     * @param allConfigUtil
     */
    private void buildServerConfigList(ConfigUtil allConfigUtil) {
        String[] nameList = StringUtils.split(allConfigUtil.getString(ServerConfig.NAME, DEFAULT_NAME), "|");
        for (String name : nameList) {
            if (SERVER_CONFIG_MAP.containsKey(name)) {
                throw new IllegalArgumentException("重复的Netty 服务端配置名称: " + name);
            }
            Map<String, String> configMap = allConfigUtil.getMap(String.format("server.%s.", name));
            if (configMap.isEmpty()) {
                continue;
            }
            IConfigReader configReader = MapSafeConfigReader.bind(configMap);
            ConfigUtil configUtil = new ConfigUtil(configReader.toMap());
            ServerConfig serverConfig = ServerConfig.buildConfig(name, configUtil);
            SERVER_NAME_ADDRESS_MAP.put(serverConfig.getHost() + serverConfig.getPort(), name);
            SERVER_CONFIG_MAP.put(name, serverConfig);
            serverConfigList.add(serverConfig);
        }
    }

    /**
     * 构建客户端配置
     *
     * @param allConfigUtil
     */
    private void buildClientConfigList(ConfigUtil allConfigUtil) {
        String[] nameList = StringUtils.split(allConfigUtil.getString(ClientConfig.NAME, DEFAULT_NAME), "|");
        for (String name : nameList) {
            if (CLIENT_CONFIG_MAP.containsKey(name)) {
                throw new IllegalArgumentException("重复的Netty 客户端配置名称: " + name);
            }
            Map<String, String> configMap = allConfigUtil.getMap(String.format("client.%s.", name));
            if (configMap.isEmpty()) {
                continue;
            }
            IConfigReader configReader = MapSafeConfigReader.bind(configMap);
            ConfigUtil configUtil = new ConfigUtil(configReader.toMap());
            ClientConfig clientConfig = ClientConfig.buildConfig(name, configUtil);
            CLIENT_CONFIG_MAP.put(name, clientConfig);
            clientConfigList.add(clientConfig);
        }
    }


    @Override
    public void initialize(INetty owner) throws Exception {
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
    public boolean serverAutoInit() {
        return serverAutoInit;
    }

    @Override
    public boolean clientAutoInit() {
        return clientAutoInit;
    }

    @Override
    public boolean websocketAutoInit() {
        return websocketAutoInit;
    }

    @Override
    public String getServerName(String address) {
        return SERVER_NAME_ADDRESS_MAP.get(address);
    }

    @Override
    public List<ServerConfig> serverConfigList() {
        return serverConfigList;
    }

    @Override
    public ServerConfig serverConfig(String serverName) {
        return SERVER_CONFIG_MAP.get(serverName);
    }

    @Override
    public List<ClientConfig> clientConfigList() {
        return clientConfigList;
    }

    @Override
    public ClientConfig clientConfig(String clientName) {
        return CLIENT_CONFIG_MAP.get(clientName);
    }

    @Override
    public WebsocketConfig websocketConfig() {
        return websocketConfig;
    }

}
