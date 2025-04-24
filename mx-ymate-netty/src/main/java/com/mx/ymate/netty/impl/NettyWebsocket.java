package com.mx.ymate.netty.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mx.ymate.netty.INettyConfig;
import com.mx.ymate.netty.websocket.handler.MappingHandler;
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
import net.ymate.platform.log.Logs;

import java.util.List;

import static com.mx.ymate.netty.INettyConfig.HEART_BEAT_TIME_ITEM_COUNT;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: nettywebsocket
 */
public class NettyWebsocket {

    private final INettyConfig config;

    private final EventLoopGroup BOSS_GROUP = new NioEventLoopGroup();
    private final EventLoopGroup WORK_GROUP = new NioEventLoopGroup();


    public NettyWebsocket(INettyConfig config) {
        this.config = config;
    }

    public void run() throws Exception {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(BOSS_GROUP, WORK_GROUP)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline channelPipeline = ch.pipeline();
                        List<Integer> heartBeatTimeList = config.websocketHeartBeatTimeList();
                        if (CollUtil.isNotEmpty(heartBeatTimeList) && heartBeatTimeList.size() == HEART_BEAT_TIME_ITEM_COUNT) {
                            channelPipeline.addLast(new IdleStateHandler(heartBeatTimeList.get(0), heartBeatTimeList.get(1), heartBeatTimeList.get(2)));
                        }
                        channelPipeline.addLast(new HttpServerCodec());
                        channelPipeline.addLast(new HttpObjectAggregator(65536));
                        channelPipeline.addLast(new MappingHandler());
                        channelPipeline.addLast(new WebSocketServerProtocolHandler(config.websocketMapping(), true));
                        if (CollUtil.isNotEmpty(heartBeatTimeList) && heartBeatTimeList.size() == HEART_BEAT_TIME_ITEM_COUNT) {
                            channelPipeline.addLast(config.websocketHeart());
                        }
                    }
                });
        serverBootstrap.bind(config.websocketPort()).sync();
        Logs.get().getLogger().info(StrUtil.format("端口{}websocket启动成功", config.websocketPort()));

    }

    public void stop() {
        //优雅退出，释放线程池
        BOSS_GROUP.shutdownGracefully();
        WORK_GROUP.shutdownGracefully();
    }

}
