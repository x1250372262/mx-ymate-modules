package com.mx.ymate.dev.support.rmi;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.log.ILogger;
import net.ymate.platform.log.Logs;
import org.apache.commons.lang3.StringUtils;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: mengxiang.
 * @Date: 2024-10-11 17:00
 * @Description:
 */
public class RmiServer {

    private static Registry registry;

    private static final List<String> RMI_SERVER_LIST = new ArrayList<>();

    private static final ILogger LOG = Logs.get().getLogger();

    private static final String RMI_SERVER_STOP = "RMI服务`{}`已停止";
    private static final String RMI_SERVER_START = "RMI服务`{}`已开启";
    private static final String RMI_SERVER_START_ERROR = "RMI服务`{}`开启失败";

    private static final String RMI_SERVER_STOP_ERROR = "RMI服务`{}`停止失败";

    private static final String RMI_SERVER_INIT_SUCCESS = "RMI初始化成功,端口:{}";
    private static final String RMI_SERVER_INIT_ERROR = "RMI初始化失败:{}";

    private RmiServer() {
    }

    public static RmiServer init(String host, int port) {
        try {
            if (registry == null) {
                // 指定服务端IP地址
                System.setProperty("java.rmi.server.hostname", host);
                registry = LocateRegistry.createRegistry(port);
            }
            LOG.info(StrUtil.format(RMI_SERVER_INIT_SUCCESS, port));
        } catch (RemoteException e) {
            LOG.error(StrUtil.format(RMI_SERVER_INIT_ERROR, e.getMessage()));
            throw new RuntimeException(StrUtil.format(RMI_SERVER_INIT_ERROR, e.getMessage()));
        }
        return new RmiServer();
    }

    public static RmiServer init(String host) {
        return init(host, Registry.REGISTRY_PORT);
    }

    public void publishServices(String packageName) {
        Set<Class<?>> rmiServiceClassList = ClassUtil.scanPackageByAnnotation(packageName, RmiService.class);
        if (CollUtil.isEmpty(rmiServiceClassList)) {
            return;
        }
        try {
            for (Class<?> serviceClass : rmiServiceClassList) {
                RmiService rmiService = AnnotationUtil.getAnnotation(serviceClass, RmiService.class);
                if (rmiService == null) {
                    continue;
                }
                Remote remote;
                Bean bean = AnnotationUtil.getAnnotation(serviceClass, Bean.class);
                if (bean != null) {
                    remote = (Remote) YMP.get().getBeanFactory().getBean(serviceClass);
                } else {
                    remote = (Remote) serviceClass.newInstance();
                }
                String bindName = StringUtils.defaultIfBlank(rmiService.name(), serviceClass.getSimpleName());

                registry.rebind(bindName, remote);
                RMI_SERVER_LIST.add(bindName);
                LOG.info(StrUtil.format(RMI_SERVER_START, bindName));
            }
        } catch (Exception e) {
            throw new RuntimeException(StrUtil.format(RMI_SERVER_START_ERROR, e.getMessage()));
        }
    }

    public static void closeAll() {
        if (CollUtil.isEmpty(RMI_SERVER_LIST)) {
            return;
        }
        try {
            for (String rmiServiceName : RMI_SERVER_LIST) {
                // 解绑远程对象
                registry.unbind(rmiServiceName);
                // 停止 RMI 注册表
                UnicastRemoteObject.unexportObject(registry, true);
                LOG.info(StrUtil.format(RMI_SERVER_STOP, rmiServiceName));
            }
        } catch (Exception e) {
            throw new RuntimeException(StrUtil.format(RMI_SERVER_STOP_ERROR, e.getMessage()));
        }
        registry = null;
        RMI_SERVER_LIST.clear();
    }
}
