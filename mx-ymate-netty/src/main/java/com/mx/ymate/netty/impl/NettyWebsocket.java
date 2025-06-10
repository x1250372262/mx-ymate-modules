package com.mx.ymate.netty.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mx.ymate.netty.bean.WebsocketConfig;
import com.mx.ymate.netty.websocket.MappingHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import net.ymate.platform.commons.lang.BlurObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Map;

import static com.mx.ymate.netty.INettyConfig.HEART_BEAT_TIME_ITEM_COUNT;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: nettywebsocket
 */
public class NettyWebsocket {

    private final WebsocketConfig config;
    private final EventLoopGroup boosGroup = new NioEventLoopGroup();
    private final EventLoopGroup workGroup = new NioEventLoopGroup();
    private static final Log LOG = LogFactory.getLog(NettyWebsocket.class);
    private volatile ServerBootstrap websocketBootstrap;
    private boolean started = false;

    public NettyWebsocket(WebsocketConfig config) {
        this.config = config;
    }

    public synchronized NettyWebsocket init() {
        websocketBootstrap = new ServerBootstrap();
        websocketBootstrap.group(boosGroup, workGroup).channel(NioServerSocketChannel.class);
        config.getWebsocketOptionConfig().optionConfig(websocketBootstrap);

        websocketBootstrap.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline channelPipeline = ch.pipeline();
                List<Integer> heartBeatTimeList = config.getHeartBeatTimeList();
                if (CollUtil.isNotEmpty(heartBeatTimeList) && heartBeatTimeList.size() == HEART_BEAT_TIME_ITEM_COUNT) {
                    channelPipeline.addLast(new IdleStateHandler(heartBeatTimeList.get(0), heartBeatTimeList.get(1), heartBeatTimeList.get(2)));
                    channelPipeline.addLast(config.getHeartBeatHandler());
                }
                channelPipeline.addLast(new HttpServerCodec());
                channelPipeline.addLast(new HttpObjectAggregator(65536));
                channelPipeline.addLast(new MappingHandler(config));
                channelPipeline.addLast(new WebSocketServerProtocolHandler(config.getMapping(), true));
            }
        });
        LOG.info("初始化websocket server成功");
        return this;
    }

    public void start() throws Exception {
        if (started) {
            LOG.warn("websocket Server 已启动，忽略重复启动");
            return;
        }
        if (websocketBootstrap == null) {
            init();
        }
        Integer port = config.getPort();
        websocketBootstrap.bind(port).sync();
        LOG.info(StrUtil.format("服务端口{}websocket启动成功",port));
        started = true;
    }

    public void stop() {
        //优雅退出，释放线程池
        boosGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
        websocketBootstrap = null;
        started = false;
        LOG.info("Netty Websocket 已停止");
    }

}
