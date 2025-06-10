package com.mx.ymate.netty.handler;

import com.mx.ymate.netty.util.NettyRemoteAddressUtil;
import io.netty.channel.ChannelHandlerContext;
import net.ymate.platform.log.Logs;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class ConnectionManager {


    /**
     * 客户端连接缓存 key=标识，value=ctx
     */
    private static final Map<String, ChannelHandlerContext> CLIENT_MAP = new ConcurrentHashMap<>();

    /**
     * 客户端key缓存 key=ctxId，value=标识
     */
    private static final Map<String, String> KEY_MAP = new ConcurrentHashMap<>();

    private ConnectionManager() {
    }

    /**
     * 注册连接
     *
     * @param key 业务标识
     * @param ctx Netty 连接上下文
     */
    public static void registerClient(String key, ChannelHandlerContext ctx) {
        CLIENT_MAP.put(key, ctx);
        KEY_MAP.put(ctx.channel().id().asShortText(), key);
    }


    /**
     * 注销连接，并关闭连接
     *
     * @param key 业务标识
     */
    public static void unregisterClient(String key) {
        ChannelHandlerContext ctx = CLIENT_MAP.remove(key);
        if (ctx != null) {
            KEY_MAP.remove(ctx.channel().id().asShortText());
            try {
                ctx.close();
            } catch (Exception e) {
                // 可选择记录日志
            }
        }
    }

    /**
     * 根据业务标识获取连接上下文
     */
    public static ChannelHandlerContext getContext(String key) {
        return CLIENT_MAP.get(key);
    }

    /**
     * 根据 channelId 获取业务标识
     */
    public static String getKey(String channelId) {
        return KEY_MAP.get(channelId);
    }

    /**
     * 判断是否包含业务标识
     */
    public static boolean containsContext(String key) {
        return CLIENT_MAP.containsKey(key);
    }

    /**
     * 判断是否包含 Channel ID
     */
    public static boolean containsChannelId(String channelId) {
        return KEY_MAP.containsKey(channelId);
    }

    /**
     * 清空所有连接并关闭
     */
    public static void clearAll() {
        for (ChannelHandlerContext ctx : CLIENT_MAP.values()) {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                    // 记录日志或忽略
                    Logs.get().getLogger().error("关闭失败", e);
                }
            }
        }
        CLIENT_MAP.clear();
        KEY_MAP.clear();
    }
}
