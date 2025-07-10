package com.mx.ymate.netty.handler.connection;

import cn.hutool.core.util.HexUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.ymate.platform.log.Logs;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public abstract class AbstractConnectionManager {

    private static final Log LOG = LogFactory.getLog(AbstractConnectionManager.class);
    protected final Map<String, ChannelHandlerContext> CLIENT_MAP = new ConcurrentHashMap<>();
    protected final Map<String, String> KEY_MAP = new ConcurrentHashMap<>();

    /**
     * 注册连接
     *
     * @param key 业务标识
     * @param ctx Netty 连接上下文
     */
    public void add(String key, ChannelHandlerContext ctx) {
        CLIENT_MAP.put(key, ctx);
        KEY_MAP.put(ctx.channel().id().asShortText(), key);
    }


    /**
     * 注销连接，并关闭连接
     *
     * @param key 业务标识
     */
    public void remove(String key) {
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
    public ChannelHandlerContext getContext(String key) {
        return CLIENT_MAP.get(key);
    }

    /**
     * 根据 channelId 获取业务标识
     */
    public String getKey(String channelId) {
        return KEY_MAP.get(channelId);
    }

    /**
     * 判断是否包含业务标识
     */
    public boolean containsContext(String key) {
        return CLIENT_MAP.containsKey(key);
    }

    /**
     * 判断是否包含 Channel ID
     */
    public boolean containsChannelId(String channelId) {
        return KEY_MAP.containsKey(channelId);
    }


    /**
     * 清空所有连接并关闭
     */
    public void clear() {
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

    public InetSocketAddress getInetSocketAddress(ChannelHandlerContext ctx) {
        if (ctx == null) {
            return null;
        }
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        if (remoteAddress instanceof InetSocketAddress) {
            return (InetSocketAddress) remoteAddress;
        }
        return null;
    }

    public InetSocketAddress getInetSocketAddress(String key) {
        ChannelHandlerContext ctx = getContext(key);
        if (ctx == null) {
            return null;
        }
        return getInetSocketAddress(ctx);
    }

    public String getHost(ChannelHandlerContext ctx) {
        InetSocketAddress address = getInetSocketAddress(ctx);
        if (address == null) {
            return null;
        }
        return address.getHostString();
    }

    public int getPort(ChannelHandlerContext ctx) {
        InetSocketAddress address = getInetSocketAddress(ctx);
        if (address == null) {
            return -1;
        }
        return address.getPort();
    }

    /**
     * 直接发送字符串，依赖 Netty Pipeline 中的 StringEncoder 自动编码。
     *
     * @param key 客户端唯一标识
     * @param msg 要发送的字符串
     */
    public void sendStrDirect(String key, String msg) {
        ChannelHandlerContext ctx = getContext(key);
        if (ctx != null && ctx.channel().isActive()) {
            try {
                LOG.info("直接发送字符串: key=" + key + " 原始数据=" + msg);
                ctx.writeAndFlush(msg);
            } catch (Exception e) {
                LOG.error("直接发送字符串失败", e);
            }
        } else {
            LOG.warn("连接不可用或不存在，key=" + key);
        }
    }

    /**
     * 先把字符串编码成 ByteBuf，再发送，编码格式 UTF-8。
     *
     * @param key 客户端唯一标识
     * @param msg 要发送的字符串
     */
    public void sendStrAsBuffer(String key, String msg) {
        ChannelHandlerContext ctx = getContext(key);
        if (ctx != null && ctx.channel().isActive()) {
            try {
                ByteBuf buffer = Unpooled.copiedBuffer(msg, StandardCharsets.UTF_8);
                LOG.info("发送字符串(ByteBuf): key=" + key + " 原始数据=" + msg);
                ctx.writeAndFlush(buffer);
            } catch (Exception e) {
                LOG.error("发送字符串(ByteBuf)失败", e);
            }
        } else {
            LOG.warn("连接不可用或不存在，key=" + key);
        }
    }

    /**
     * 发送 Hex 字符串，先解码成字节数组，再发送。
     *
     * @param key 客户端唯一标识
     * @param hex 16进制字符串
     */
    public void sendHex(String key, String hex) {
        ChannelHandlerContext ctx = getContext(key);
        if (ctx != null && ctx.channel().isActive()) {
            try {
                byte[] bytes = HexUtil.decodeHex(hex);
                ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
                LOG.info("发送 Hex 数据: key=" + key + " 原始数据=" + hex);
                ctx.writeAndFlush(buffer);
            } catch (Exception e) {
                LOG.error("发送 Hex 数据失败", e);
            }
        } else {
            LOG.warn("连接不可用或不存在，key=" + key);
        }
    }

    /**
     * 直接发送 ByteBuf 对象。
     *
     * @param key    客户端唯一标识
     * @param buffer 要发送的字节缓冲区
     */
    public void sendBuffer(String key, ByteBuf buffer, String raw) {
        ChannelHandlerContext ctx = getContext(key);
        if (ctx != null && ctx.channel().isActive()) {
            try {
                LOG.info("发送 ByteBuf 数据: key=" + key + " 原始数据:" + raw);
                ctx.writeAndFlush(buffer);
            } catch (Exception e) {
                LOG.error("发送 ByteBuf 失败", e);
            }
        } else {
            LOG.warn("连接不可用或不存在，key=" + key);
            // 这里如果 buffer 是外部创建的，调用者需要注意释放
        }
    }

    /**
     * 发送任意对象，要求 Netty Pipeline 有对应编码器支持。
     *
     * @param key     客户端唯一标识
     * @param message 发送的对象
     */
    public void send(String key, Object message) {
        ChannelHandlerContext ctx = getContext(key);
        if (ctx != null && ctx.channel().isActive()) {
            try {
                LOG.info("发送对象数据: key=" + key + " 原始数据:" + JSONObject.toJSONString(message));
                ctx.writeAndFlush(message);
            } catch (Exception e) {
                LOG.error("发送对象数据失败", e);
            }
        } else {
            LOG.warn("连接不可用或不存在，key=" + key);
        }
    }
}
