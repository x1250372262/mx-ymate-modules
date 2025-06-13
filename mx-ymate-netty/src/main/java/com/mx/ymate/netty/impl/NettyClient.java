package com.mx.ymate.netty.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.mx.ymate.netty.bean.ClientConfig;
import com.mx.ymate.netty.bean.ClientContext;
import com.mx.ymate.netty.bean.HandlerConfig;
import com.mx.ymate.netty.bean.RemoteAddress;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.mx.ymate.netty.bean.ClientConfig.CLIENT_CTX_KEY;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: netty客户端
 */
public class NettyClient {

    private final ClientConfig config;

    private final EventLoopGroup workGroup = new NioEventLoopGroup();
    private static final Log LOG = LogFactory.getLog(NettyClient.class);
    private volatile Bootstrap clientBootstrap;
    private List<RemoteAddress> remoteAddressesList;
    private volatile boolean connected = false;
    private volatile Channel currentChannel;

    public NettyClient(ClientConfig config) {
        this.config = config;
    }

    private List<RemoteAddress> getRemoteAddressList(List<String> remoteAddressStrList) {
        if (CollUtil.isEmpty(remoteAddressStrList)) {
            throw new IllegalArgumentException("请指定需要连接的服务地址（INettyConfig.clientRemoteAddress）");
        }
        List<RemoteAddress> list = new ArrayList<>();
        for (String remoteAddressStr : remoteAddressStrList) {
            String[] remoteAddressArr = remoteAddressStr.split(":");
            if (remoteAddressArr.length == 2) {
                list.add(new RemoteAddress(remoteAddressArr[0], Convert.toInt(remoteAddressArr[1])));
            }
        }
        return list;
    }


    public synchronized NettyClient init() {
        List<HandlerConfig> handlerConfigList = config.getHandlerConfigList();
        if (CollUtil.isEmpty(handlerConfigList)) {
            throw new IllegalArgumentException("请至少配置一个服务端Handler");
        }
        clientBootstrap = new Bootstrap();
        clientBootstrap.group(workGroup).channel(NioSocketChannel.class);
        config.getClientOptionConfig().optionConfig(clientBootstrap);
        clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline channelPipeline = ch.pipeline();
                channelPipeline.addLast(new LoggingHandler(LogLevel.INFO));
                for (HandlerConfig handlerConfig : config.getHandlerConfigList()) {
                    channelPipeline.addLast(handlerConfig.newInstance());
                }
            }
        });
        LOG.info(StrUtil.format("初始化netty client成功", config.getName()));
        return this;
    }

    private synchronized void doConnect(RemoteAddress remoteAddress, Map<String, Object> extras) {
        if (connected) {
            LOG.warn("Netty Client 已连接，忽略重复连接");
            return;
        }
        LOG.info(StrUtil.format("和ip:{},端口:{}服务进行连接", remoteAddress.getHost(), remoteAddress.getPort()));
        ChannelFuture cf = clientBootstrap.connect(remoteAddress.getHost(), remoteAddress.getPort());
        cf.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                //重连交给后端线程执行
                future.channel().eventLoop().schedule(() -> {
                    LOG.error("重连服务端...");
                    try {
                        doConnect(remoteAddress, extras);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }, 3, TimeUnit.SECONDS);
            } else {
                LOG.info("服务端连接成功...");
                1缓存调整  分为 客户端 服务端 ws 分别缓存不同的内容
                客户端增加一个 ctx，nettyclient 或者 name    以便断连的时候能获取到nettyclient
                2客户端配置文件增加断线重连开关 以及 断线重连时间
                currentChannel = future.channel();
                future.channel().attr(CLIENT_CTX_KEY).set(ClientContext.builder().clientId(config.getName()).putExtras(extras).build());
                connected = true;
            }
        });
    }

    public void connect(Map<String, Object> extras) {
        if (CollUtil.isEmpty(remoteAddressesList)) {
            remoteAddressesList = getRemoteAddressList(config.getRemoteAddress());
        }
        //启动客户端去连接服务器端
        for (RemoteAddress remoteAddress : remoteAddressesList) {
            ThreadUtil.execAsync(() -> doConnect(remoteAddress, extras));
        }
    }

    public void connect(RemoteAddress remoteAddress, Map<String, Object> extras) {
        ThreadUtil.execAsync(() -> doConnect(remoteAddress, extras));
    }

    public void connect() {
        if (CollUtil.isEmpty(remoteAddressesList)) {
            remoteAddressesList = getRemoteAddressList(config.getRemoteAddress());
        }
        //启动客户端去连接服务器端
        for (RemoteAddress remoteAddress : remoteAddressesList) {
            ThreadUtil.execAsync(() -> doConnect(remoteAddress, null));
        }
    }

    public void connect(RemoteAddress remoteAddress) {
        ThreadUtil.execAsync(() -> doConnect(remoteAddress, null));
    }

    public void disconnect() {
        connected = false;
        currentChannel.disconnect();
        currentChannel.close();
        LOG.info(StrUtil.format("NettyClient[{}]已断开连接",config.getName()));
    }

    public void destory() {
        //优雅退出，释放线程池
        workGroup.shutdownGracefully();
        clientBootstrap = null;
        connected = false;
        LOG.info("Netty Client 已停止");
    }

}
