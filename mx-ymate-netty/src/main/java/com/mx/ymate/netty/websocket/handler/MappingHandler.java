package com.mx.ymate.netty.websocket.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.mx.ymate.netty.Netty;
import com.mx.ymate.netty.websocket.annotation.MxWebsocket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.log.Logs;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.mx.ymate.netty.INettyConfig.WEBSOCKET_PACKAGE;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class MappingHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static final Map<String, IMxWebsocketHandler> URI_HANDLER_MAP = new ConcurrentHashMap<>();
    public static final Map<String, IMxWebsocketHandler> ID_HANDLER_MAP = new ConcurrentHashMap<>();
    private static final String UPGRADE_VALUE = "websocket";

    static {
        String packageName = Netty.get().getConfig().websocketPackage();
        if (StringUtils.isBlank(packageName)) {
            throw new NullArgumentException(WEBSOCKET_PACKAGE);
        }
        Set<Class<?>> mxWebsocketList = ClassUtil.scanPackageByAnnotation(packageName, MxWebsocket.class);
        if (CollUtil.isEmpty(mxWebsocketList)) {
            throw new RuntimeException("请至少指定一个 websocket handler ");
        }
        String mapping = Netty.get().getConfig().websocketMapping();
        for (Class<?> clazz : mxWebsocketList) {
            MxWebsocket mxWebsocket = clazz.getAnnotation(MxWebsocket.class);
            URI_HANDLER_MAP.put(mapping + mxWebsocket.mapping(), ClassUtils.impl(clazz, IMxWebsocketHandler.class));
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String id = ctx.channel().id().asShortText();
        IMxWebsocketHandler endpoint = ID_HANDLER_MAP.get(id);
        if (endpoint != null) {
            endpoint.onMessage(ctx, msg);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            String upgrade = request.headers().get("Upgrade");
            if (StringUtils.isBlank(upgrade) || !UPGRADE_VALUE.equalsIgnoreCase(upgrade)) {
                ctx.close();
            } else {
                String pathPrefix = Netty.get().getConfig().websocketMapping();
                if (!uri.startsWith(pathPrefix)) {
                    Logs.get().getLogger().error(StrUtil.format("url {} 格式错误", uri));
                    notFoundError(ctx);
                } else {
                    IMxWebsocketHandler endpoint = URI_HANDLER_MAP.get(uri);
                    if (endpoint == null) {
                        Logs.get().getLogger().error(StrUtil.format("url {} 没有对应的处理器", uri));
                        notFoundError(ctx);
                    } else {
                        super.channelRead(ctx, msg);
                        String id = ctx.channel().id().asShortText();
                        ID_HANDLER_MAP.put(id, endpoint);
                        endpoint.onOpen(ctx);
                    }
                }
            }
        } else {
            super.channelRead(ctx, msg);
        }

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String id = ctx.channel().id().asShortText();
        IMxWebsocketHandler endpoint = ID_HANDLER_MAP.get(id);
        if (endpoint != null) {
            endpoint.onClose(ctx);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String id = ctx.channel().id().asShortText();
        IMxWebsocketHandler endpoint = ID_HANDLER_MAP.get(id);
        if (endpoint != null) {
            endpoint.onError(ctx, cause);
        }
    }

    private void notFoundError(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
        ctx.writeAndFlush(response);
    }

}
