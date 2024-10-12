package com.mx.ymate.dev.support.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @Author: mengxiang.
 * @Date: 2024-10-11 17:00
 * @Description:
 */
public class RmiClient {
    public static Registry get(String host, int port) {
        try {
            return LocateRegistry.getRegistry(host, port);
        } catch (RemoteException e) {
            throw new RuntimeException("获取连接失败:" + e.getMessage());
        }
    }

    public static Registry get(int port) {
        return get("localhost", port);
    }

    public static Registry get() {
        return get("localhost", Registry.REGISTRY_PORT);
    }

}
