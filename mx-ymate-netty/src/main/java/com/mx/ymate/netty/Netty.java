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

import com.mx.ymate.netty.impl.DefaultNettyConfig;
import com.mx.ymate.netty.impl.NettyClient;
import com.mx.ymate.netty.impl.NettyServer;
import com.mx.ymate.netty.impl.NettyWebsocket;
import io.netty.channel.ChannelHandlerContext;
import net.ymate.platform.core.*;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.IModuleConfigurer;
import net.ymate.platform.core.module.impl.DefaultModuleConfigurer;

import java.net.InetSocketAddress;

import static com.mx.ymate.netty.INettyConfig.SERVER_CLIENT_CLIENT;
import static com.mx.ymate.netty.INettyConfig.SERVER_CLIENT_SERVER;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public final class Netty implements IModule, INetty {


    private static volatile INetty instance;

    private IApplication owner;

    private INettyConfig config;

    private boolean initialized;

    private NettyServer nettyServer;
    private NettyClient nettyClient;

    private NettyWebsocket nettyWebsocket;

    public static INetty get() {
        INetty inst = instance;
        if (inst == null) {
            synchronized (Netty.class) {
                inst = instance;
                if (inst == null) {
                    instance = inst = YMP.get().getModuleManager().getModule(Netty.class);
                }
            }
        }
        return inst;
    }

    public Netty() {
    }

    public Netty(INettyConfig config) {
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
                        config = DefaultNettyConfig.create(moduleConfigurer);
                    } else {
                        config = DefaultNettyConfig.create(DefaultModuleConfigurer.createEmpty(MODULE_NAME));
                    }
                }
            }
            if (!config.isInitialized()) {
                config.initialize(this);
            }

            if (config.isEnabled()) {

                if (config.autoStart()) {
                    if (SERVER_CLIENT_SERVER.equals(config.client())) {
                        nettyServer = new NettyServer(config);
                        nettyServer.run();
                    } else if (SERVER_CLIENT_CLIENT.equals(config.client())) {
                        nettyClient = new NettyClient(config);
                        nettyClient.run();
                    } else {
                        nettyServer = new NettyServer(config);
                        nettyClient = new NettyClient(config);
                        nettyServer.run();
                        nettyClient.run();
                    }
                }
                if (config.websocketEnabled()) {
                    nettyWebsocket = new NettyWebsocket(config);
                    nettyWebsocket.run();
                }
            }
            initialized = true;
            YMP.showVersion("初始化 mx-ymate-netty-netty-${version} 模块成功", new Version(1, 0, 0, Netty.class, Version.VersionType.Release));
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
            if (config.isEnabled()) {
                if (SERVER_CLIENT_SERVER.equals(config.client())) {
                    if (nettyServer != null) {
                        nettyServer.stop();
                    }
                } else if (SERVER_CLIENT_CLIENT.equals(config.client())) {
                    if (nettyClient != null) {
                        nettyClient.stop();
                    }
                } else {
                    if (nettyClient != null) {
                        nettyClient.stop();
                    }
                    if (nettyServer != null) {
                        nettyServer.stop();
                    }

                }

                if (nettyWebsocket != null) {
                    nettyWebsocket.stop();
                }
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
    public INettyConfig getConfig() {
        return config;
    }

    @Override
    public void startServer() throws Exception {
        if (nettyServer == null) {
            nettyServer = new NettyServer(config);
            nettyServer.run();
        }
    }

    @Override
    public void startClient() throws Exception {
        if (nettyClient == null) {
            nettyClient = new NettyClient(config);
            nettyClient.run();
        }
    }

    @Override
    public void startAll() throws Exception {
        if (nettyClient == null) {
            nettyClient = new NettyClient(config);
            nettyClient.run();
        }
        if (nettyServer == null) {
            nettyServer = new NettyServer(config);
            nettyServer.run();
        }
    }

    @Override
    public void stopServer() {
        if (nettyServer != null) {
            nettyServer.stop();
        }
    }

    @Override
    public void stopClient() {
        if (nettyClient != null) {
            nettyClient.stop();
        }
    }

    @Override
    public void stoptAll() {
        if (nettyClient != null) {
            nettyClient.stop();
        }
        if (nettyServer != null) {
            nettyServer.stop();
        }
    }

    @Override
    public void startWebSocketServer() throws Exception {
        if (nettyWebsocket == null) {
            nettyWebsocket = new NettyWebsocket(config);
            nettyWebsocket.run();
        }
    }

    @Override
    public void stopWebSocketServer() throws Exception {
        if (nettyWebsocket != null) {
            nettyWebsocket.stop();
        }
    }

    @Override
    public void connect(ChannelHandlerContext context) throws Exception {
        InetSocketAddress ipSocket = (InetSocketAddress) context.channel().remoteAddress();
        int port = ipSocket.getPort();
        String host = ipSocket.getHostString();
        nettyClient.connect(new NettyClient.RemoteAddress(host, port));
    }
}
