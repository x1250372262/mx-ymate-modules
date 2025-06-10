package com.mx.ymate.netty.manager;

import cn.hutool.core.util.StrUtil;
import com.mx.ymate.netty.INettyConfig;
import com.mx.ymate.netty.bean.ServerConfig;
import com.mx.ymate.netty.impl.NettyServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/9.
 * @Time: 15:31.
 * @Description:
 */
public class NettyServerManager {

    private final Map<String, NettyServer> SERVER_MAP = new ConcurrentHashMap<>();
    private final Log LOG = LogFactory.getLog(NettyServerManager.class);
    private final List<ServerConfig> serverConfigList;

    public NettyServerManager(List<ServerConfig> serverConfigList) {
        this.serverConfigList = serverConfigList;
    }

    public void initAll() {
        for (ServerConfig serverConfig : serverConfigList) {
            NettyServer result = SERVER_MAP.computeIfAbsent(serverConfig.getName(), name -> new NettyServer(serverConfig).init());
            if (result == null) {
                LOG.error(StrUtil.format("NettyServer[{}] 初始化失败", serverConfig.getName()));
            }
        }
    }

    public void init(String name) {
        ServerConfig targetConfig = serverConfigList.stream()
                .filter(cfg -> cfg.getName().equals(name))
                .findFirst()
                .orElse(null);

        if (targetConfig == null) {
            LOG.error(StrUtil.format("未找到名称为 [{}] 的服务器配置", name));
            return;
        }

        NettyServer result = SERVER_MAP.computeIfAbsent(name, n -> new NettyServer(targetConfig).init());
        if (result == null) {
            LOG.error(StrUtil.format("NettyServer[{}] 初始化失败", name));
        }
    }

    public void start(String name){
        NettyServer nettyServer = SERVER_MAP.get(name);
        if (nettyServer == null) {
            LOG.error(StrUtil.format("NettyServer[{}] 没有初始化，请先初始化", name));
            return;
        }
        try {
            nettyServer.start();
        } catch (Exception e) {
            LOG.error(StrUtil.format("NettyServer[{}] 启动失败", name), e);
        }
    }

    public void startAll() {
        SERVER_MAP.forEach((name, nettyServer) -> {
            try {
                nettyServer.start();
            } catch (Exception e) {
                LOG.error(StrUtil.format("NettyServer[{}] 启动失败", name), e);
            }
        });
    }


    public void stop(String name) {
        NettyServer nettyServer = SERVER_MAP.get(name);
        if (nettyServer != null) {
            nettyServer.stop();
        }
    }

    public void stopAll() {
        SERVER_MAP.forEach((name, nettyServer) -> nettyServer.stop());
    }
}
