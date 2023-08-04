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
public class NettyClientUtil {


    public static void store(String key, ChannelHandlerContext channelHandlerContext) {
        NettyClientStore.put(key, channelHandlerContext);
    }

    public static void closeAll() {
        for (Map.Entry<String, ChannelHandlerContext> channelHandlerContextEntry : NettyClientStore.getClientMap().entrySet()) {
            channelHandlerContextEntry.getValue().close();
        }
        NettyClientStore.clear();
    }

    public static void close(String key) {
        ChannelHandlerContext channelHandlerContext = NettyClientStore.getChannelHandlerContext(key);
        if (channelHandlerContext != null) {
            String channelId = channelHandlerContext.channel().id().asShortText();
            channelHandlerContext.close();
            NettyClientStore.removeKey(channelId);
            NettyClientStore.removeChannelHandlerContext(key);
        }
    }

    public static String getChannelId(String key) {
        ChannelHandlerContext channelHandlerContext = NettyClientStore.getChannelHandlerContext(key);
        if (channelHandlerContext != null) {
            return channelHandlerContext.channel().id().asShortText();
        }
        return "";
    }

    public static String getKey(ChannelHandlerContext channelHandlerContext) {
        String channelId = channelHandlerContext.channel().id().asShortText();
        return NettyClientStore.getKey(channelId);
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
        ChannelHandlerContext channelHandlerContext = NettyClientStore.getChannelHandlerContext(key);
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
