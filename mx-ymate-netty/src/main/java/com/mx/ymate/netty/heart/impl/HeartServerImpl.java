package com.mx.ymate.netty.heart.impl;

import cn.hutool.core.util.StrUtil;
import com.mx.ymate.netty.heart.IHeartServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import net.ymate.platform.log.Logs;

import java.net.InetSocketAddress;

/**
 * @Author: xujianpeng.
 * @Date: 2023-06-07 09:43
 * @Description:
 */
public class HeartServerImpl implements IHeartServer {


    @Override
    public void handle(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        if (event.state() == IdleState.READER_IDLE) {
            InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
            Logs.get().getLogger().debug(StrUtil.format("ip:{},端口:{}客户端长时间未发送消息",ipSocket.getHostString(),ipSocket.getPort()));
            ctx.channel().close();
        }
    }
}
