package com.mx.ymate.serv.util;


import net.ymate.platform.serv.nio.server.NioSessionWrapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: mengxiang.
 * @create: 2022-04-29 13:27
 * @Description:
 */
public class TcpServerStore {

    /**
     * 存放ChannelHandlerContext map   key是自定义标识
     */
    private static final Map<String, NioSessionWrapper> CLIENT_MAP;

    static {
        CLIENT_MAP = new ConcurrentHashMap<>();
    }

    public static Map<String, NioSessionWrapper> getClientMap() {
        return CLIENT_MAP;
    }


    public static void put(String key, NioSessionWrapper nioSessionWrapper) {
        CLIENT_MAP.put(key, nioSessionWrapper);
    }

    public static NioSessionWrapper getSession(String key) {
        return CLIENT_MAP.get(key);
    }


    public static void removeSession(String key) {
        CLIENT_MAP.remove(key);
    }

    public static void clear() {
        CLIENT_MAP.clear();
    }
}
