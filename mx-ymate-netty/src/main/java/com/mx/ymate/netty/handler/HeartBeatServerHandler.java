package com.mx.ymate.netty.handler;

import com.mx.spring.dev.support.log.MxLog;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @Author: mengxiang.
 * @create: 2022-04-22 15:34
 * @Description: 心跳维护
 */
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {

    public HeartBeatServerHandler() {
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MxLog.debug("===" + msg + "===");
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        MxLog.error("心跳检测异常", cause);
        ctx.close();
    }
}
