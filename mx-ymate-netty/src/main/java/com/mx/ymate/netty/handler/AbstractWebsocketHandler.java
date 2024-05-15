package com.mx.ymate.netty.handler;

import com.mx.ymate.netty.IMxWebsocketHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import net.ymate.platform.log.ILogger;
import net.ymate.platform.log.Logs;

/**
 * @Author: mengxiang.
 * @create: 2024-05-14 14:00
 * @Description:
 */
public abstract class AbstractWebsocketHandler implements IMxWebsocketHandler {

    private final ILogger log = Logs.get().getLogger();

    @Override
    public void onOpen(ChannelHandlerContext ctx) throws Exception {
        log.info("onOpen");
    }

    @Override
    public abstract void onMessage(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception;

    @Override
    public void onClose(ChannelHandlerContext ctx) throws Exception {
        log.info("onClose");
    }

    @Override
    public void onError(ChannelHandlerContext ctx, Throwable t) throws Exception {
        log.info("onError");
    }
}
