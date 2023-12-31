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
import com.mx.ymate.netty.INetty;
import com.mx.ymate.netty.INettyConfig;
import com.mx.ymate.netty.heart.IHeartClient;
import com.mx.ymate.netty.heart.IHeartServer;
import com.mx.ymate.netty.heart.impl.HeartClientImpl;
import com.mx.ymate.netty.heart.impl.HeartServerImpl;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.module.IModuleConfigurer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * DefaultNettyConfig generated By ModuleMojo on 2022/07/07 10:19
 *
 * @author YMP (https://www.ymate.net/)
 */
public final class DefaultNettyConfig implements INettyConfig {

    private boolean enabled;
    private Boolean autoStart;
    private String client;
    private Integer serverPort;
    private Integer serverStartPort;
    private Integer serverEndPort;
    private Integer serverHeartBeatTime;

    private IHeartServer heartServer;
    private List<String> serverExcludePort;
    private List<ChannelInboundHandlerAdapter> serverHandler = new ArrayList<>();
    private ChannelInboundHandlerAdapter serverDecoder;
    private List<String> clientRemoteAddress;
    private Integer clientHeartBeatTime;

    private IHeartClient heartClient;
    private List<ChannelInboundHandlerAdapter> clientHandler = new ArrayList<>();
    private ChannelInboundHandlerAdapter clientDecoder;
    private String serverDecoderClassName;
    private String clientDecoderClassName;

    private boolean initialized;


    public static DefaultNettyConfig defaultConfig() {
        return builder().build();
    }

    public static DefaultNettyConfig create(IModuleConfigurer moduleConfigurer) {
        return new DefaultNettyConfig(moduleConfigurer);
    }

    public static Builder builder() {
        return new Builder();
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
        serverHeartBeatTime = configUtil.getInteger(SERVER_HEART_BEAT_TIME);
        heartServer = configUtil.getClassImpl(SERVER_HEART_BEAT_CLASS,IHeartServer.class);
        if(heartServer == null){
            heartServer = new HeartServerImpl();
        }
        serverExcludePort = ObjectUtil.defaultIfNull(configUtil.getList(SERVER_EXCLUDE_PORT), new ArrayList<>());
        List<String> serverHandlerClassNameList = ObjectUtil.defaultIfNull(configUtil.getList(SERVER_HANDLER_CLASS), new ArrayList<>());
        if (!serverHandlerClassNameList.isEmpty()) {
            for (String className : serverHandlerClassNameList) {
                serverHandler.add(ClassUtils.impl(className, ChannelInboundHandlerAdapter.class, this.getClass()));
            }
        }

        serverDecoderClassName = configUtil.getString(SERVER_DECODER_CLASS);

        clientRemoteAddress = ObjectUtil.defaultIfNull(configUtil.getList(CLIENT_REMOTE_ADDRESS), new ArrayList<>());
        clientHeartBeatTime = configUtil.getInteger(CLIENT_HEART_BEAT_TIME);
        heartClient = configUtil.getClassImpl(CLIENT_HEART_BEAT_CLASS,IHeartClient.class);
        if(heartClient == null){
            heartClient = new HeartClientImpl();
        }
        List<String> clientHandlerClassNameList = ObjectUtil.defaultIfNull(configUtil.getList(CLIENT_HANDLER_CLASS), new ArrayList<>());
        if (!clientHandlerClassNameList.isEmpty()) {
            for (String className : clientHandlerClassNameList) {
                clientHandler.add(ClassUtils.impl(className, ChannelInboundHandlerAdapter.class, this.getClass()));
            }
        }
        clientDecoderClassName = configUtil.getString(CLIENT_DECODER_CLASS);

    }

    @Override
    public void initialize(INetty owner) throws Exception {
        if (!initialized) {
            if (enabled) {
                // TODO What to do?
            }
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
    public Integer serverHeartBeatTime() {
        return serverHeartBeatTime;
    }

    @Override
    public IHeartServer heartServer() {
        return heartServer;
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
            serverDecoder = ClassUtils.impl(serverDecoderClassName, ChannelInboundHandlerAdapter.class, this.getClass());
        }

        return serverDecoder;
    }

    @Override
    public List<String> clientRemoteAddress() {
        return clientRemoteAddress;
    }

    @Override
    public Integer clientHeartBeatTime() {
        return clientHeartBeatTime;
    }

    @Override
    public IHeartClient heartClient() {
        return heartClient;
    }

    @Override
    public List<ChannelInboundHandlerAdapter> clientHandler() {
        return clientHandler;
    }

    @Override
    public ChannelInboundHandlerAdapter clientDecoder() {
        if (StringUtils.isNotBlank(clientDecoderClassName)) {
            clientDecoder = ClassUtils.impl(clientDecoderClassName, ChannelInboundHandlerAdapter.class, this.getClass());
        }
        return clientDecoder;
    }

    public void setEnabled(boolean enabled) {
        if (!initialized) {
            this.enabled = enabled;
        }
    }

    public void setAutoStart(Boolean autoStart) {
        if (!initialized) {
            this.autoStart = autoStart;
        }
    }

    public void setClient(String client) {
        if (!initialized) {
            this.client = client;
        }
    }

    public void setServerPort(Integer serverPort) {
        if (!initialized) {
            this.serverPort = serverPort;
        }
    }

    public void setServerStartPort(Integer serverStartPort) {
        if (!initialized) {
            this.serverStartPort = serverStartPort;
        }
    }

    public void setServerEndPort(Integer serverEndPort) {
        if (!initialized) {
            this.serverEndPort = serverEndPort;
        }
    }

    public void setServerHeartBeatTime(Integer serverHeartBeatTime) {
        if (!initialized) {
            this.serverHeartBeatTime = serverHeartBeatTime;
        }
    }

    public void setServerHeartBeatClass(IHeartServer heartServer) {
        if (!initialized) {
            this.heartServer = heartServer;
        }
    }

    public void setServerExcludePort(List<String> serverExcludePort) {
        if (!initialized) {
            this.serverExcludePort = serverExcludePort;
        }
    }

    public void setServerHandlerClass(List<ChannelInboundHandlerAdapter> serverHandler) {
        if (!initialized) {
            this.serverHandler = serverHandler;
        }
    }

    public void setServerDecoderClass(ChannelInboundHandlerAdapter serverDecoder) {
        if (!initialized) {
            this.serverDecoder = serverDecoder;
        }
    }

    public void setClientRemoteAddress(List<String> clientRemoteAddress) {
        if (!initialized) {
            this.clientRemoteAddress = clientRemoteAddress;
        }
    }

    public void setClientHeartBeatTime(Integer clientHeartBeatTime) {
        if (!initialized) {
            this.clientHeartBeatTime = clientHeartBeatTime;
        }
    }

    public void setClientHeartBeatClass(IHeartClient heartClient) {
        if (!initialized) {
            this.heartClient = heartClient;
        }
    }


    public void setClientHandlerClass(List<ChannelInboundHandlerAdapter> clientHandler) {
        if (!initialized) {
            this.clientHandler = clientHandler;
        }
    }

    public void setClientDecoderClass(ChannelInboundHandlerAdapter clientDecoder) {
        if (!initialized) {
            this.clientDecoder = clientDecoder;
        }
    }


    public static final class Builder {

        private final DefaultNettyConfig config = new DefaultNettyConfig();

        private Builder() {
        }

        public Builder enabled(boolean enabled) {
            config.setEnabled(enabled);
            return this;
        }

        public Builder client(String client) {
            config.setClient(client);
            return this;
        }

        public Builder serverPort(Integer serverPort) {
            config.setServerPort(serverPort);
            return this;
        }

        public Builder serverStartPort(Integer serverStartPort) {
            config.setServerStartPort(serverStartPort);
            return this;
        }

        public Builder serverEndPort(Integer serverEndPort) {
            config.setServerEndPort(serverEndPort);
            return this;
        }

        public Builder serverHeartBeatTime(Integer serverHeartBeatTime) {
            config.setServerHeartBeatTime(serverHeartBeatTime);
            return this;
        }

        public Builder serverHeartBeatClass(IHeartServer heartServer) {
            config.setServerHeartBeatClass(heartServer);
            return this;
        }

        public Builder serverExcludePort(List<String> serverExcludePort) {
            config.setServerExcludePort(serverExcludePort);
            return this;
        }

        public Builder serverHandlerClass(List<ChannelInboundHandlerAdapter> serverHandlerClass) {
            config.setServerHandlerClass(serverHandlerClass);
            return this;
        }

        public Builder serverDecoderClass(ChannelInboundHandlerAdapter serverDecoderClass) {
            config.setServerDecoderClass(serverDecoderClass);
            return this;
        }


        public Builder clientRemoteAddress(List<String> clientRemoteAddress) {
            config.setClientRemoteAddress(clientRemoteAddress);
            return this;
        }

        public Builder clientHeartBeatTime(Integer clientHeartBeatTime) {
            config.setClientHeartBeatTime(clientHeartBeatTime);
            return this;
        }

        public Builder clientHeartBeatClass(IHeartClient heartClient) {
            config.setClientHeartBeatClass(heartClient);
            return this;
        }

        public Builder clientHandlerClass(List<ChannelInboundHandlerAdapter> clientHandlerClass) {
            config.setClientHandlerClass(clientHandlerClass);
            return this;
        }

        public Builder clientDecoderClass(ChannelInboundHandlerAdapter clientDecoderClass) {
            config.setClientDecoderClass(clientDecoderClass);
            return this;
        }

        public DefaultNettyConfig build() {
            return config;
        }
    }
}