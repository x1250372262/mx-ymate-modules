package com.mx.ymate.netty.websocket.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@ChannelHandler.Sharable
public abstract class AbstractWebsocketHandler implements IMxWebsocketHandler {

    private static final Log LOG = LogFactory.getLog(AbstractWebsocketHandler.class);

    @Override
    public void onOpen(ChannelHandlerContext ctx) throws Exception {
        LOG.info("onOpen");
    }

    /**
     * 消息到达
     *
     * @param ctx 会话
     * @param msg 消息
     * @throws Exception
     */
    @Override
    public abstract void onMessage(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception;

    @Override
    public void onClose(ChannelHandlerContext ctx) throws Exception {
        LOG.info("onClose");
    }

    @Override
    public void onError(ChannelHandlerContext ctx, Throwable t) throws Exception {
        LOG.info("onError");
    }
}
