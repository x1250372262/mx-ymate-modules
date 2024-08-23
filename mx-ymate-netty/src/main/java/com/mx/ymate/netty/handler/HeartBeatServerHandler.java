package com.mx.ymate.netty.handler;

import cn.hutool.core.util.StrUtil;
import com.mx.ymate.netty.INettyConfig;
import com.mx.ymate.netty.Netty;
import com.mx.ymate.netty.heart.IHeartServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.log.Logs;

import java.net.InetSocketAddress;

/**
 * @Author: mengxiang.
 * @create: 2022-04-22 15:34
 * @Description: 心跳维护
 */
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {

    private final INettyConfig nettyConfig;

    public HeartBeatServerHandler() {
        nettyConfig = Netty.get().getConfig();
    }

    private final static String MESSAGE = "netty异常，ip:{},端口:{}";

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 检查 ctx 和 evt 是否为空
        if (ctx == null || evt == null) {
            Logs.get().getLogger().warn("Context or event is null, skipping event handling.");
            return;
        }
        if (evt instanceof IdleStateEvent) {
            IHeartServer iHeartServer = nettyConfig.heartServer();
            iHeartServer.handle(ctx, evt);
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Logs.get().getLogger().info("===" + msg + "===");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String ip = "未知";
        String port = "未知";
        if (ctx != null) {
            InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
            ip = ipSocket.getHostString();
            port = BlurObject.bind(ipSocket.getPort()).toString();
        }
        cause.printStackTrace();
        Logs.get().getLogger().error(StrUtil.format(MESSAGE, ip, port), cause);
//        ctx.close();
    }
}
