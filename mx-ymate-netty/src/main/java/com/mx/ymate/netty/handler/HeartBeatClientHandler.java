package com.mx.ymate.netty.handler;

import com.mx.ymate.dev.support.log.MxLog;
import com.mx.ymate.netty.INettyConfig;
import com.mx.ymate.netty.Netty;
import com.mx.ymate.netty.heart.IHeartClient;
import com.mx.ymate.netty.heart.IHeartServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @Author: mengxiang.
 * @create: 2022-04-22 15:34
 * @Description: 心跳维护
 */
public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {

    private final INettyConfig nettyConfig ;

    public HeartBeatClientHandler() {
        nettyConfig = Netty.get().getConfig();
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IHeartClient iHeartClient = nettyConfig.heartClient();
            iHeartClient.handle(ctx,evt);
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MxLog.debug("===" + msg + "===");
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        MxLog.error("netty异常", cause);
//        ctx.close();
    }
}
