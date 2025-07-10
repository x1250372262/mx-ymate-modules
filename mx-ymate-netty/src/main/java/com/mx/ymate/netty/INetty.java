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

import com.mx.ymate.netty.manager.NettyClientManager;
import com.mx.ymate.netty.manager.NettyServerManager;
import com.mx.ymate.netty.manager.NettyWebsocketManager;
import net.ymate.platform.core.IApplication;
import net.ymate.platform.core.beans.annotation.Ignored;
import net.ymate.platform.core.support.IDestroyable;
import net.ymate.platform.core.support.IInitialization;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Ignored
public interface INetty extends IInitialization<IApplication>, IDestroyable {

    String MODULE_NAME = "module.netty";

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
    INettyConfig getConfig();

    /**
     * 获取服务端管理对象
     *
     * @return
     */
    NettyServerManager serverManager();

    /**
     * 获取客户端管理对象
     *
     * @return
     */
    NettyClientManager clientManager();

    /**
     * 获取websocket管理对象
     *
     * @return
     */
    NettyWebsocketManager websocketManager();

}
