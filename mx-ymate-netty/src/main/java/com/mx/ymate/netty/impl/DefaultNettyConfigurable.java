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

import com.mx.ymate.netty.INetty;
import com.mx.ymate.netty.INettyConfig;
import com.mx.ymate.netty.heart.IHeartClient;
import com.mx.ymate.netty.heart.IHeartServer;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.ymate.platform.core.module.IModuleConfigurer;
import net.ymate.platform.core.module.impl.DefaultModuleConfigurable;

import java.util.List;

/**
 * DefaultNettyConfig generated By ModuleMojo on 2022/07/07 10:19
 *
 * @author YMP (https://www.ymate.net/)
 */
public final class DefaultNettyConfigurable extends DefaultModuleConfigurable {

    public static Builder builder() {
        return new Builder();
    }

    private DefaultNettyConfigurable() {
        super(INetty.MODULE_NAME);
    }

    public static final class Builder {

        private final DefaultNettyConfigurable configurable = new DefaultNettyConfigurable();

        private Builder() {
        }

        public Builder enabled(boolean enabled) {
            configurable.addConfig(INettyConfig.ENABLED, String.valueOf(enabled));
            return this;
        }

        public Builder autoStart(boolean autoStart) {
            configurable.addConfig(INettyConfig.AUTO_START, String.valueOf(autoStart));
            return this;
        }

        public Builder client(String client) {
            configurable.addConfig(INettyConfig.CLIENT, client);
            return this;
        }

        public Builder serverPort(Integer serverPort) {
            configurable.addConfig(INettyConfig.SERVER_PORT, String.valueOf(serverPort));
            return this;
        }

        public Builder serverStartPort(Integer serverStartPort) {
            configurable.addConfig(INettyConfig.SERVER_START_PORT, String.valueOf(serverStartPort));
            return this;
        }

        public Builder serverEndPort(Integer serverEndPort) {
            configurable.addConfig(INettyConfig.SERVER_END_PORT, String.valueOf(serverEndPort));
            return this;
        }

        public Builder serverHeartBeatTime(Integer serverHeartBeatTime) {
            configurable.addConfig(INettyConfig.SERVER_HEART_BEAT_TIME, String.valueOf(serverHeartBeatTime));
            return this;
        }

        public Builder serverHeartBeatClass(IHeartServer heartServer) {
            configurable.addConfig(INettyConfig.SERVER_HEART_BEAT_CLASS, String.valueOf(heartServer));
            return this;
        }

        public Builder serverExcludePort(List<Integer> serverExcludePort) {
            configurable.addConfig(INettyConfig.SERVER_EXCLUDE_PORT, String.valueOf(serverExcludePort));
            return this;
        }

        public Builder serverHandlerClass(List<Class<? extends ChannelInboundHandlerAdapter>> serverHandlerClass) {
            configurable.addConfig(INettyConfig.SERVER_HANDLER_CLASS, String.valueOf(serverHandlerClass));
            return this;
        }

        public Builder serverDecoderClass(Class<? extends ChannelInboundHandlerAdapter> serverDecoderClass) {
            configurable.addConfig(INettyConfig.SERVER_DECODER_CLASS, String.valueOf(serverDecoderClass));
            return this;
        }


        public Builder clientRemoteAddress(List<String> clientRemoteAddress) {
            configurable.addConfig(INettyConfig.CLIENT_REMOTE_ADDRESS, String.valueOf(clientRemoteAddress));
            return this;
        }

        public Builder clientHeartBeatTime(Integer clientHeartBeatTime) {
            configurable.addConfig(INettyConfig.CLIENT_HEART_BEAT_TIME, String.valueOf(clientHeartBeatTime));
            return this;
        }

        public Builder  clientHeartBeatClass(IHeartClient heartClient) {
            configurable.addConfig(INettyConfig.CLIENT_HEART_BEAT_CLASS, String.valueOf(heartClient));
            return this;
        }

        public Builder clientHandlerClass(List<Class<? extends ChannelInboundHandlerAdapter>> clientHandlerClass) {
            configurable.addConfig(INettyConfig.CLIENT_HANDLER_CLASS, String.valueOf(clientHandlerClass));
            return this;
        }

        public Builder clientDecoderClass(Class<? extends ChannelInboundHandlerAdapter> clientDecoderClass) {
            configurable.addConfig(INettyConfig.CLIENT_DECODER_CLASS, String.valueOf(clientDecoderClass));
            return this;
        }

        public IModuleConfigurer build() {
            return configurable.toModuleConfigurer();
        }
    }
}