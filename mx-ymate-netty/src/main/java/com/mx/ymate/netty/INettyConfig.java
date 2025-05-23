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

import io.netty.channel.ChannelInboundHandlerAdapter;
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
    String CLIENT = "client";
    String AUTO_START = "autoStart";

    String SERVER_PORT = "server.port";
    String SERVER_START_PORT = "server.startPort";
    String SERVER_END_PORT = "server.endPort";
    String SERVER_EXCLUDE_PORT = "server.excludePort";
    String SERVER_HEART_BEAT_TIME = "server.heartBeatTime";
    String SERVER_HEART_BEAT_CLASS = "server.heartBeatClass";
    String SERVER_HANDLER_CLASS = "server.handlerClass";
    String SERVER_DECODER_CLASS = "server.decoderClass";

    String CLIENT_REMOTE_ADDRESS = "client.remoteAddress";
    String CLIENT_HEART_BEAT_TIME = "client.heartBeatTime";
    String CLIENT_NUM = "client.num";
    String CLIENT_HEART_BEAT_CLASS = "client.heartBeatClass";
    String CLIENT_HANDLER_CLASS = "client.handlerClass";
    String CLIENT_DECODER_CLASS = "client.decoderClass";

    String WEBSOCKET_ENABLED = "websocket.enabled";
    String WEBSOCKET_PORT = "websocket.port";
    String WEBSOCKET_HEART_BEAT_TIME = "websocket.heartBeatTime";
    String WEBSOCKET_HEART_BEAT_CLASS = "websocket.heartBeatClass";
    String WEBSOCKET_MAPPING = "websocket.mapping";
    String WEBSOCKET_PACKAGE = "websocket.package";

    String SERVER_CLIENT_SERVER = "server";
    String SERVER_CLIENT_CLIENT = "client";

    int HEART_BEAT_TIME_ITEM_COUNT = 3;

    /**
     * 模块是否已启用, 默认值: true
     *
     * @return 返回false表示禁用
     */
    boolean isEnabled();

    /**
     * 是否自动启动
     *
     * @return
     */
    Boolean autoStart();

    /**
     * #启动的客户端 all全部 server服务端  client 客户端  默认all
     *
     * @return
     */
    String client();

    /**
     * 服务端端口 优先级最高
     *
     * @return
     */
    Integer serverPort();

    /**
     * 服务端开始端口 指定port了 以port为准
     *
     * @return
     */
    Integer serverStartPort();

    /**
     * 服务端结束端口 指定port了 以port为准
     *
     * @return
     */
    Integer serverEndPort();

    /**
     * 排除端口 用,号分割 只针对startPort endPort有效
     *
     * @return
     */
    List<String> serverExcludePort();


    /**
     * 心跳维护时间（三个时间）格式：读空闲|写空闲|读写空闲 默认空不维护 单位s
     *
     * @return
     */
    List<Integer> serverHeartBeatTimeList();

    /**
     * 心跳实现类 当serverHeartBeatTime不等于0的时候有效
     *
     * @return
     */
    AbstractHeartBeatHandler serverHeart();

    /**
     * 处理器名称 可以指定多个用,号分割 按顺序添加
     *
     * @return
     */
    List<ChannelInboundHandlerAdapter> serverHandler();

    /**
     * 编解码名称 只能指定一个
     *
     * @return
     */
    ChannelInboundHandlerAdapter serverDecoder();

    /**
     * 客户端数量
     *
     * @return
     */
    Integer clientNum();

    /**
     * 远程连接地址 ip:port  多个用逗号分割
     *
     * @return
     */
    List<String> clientRemoteAddress();


    /**
     * 心跳维护时间（三个时间）格式：读空闲|写空闲|读写空闲 默认空不维护 单位s
     *
     * @return
     */
    List<Integer> clientHeartBeatTimeList();

    /**
     * 心跳实现类 当clientHeartBeatTime不等于0的时候有效
     *
     * @return
     */
    AbstractHeartBeatHandler clientHeart();

    /**
     * 处理器名称 可以指定多个用,号分割 按顺序添加
     *
     * @return
     */
    List<ChannelInboundHandlerAdapter> clientHandler();

    /**
     * 编解码名称 只能指定一个
     *
     * @return
     */
    ChannelInboundHandlerAdapter clientDecoder();

    /**
     * 是否启用websocket 默认false
     *
     * @return
     */
    boolean websocketEnabled();

    /**
     * websocket端口 默认8756
     *
     * @return
     */
    int websocketPort();

    /**
     * 心跳维护时间（三个时间）格式：读空闲|写空闲|读写空闲 默认空不维护 单位s
     *
     * @return
     */
    List<Integer> websocketHeartBeatTimeList();

    /**
     * 心跳实现类 当clientHeartBeatTime不等于0的时候有效
     *
     * @return
     */
    AbstractHeartBeatHandler websocketHeart();

    /**
     * websocket请求路径 默认websocket
     *
     * @return
     */
    String websocketMapping();

    /**
     * websocket 处理器所在包
     *
     * @return
     */
    String websocketPackage();


}
