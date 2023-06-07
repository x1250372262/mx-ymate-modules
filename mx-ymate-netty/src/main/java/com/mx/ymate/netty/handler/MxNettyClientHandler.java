package com.mx.ymate.netty.handler;

import com.mx.ymate.netty.Netty;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: xujianpeng.
 * @Date: 2023-06-07 09:34
 * @Description:
 */
public class MxNettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断线了。。。");
        Netty.get().connect(ctx);
    }
}
