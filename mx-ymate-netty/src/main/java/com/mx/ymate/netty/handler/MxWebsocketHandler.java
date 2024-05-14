package com.mx.ymate.netty.handler;

import com.mx.ymate.netty.IMxWebsocketHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @Author: mengxiang.
 * @create: 2024-05-14 14:00
 * @Description:
 */
public class MxWebsocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println(msg.text());
        String mapping = MappingHandler.MAPPING_MAP.get(ctx.channel().id().asShortText());
        IMxWebsocketHandler handler = MappingHandler.HANDLER_MAP.get(mapping);
        handler.handle(ctx, msg);
    }
}
