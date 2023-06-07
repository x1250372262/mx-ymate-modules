package com.mx.ymate.netty.heart.impl;

import cn.hutool.core.util.StrUtil;
import com.mx.ymate.dev.support.log.MxLog;
import com.mx.ymate.netty.heart.IHeartClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetSocketAddress;

/**
 * @Author: xujianpeng.
 * @Date: 2023-06-07 09:43
 * @Description:
 */
public class HeartClientImpl implements IHeartClient {


    @Override
    public void handle(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        if (event.state() == IdleState.WRITER_IDLE) {
            InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
            MxLog.debug(StrUtil.format("客户端长时间未向ip:{},端口:{}服务端发送消息"),ipSocket.getHostString(),ipSocket.getPort());
        }
    }
}
