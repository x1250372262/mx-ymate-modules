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
import com.mx.ymate.netty.manager.NettyClientManager;
import com.mx.ymate.netty.manager.NettyServerManager;
import com.mx.ymate.netty.manager.NettyWebsocketManager;
import net.ymate.platform.core.*;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.IModuleConfigurer;
import net.ymate.platform.core.module.impl.DefaultModuleConfigurer;

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

    private NettyServerManager nettyServerManager;

    private NettyClientManager nettyClientManager;

    private NettyWebsocketManager nettyWebsocketManager;

    private boolean initialized;

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
                nettyServerManager = new NettyServerManager(config.serverConfigList());
                nettyClientManager = new NettyClientManager(config.clientConfigList());
                nettyWebsocketManager = new NettyWebsocketManager(config.websocketConfig());
                if (config.serverAutoInit()) {
                    nettyServerManager.initAll();
                }
                if (config.clientAutoInit()) {
                    nettyClientManager.initAll();
                }
                if (config.websocketAutoInit()) {
                    nettyWebsocketManager.init();
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
                nettyServerManager.stopAll();
                nettyClientManager.stopAll();
                nettyWebsocketManager.stop();
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
    public NettyServerManager serverManager() {
        return nettyServerManager;
    }

    @Override
    public NettyClientManager clientManager() {
        return nettyClientManager;
    }

    @Override
    public NettyWebsocketManager websocketManager() {
        return nettyWebsocketManager;
    }


}


