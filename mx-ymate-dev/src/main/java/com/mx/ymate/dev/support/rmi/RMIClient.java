package com.mx.ymate.dev.support.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
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
