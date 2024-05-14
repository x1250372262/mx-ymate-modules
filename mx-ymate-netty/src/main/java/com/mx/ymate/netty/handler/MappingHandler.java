package com.mx.ymate.netty.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import com.mx.ymate.netty.IMxWebsocketHandler;
import com.mx.ymate.netty.Netty;
import com.mx.ymate.netty.annotation.MxWebsocket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import net.ymate.platform.commons.util.ClassUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.mx.ymate.netty.INettyConfig.WEBSOCKET_PACKAGE;

/**
 * @Author: mengxiang.
 * @create: 2024-05-14 14:00
 * @Description:
 */
public class MappingHandler extends ChannelInboundHandlerAdapter {

    public static final Map<String, String> MAPPING_MAP = new ConcurrentHashMap<>();
    public static final Map<String, IMxWebsocketHandler> HANDLER_MAP = new ConcurrentHashMap<>();

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
        if(!mapping.startsWith("/")){
            mapping = "/" + mapping;
        }
        for (Class<?> clazz : mxWebsocketList) {
            MxWebsocket mxWebsocket = clazz.getAnnotation(MxWebsocket.class);
            HANDLER_MAP.put(mapping + mxWebsocket.mapping(), ClassUtils.impl(clazz, IMxWebsocketHandler.class));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            MAPPING_MAP.put(ctx.channel().id().asShortText(),request.uri());
        }
        ctx.fireChannelRead(msg);
    }
}
