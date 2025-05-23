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

import cn.hutool.core.util.ObjectUtil;
import com.mx.ymate.dev.util.ConfigUtil;
import com.mx.ymate.netty.AbstractHeartBeatHandler;
import com.mx.ymate.netty.INetty;
import com.mx.ymate.netty.INettyConfig;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.module.IModuleConfigurer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mx.ymate.dev.constants.Constants.XG;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public final class DefaultNettyConfig implements INettyConfig {

    private boolean enabled;
    private Boolean autoStart;
    private String client;

    private Integer serverPort;
    private Integer serverStartPort;
    private Integer serverEndPort;
    private List<String> serverExcludePort;
    private List<Integer> serverHeartBeatTimeList;
    private AbstractHeartBeatHandler serverHeart;
    private final List<ChannelInboundHandlerAdapter> serverHandler = new ArrayList<>();
    private String serverDecoderClassName;

    private Integer clientNum;
    private List<String> clientRemoteAddress;
    private List<Integer> clientHeartBeatTimeList;
    private AbstractHeartBeatHandler clientHeart;
    private final List<ChannelInboundHandlerAdapter> clientHandler = new ArrayList<>();
    private String clientDecoderClassName;

    private boolean websocketEnabled;
    private int websocketPort;
    private List<Integer> websocketHeartBeatTimeList;
    private AbstractHeartBeatHandler websocketHeart;
    private String websocketMapping;
    private String websocketPackage;


    private boolean initialized;


    public static DefaultNettyConfig create(IModuleConfigurer moduleConfigurer) {
        return new DefaultNettyConfig(moduleConfigurer);
    }


    private DefaultNettyConfig() {
    }

    private DefaultNettyConfig(IModuleConfigurer moduleConfigurer) {
        ConfigUtil configUtil = new ConfigUtil(moduleConfigurer.getConfigReader().toMap());
        enabled = configUtil.getBool(ENABLED, true);
        autoStart = configUtil.getBool(AUTO_START, true);
        client = configUtil.getString(CLIENT, "all");
        serverPort = configUtil.getInteger(SERVER_PORT);
        serverStartPort = configUtil.getInteger(SERVER_START_PORT);
        serverEndPort = configUtil.getInteger(SERVER_END_PORT);
        serverExcludePort = ObjectUtil.defaultIfNull(configUtil.getList(SERVER_EXCLUDE_PORT), new ArrayList<>());
        List<String> heartBeatTimeTempList = ObjectUtil.defaultIfNull(configUtil.getList(SERVER_HEART_BEAT_TIME), new ArrayList<>());
        if (heartBeatTimeTempList.size() == HEART_BEAT_TIME_ITEM_COUNT) {
            serverHeartBeatTimeList = heartBeatTimeTempList.stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
        }
        serverHeart = configUtil.getClassImpl(SERVER_HEART_BEAT_CLASS, AbstractHeartBeatHandler.class);
        if (serverHeart == null) {
            serverHeart = new AbstractHeartBeatHandler.DefaultServerHeartImpl();
        }
        List<String> serverHandlerClassNameList = ObjectUtil.defaultIfNull(configUtil.getList(SERVER_HANDLER_CLASS), new ArrayList<>());
        if (!serverHandlerClassNameList.isEmpty()) {
            for (String className : serverHandlerClassNameList) {
                serverHandler.add(ClassUtils.impl(className, ChannelInboundHandlerAdapter.class, this.getClass()));
            }
        }
        serverDecoderClassName = configUtil.getString(SERVER_DECODER_CLASS);
        clientNum = configUtil.getInteger(CLIENT_NUM, 1);
        clientRemoteAddress = ObjectUtil.defaultIfNull(configUtil.getList(CLIENT_REMOTE_ADDRESS), new ArrayList<>());
        heartBeatTimeTempList = ObjectUtil.defaultIfNull(configUtil.getList(CLIENT_HEART_BEAT_TIME), new ArrayList<>());
        if (heartBeatTimeTempList.size() == HEART_BEAT_TIME_ITEM_COUNT) {
            clientHeartBeatTimeList = heartBeatTimeTempList.stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
        }
        clientHeart = configUtil.getClassImpl(CLIENT_HEART_BEAT_CLASS, AbstractHeartBeatHandler.class);
        if (clientHeart == null) {
            clientHeart = new AbstractHeartBeatHandler.DefaultClientHeartImpl();
        }
        List<String> clientHandlerClassNameList = ObjectUtil.defaultIfNull(configUtil.getList(CLIENT_HANDLER_CLASS), new ArrayList<>());
        if (!clientHandlerClassNameList.isEmpty()) {
            for (String className : clientHandlerClassNameList) {
                clientHandler.add(ClassUtils.impl(className, ChannelInboundHandlerAdapter.class, this.getClass()));
            }
        }
        clientDecoderClassName = configUtil.getString(CLIENT_DECODER_CLASS);

        websocketEnabled = configUtil.getBool(WEBSOCKET_ENABLED, false);
        websocketPort = configUtil.getInt(WEBSOCKET_PORT, 8756);
        heartBeatTimeTempList = ObjectUtil.defaultIfNull(configUtil.getList(WEBSOCKET_HEART_BEAT_TIME), new ArrayList<>());
        if (heartBeatTimeTempList.size() == HEART_BEAT_TIME_ITEM_COUNT) {
            websocketHeartBeatTimeList = heartBeatTimeTempList.stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
        }
        websocketHeart = configUtil.getClassImpl(WEBSOCKET_HEART_BEAT_CLASS, AbstractHeartBeatHandler.class);
        if (websocketHeart == null) {
            websocketHeart = new AbstractHeartBeatHandler.DefaultWebsocketHeartImpl();
        }
        websocketMapping = configUtil.getString(WEBSOCKET_MAPPING, "/websocket");
        if (!websocketMapping.startsWith(XG)) {
            websocketMapping = XG + websocketMapping;
        }
        websocketPackage = configUtil.getString(WEBSOCKET_PACKAGE);
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
    public Boolean autoStart() {
        return autoStart;
    }

    @Override
    public String client() {
        return client;
    }

    @Override
    public Integer serverPort() {
        return serverPort;
    }

    @Override
    public Integer serverStartPort() {
        return serverStartPort;
    }

    @Override
    public Integer serverEndPort() {
        return serverEndPort;
    }

    @Override
    public List<Integer> serverHeartBeatTimeList() {
        return serverHeartBeatTimeList;
    }

    @Override
    public AbstractHeartBeatHandler serverHeart() {
        return serverHeart;
    }

    @Override
    public List<String> serverExcludePort() {
        return serverExcludePort;
    }

    @Override
    public List<ChannelInboundHandlerAdapter> serverHandler() {
        return serverHandler;
    }

    @Override
    public ChannelInboundHandlerAdapter serverDecoder() {
        if (StringUtils.isNotBlank(serverDecoderClassName)) {
            return ClassUtils.impl(serverDecoderClassName, ChannelInboundHandlerAdapter.class, this.getClass());
        }
        return null;
    }

    @Override
    public Integer clientNum() {
        return clientNum;
    }

    @Override
    public List<String> clientRemoteAddress() {
        return clientRemoteAddress;
    }

    @Override
    public List<Integer> clientHeartBeatTimeList() {
        return clientHeartBeatTimeList;
    }

    @Override
    public AbstractHeartBeatHandler clientHeart() {
        return clientHeart;
    }

    @Override
    public List<ChannelInboundHandlerAdapter> clientHandler() {
        return clientHandler;
    }

    @Override
    public ChannelInboundHandlerAdapter clientDecoder() {
        if (StringUtils.isNotBlank(clientDecoderClassName)) {
            return ClassUtils.impl(clientDecoderClassName, ChannelInboundHandlerAdapter.class, this.getClass());
        }
        return null;
    }

    @Override
    public boolean websocketEnabled() {
        return websocketEnabled;
    }

    @Override
    public int websocketPort() {
        return websocketPort;
    }

    @Override
    public List<Integer> websocketHeartBeatTimeList() {
        return websocketHeartBeatTimeList;
    }

    @Override
    public AbstractHeartBeatHandler websocketHeart() {
        return websocketHeart;
    }

    @Override
    public String websocketMapping() {
        return websocketMapping;
    }

    @Override
    public String websocketPackage() {
        return websocketPackage;
    }

}
