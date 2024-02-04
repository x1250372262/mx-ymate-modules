package com.mx.ymate.netty.util;

import cn.hutool.core.util.HexUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.ymate.platform.log.Logs;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: mengxiang.
 * @create: 2022-04-29 13:25
 * @Description:
 */
public class NettyUtil {


    //客户端连接缓存
    private static final Map<String, ChannelHandlerContext> CLIENT_MAP;

    /**
     * 客户端key缓存
     */
    private static final Map<String, String> KEY_MAP;

    static {
        CLIENT_MAP = new ConcurrentHashMap<>();
        KEY_MAP = new ConcurrentHashMap<>();
    }

    public static Map<String, ChannelHandlerContext> getClientMap() {
        return CLIENT_MAP;
    }

    public static Map<String, String> getKeyMap() {
        return KEY_MAP;
    }

    public static void putContent(String key, ChannelHandlerContext context) {
        CLIENT_MAP.put(key, context);
        String channelId = context.channel().id().asShortText();
        KEY_MAP.put(channelId, key);
    }

    public static ChannelHandlerContext getContent(String key) {
        return CLIENT_MAP.get(key);
    }

    public static String getKey(String channelId) {
        return KEY_MAP.get(channelId);
    }

    public static void removeKey(String channelId) {
        KEY_MAP.remove(channelId);
    }

    public static boolean containsKey(String channelId) {
        return KEY_MAP.containsKey(channelId);
    }

    public static boolean containsContext(String key) {
        return CLIENT_MAP.containsKey(key);
    }

    public static void close(String key) {
        ChannelHandlerContext context = getContent(key);
        if (context == null) {
            return;
        }
        CLIENT_MAP.remove(key);
        KEY_MAP.remove(context.channel().id().asShortText());
        context.close();
    }

    public static void clear() {
        for (Map.Entry<String, ChannelHandlerContext> serverMap : CLIENT_MAP.entrySet()) {
            ChannelHandlerContext context = serverMap.getValue();
            if (context == null) {
                continue;
            }
            context.close();
        }
        CLIENT_MAP.clear();
        KEY_MAP.clear();
    }


    /**
     * 发送16进制字符串
     *
     * @param key
     * @param hex
     */
    public static void sendHex(String key, String hex) {
        ByteBuf buffer = Unpooled.wrappedBuffer(HexUtil.decodeHex(hex));
        sendBuffer(key, buffer, hex);
    }


    /**
     * 发送字符串
     *
     * @param key
     * @param message
     */
    public static void sendStr(String key, String message) {
        ByteBuf buffer = Unpooled.wrappedBuffer(message.getBytes(StandardCharsets.UTF_8));
        sendBuffer(key, buffer, message);
    }

    public static void send(String key,Object message){
        ChannelHandlerContext context = getContent(key);
        if (context == null) {
            return;
        }
        Logs.get().getLogger().info("发送数据:key:" + key + "原始数据:" + JSONObject.toJSONString(message));
        try {
            context.writeAndFlush(message);
        } catch (Exception exception) {
            Logs.get().getLogger().error("发送失败:", exception);
        }
    }

    public static void sendBuffer(String key, ByteBuf buffer, String raw) {
        ChannelHandlerContext context = getContent(key);
        if (context == null) {
            return;
        }
        Logs.get().getLogger().info("发送数据:key:" + key + "原始数据:" + raw);
        try {
            context.writeAndFlush(buffer);
        } catch (Exception exception) {
            Logs.get().getLogger().error("发送失败:", exception);
        }
    }

}
