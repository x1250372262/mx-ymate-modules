package com.mx.ymate.netty.util;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: mengxiang.
 * @create: 2022-04-29 13:27
 * @Description:
 */
public class NettyServerStore {

    /**
     * 存放ChannelHandlerContext map   key是自定义标识
     */
    private static final Map<String, ChannelHandlerContext> CLIENT_MAP;

    /**
     * 存放key的map  key是ChannelHandlerContext id
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

    public static void put(String key, ChannelHandlerContext channelHandlerContext) {
        String channelId = channelHandlerContext.channel().id().asShortText();
        CLIENT_MAP.put(key, channelHandlerContext);
        KEY_MAP.put(channelId, key);
    }

    public static ChannelHandlerContext getChannelHandlerContext(String key) {
        return CLIENT_MAP.get(key);
    }

    public static String getKey(String channelId) {
        return KEY_MAP.getOrDefault(channelId, "");
    }

    public static void removeChannelHandlerContext(String key) {
        CLIENT_MAP.remove(key);
    }

    public static void removeKey(String channelId) {
        KEY_MAP.remove(channelId);
    }

    public static boolean containsKey(String channelId){
        return KEY_MAP.containsKey(channelId);
    }

    public static boolean containsChannelHandlerContext(String key){
        return CLIENT_MAP.containsKey(key);
    }

    public static void clear() {
        CLIENT_MAP.clear();
        KEY_MAP.clear();
    }
}
