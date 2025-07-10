package com.mx.ymate.netty.manager;

import cn.hutool.core.util.StrUtil;
import com.mx.ymate.netty.bean.ClientConfig;
import com.mx.ymate.netty.bean.RemoteAddress;
import com.mx.ymate.netty.impl.NettyClient;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/9.
 * @Time: 15:31.
 * @Description:
 */
public class NettyClientManager {

    private final Map<String, NettyClient> CLIENT_MAP = new ConcurrentHashMap<>();
    private final Log LOG = LogFactory.getLog(NettyClientManager.class);
    private final List<ClientConfig> clientConfigList;

    public NettyClientManager(List<ClientConfig> clientConfigList) {
        this.clientConfigList = clientConfigList;
    }

    public void initAll() {
        for (ClientConfig clientConfig : clientConfigList) {
            String name = clientConfig.getName();
            if (CLIENT_MAP.containsKey(name)) {
                LOG.warn(StrUtil.format("NettyClient[{}] 已初始化，忽略重复初始化", name));
                continue;
            }
            NettyClient result = new NettyClient(clientConfig).init();
            if (result == null) {
                LOG.error(StrUtil.format("NettyClient[{}] 初始化失败", name));
                continue;
            }
            CLIENT_MAP.put(name, result);
        }
    }

    public void init(String name) {
        ClientConfig targetConfig = clientConfigList.stream()
                .filter(cfg -> cfg.getName().equals(name))
                .findFirst()
                .orElse(null);

        if (targetConfig == null) {
            LOG.error(StrUtil.format("未找到名称为 [{}] 的客户端配置", name));
            return;
        }

        if (CLIENT_MAP.containsKey(name)) {
            LOG.warn(StrUtil.format("NettyClient[{}] 已初始化，忽略重复初始化", name));
            return;
        }
        NettyClient result = new NettyClient(targetConfig).init();
        if (result == null) {
            LOG.error(StrUtil.format("NettyClient[{}] 初始化失败", name));
            return;
        }
        CLIENT_MAP.put(name, result);
    }

    public void connect(String name, ChannelHandlerContext context, Map<String, Object> extras) {
        InetSocketAddress ipSocket = (InetSocketAddress) context.channel().remoteAddress();
        int port = ipSocket.getPort();
        String host = ipSocket.getHostString();
        connect(name, new RemoteAddress(host, port), extras);
    }

    public void connect(String name, RemoteAddress remoteAddress, Map<String, Object> extras) {
        NettyClient nettyClient = CLIENT_MAP.get(name);
        if (nettyClient == null) {
            LOG.error(StrUtil.format("NettyClient[{}] 没有初始化，请先初始化", name));
            return;
        }
        try {
            nettyClient.connect(remoteAddress, extras);
        } catch (Exception e) {
            LOG.error(StrUtil.format("NettyClient[{}] 连接失败", name), e);
        }
    }

    public void connect(String name, Map<String, Object> extras) {
        NettyClient nettyClient = CLIENT_MAP.get(name);
        if (nettyClient == null) {
            LOG.error(StrUtil.format("NettyClient[{}] 没有初始化，请先初始化", name));
            return;
        }
        try {
            nettyClient.connect(extras);
        } catch (Exception e) {
            LOG.error(StrUtil.format("NettyClient[{}] 连接失败", name), e);
        }
    }

    public void connect(String name, RemoteAddress remoteAddress) {
        NettyClient nettyClient = CLIENT_MAP.get(name);
        if (nettyClient == null) {
            LOG.error(StrUtil.format("NettyClient[{}] 没有初始化，请先初始化", name));
            return;
        }
        try {
            nettyClient.connect(remoteAddress);
        } catch (Exception e) {
            LOG.error(StrUtil.format("NettyClient[{}] 连接失败", name), e);
        }
    }

    public void connect(String name) {
        NettyClient nettyClient = CLIENT_MAP.get(name);
        if (nettyClient == null) {
            LOG.error(StrUtil.format("NettyClient[{}] 没有初始化，请先初始化", name));
            return;
        }
        try {
            nettyClient.connect();
        } catch (Exception e) {
            LOG.error(StrUtil.format("NettyClient[{}] 连接失败", name), e);
        }
    }


    public void connectAll(ChannelHandlerContext context, Map<String, Map<String, Object>> extrasMap) {
        InetSocketAddress ipSocket = (InetSocketAddress) context.channel().remoteAddress();
        int port = ipSocket.getPort();
        String host = ipSocket.getHostString();
        connectAll(new RemoteAddress(host, port), extrasMap);
    }

    public void connectAll(RemoteAddress remoteAddress, Map<String, Map<String, Object>> extrasMap) {
        CLIENT_MAP.forEach((name, nettyClient) -> {
            try {
                Map<String, Object> extras = extrasMap != null ? extrasMap.get(name) : new HashMap<>();
                nettyClient.connect(remoteAddress, extras);
            } catch (Exception e) {
                LOG.error(StrUtil.format("NettyClient[{}] 连接失败", name), e);
            }
        });
    }

    public void connectAll(Map<String, Map<String, Object>> extrasMap) {
        CLIENT_MAP.forEach((name, nettyClient) -> {
            try {
                Map<String, Object> extras = extrasMap != null ? extrasMap.get(name) : new HashMap<>();
                nettyClient.connect(extras);
            } catch (Exception e) {
                LOG.error(StrUtil.format("NettyClient[{}] 连接失败", name), e);
            }
        });
    }


    public void disconnect(String name) {
        NettyClient nettyClient = CLIENT_MAP.get(name);
        if (nettyClient != null) {
            nettyClient.disconnect();
        }
    }

    public void disconnectAll() {
        CLIENT_MAP.forEach((name, nettyClient) -> nettyClient.disconnect());
    }

    public void destroy(String name) {
        NettyClient nettyClient = CLIENT_MAP.get(name);
        if (nettyClient != null) {
            nettyClient.destroy();
        }
    }

    public void destroyAll() {
        CLIENT_MAP.forEach((name, nettyClient) -> nettyClient.destroy());
    }

    public void reconnect(String name, RemoteAddress remoteAddress, Map<String, Object> extras) {
        NettyClient nettyClient = CLIENT_MAP.get(name);
        if (nettyClient == null) {
            LOG.error(StrUtil.format("NettyClient[{}] 没有初始化，请先初始化", name));
            return;
        }
        try {
            nettyClient.disconnect();
            nettyClient.reconnect(remoteAddress, extras);
        } catch (Exception e) {
            LOG.error(StrUtil.format("NettyClient[{}] 连接失败", name), e);
        }
    }
}
