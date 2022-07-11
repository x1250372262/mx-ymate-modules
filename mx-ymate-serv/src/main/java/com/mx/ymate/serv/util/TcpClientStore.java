package com.mx.ymate.serv.util;

import net.ymate.platform.serv.nio.INioSession;
import net.ymate.platform.serv.nio.client.NioClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: mengxiang.
 * @create: 2022-04-29 13:27
 * @Description:
 */
public class TcpClientStore {

    /**
     * 存放TcpClient map   key是自定义标识
     */
    private static final Map<String, INioSession> CLIENT_MAP;


    static {
        CLIENT_MAP = new ConcurrentHashMap<>();
    }

    public static Map<String, INioSession> getClientMap() {
        return CLIENT_MAP;
    }

    public static void put(String key, INioSession nioClient) {
        CLIENT_MAP.put(key, nioClient);
    }

    public static INioSession getClient(String key) {
        return CLIENT_MAP.get(key);
    }

    public static void removeTcpClient(String key) {
        CLIENT_MAP.remove(key);
    }

    public static void clear() {
        CLIENT_MAP.clear();
    }
}
