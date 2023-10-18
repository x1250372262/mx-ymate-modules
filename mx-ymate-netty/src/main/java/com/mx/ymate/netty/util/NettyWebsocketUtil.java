package com.mx.ymate.netty.util;

import cn.hutool.core.util.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.ymate.platform.log.Logs;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Author: mengxiang.
 * @create: 2022-04-29 13:25
 * @Description:
 */
public class NettyWebsocketUtil {


    public static void store(String key, ChannelHandlerContext channelHandlerContext) {
        NettyWebsocketStore.put(key, channelHandlerContext);
    }

    public static void closeAll() {
        for (Map.Entry<String, ChannelHandlerContext> channelHandlerContextEntry : NettyWebsocketStore.getClientMap().entrySet()) {
            channelHandlerContextEntry.getValue().close();
        }
        NettyWebsocketStore.clear();
    }

    public static void close(String key) {
        ChannelHandlerContext channelHandlerContext = NettyWebsocketStore.getChannelHandlerContext(key);
        if (channelHandlerContext != null) {
            String channelId = channelHandlerContext.channel().id().asShortText();
            channelHandlerContext.close();
            NettyWebsocketStore.removeKey(channelId);
            NettyWebsocketStore.removeChannelHandlerContext(key);
        }
    }

    public static String getChannelId(String key) {
        ChannelHandlerContext channelHandlerContext = NettyWebsocketStore.getChannelHandlerContext(key);
        if (channelHandlerContext != null) {
            return channelHandlerContext.channel().id().asShortText();
        }
        return "";
    }

    public static String getKey(ChannelHandlerContext channelHandlerContext) {
        String channelId = channelHandlerContext.channel().id().asShortText();
        return NettyWebsocketStore.getKey(channelId);
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

    public static void sendBuffer(String key, ByteBuf buffer, String raw) {
        ChannelHandlerContext channelHandlerContext = NettyWebsocketStore.getChannelHandlerContext(key);
        if (channelHandlerContext != null) {
            Logs.get().getLogger().info("key:===" + "message:" + raw);
            try {
                channelHandlerContext.writeAndFlush(buffer);
            } catch (Exception exception) {
                Logs.get().getLogger().error("发送失败:", exception);
            }
        }
    }

}
