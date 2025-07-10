package com.mx.ymate.netty.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mx.ymate.netty.bean.HandlerConfig;
import com.mx.ymate.netty.bean.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: netty客户端
 */
public class NettyServer {

    private final ServerConfig config;
    private final EventLoopGroup boosGroup = new NioEventLoopGroup();
    private final EventLoopGroup workGroup = new NioEventLoopGroup();
    private static final Log LOG = LogFactory.getLog(NettyServer.class);
    private volatile ServerBootstrap serverBootstrap;
    private boolean started = false;

    public NettyServer(ServerConfig config) {
        this.config = config;
    }

    public synchronized NettyServer init() {
        List<HandlerConfig> handlerConfigList = config.getHandlerConfigList();
        if (CollUtil.isEmpty(handlerConfigList)) {
            throw new IllegalArgumentException("请至少配置一个服务端Handler");
        }
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boosGroup, workGroup).channel(NioServerSocketChannel.class);
        config.getServerOptionConfig().optionConfig(serverBootstrap);
        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline channelPipeline = ch.pipeline();
                        for (HandlerConfig handlerConfig : config.getHandlerConfigList()) {
                            channelPipeline.addLast(handlerConfig.newInstance());
                        }
                    }
                });
        LOG.info(StrUtil.format("初始化netty server成功", config.getName()));
        return this;
    }

    public void start() throws Exception {
        if (started) {
            LOG.warn("Netty Server 已启动，忽略重复启动");
            return;
        }
        if (serverBootstrap == null) {
            init();
        }
        Integer port = config.getPort();
        serverBootstrap.bind(port).sync();
        LOG.info(StrUtil.format("服务 [{}] 端口 [{}] 绑定成功", config.getName(), port));
        started = true;
    }

    public void stop() {
        //优雅退出，释放线程池
        boosGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
        serverBootstrap = null;
        started = false;
        LOG.info("Netty Server 已停止: " + config.getName());
    }


}
