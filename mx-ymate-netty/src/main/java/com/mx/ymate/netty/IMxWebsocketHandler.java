package com.mx.ymate.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @Author: mengxiang.
 * @create: 2024-05-14 14:00
 * @Description: websocket处理接口
 */
public interface IMxWebsocketHandler {

    /**
     * 处理数据方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    void handle(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception;
}
