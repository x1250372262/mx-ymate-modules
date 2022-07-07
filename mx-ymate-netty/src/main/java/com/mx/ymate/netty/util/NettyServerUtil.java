package com.mx.ymate.netty.util;

import cn.hutool.core.util.HexUtil;
import com.mx.spring.dev.support.log.MxLog;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Author: mengxiang.
 * @create: 2022-04-29 13:25
 * @Description:
 */
public class NettyServerUtil {


    public static void store(String key, ChannelHandlerContext channelHandlerContext) {
        NettyServerStore.put(key, channelHandlerContext);
    }

    public static void closeAll() {
        for (Map.Entry<String, ChannelHandlerContext> channelHandlerContextEntry : NettyServerStore.getClientMap().entrySet()) {
            channelHandlerContextEntry.getValue().close();
        }
        NettyServerStore.clear();
    }

    public static void close(String key) {
        ChannelHandlerContext channelHandlerContext = NettyServerStore.getChannelHandlerContext(key);
        if (channelHandlerContext != null) {
            String channelId = channelHandlerContext.channel().id().asShortText();
            channelHandlerContext.close();
            NettyServerStore.removeKey(channelId);
            NettyServerStore.removeChannelHandlerContext(key);
        }
    }

    public static String getChannelId(String key) {
        ChannelHandlerContext channelHandlerContext = NettyServerStore.getChannelHandlerContext(key);
        if (channelHandlerContext != null) {
            return channelHandlerContext.channel().id().asShortText();
        }
        return "";
    }

    public static String getKey(ChannelHandlerContext channelHandlerContext) {
        String channelId = channelHandlerContext.channel().id().asShortText();
        return NettyServerStore.getKey(channelId);
    }

    /**
     * 发送16进制字符串
     *
     * @param key
     * @param hex
     */
    public static void sendHex(String key, String hex) {
        ByteBuf buffer = Unpooled.wrappedBuffer(HexUtil.decodeHex(hex));
        sendBuffer(key, buffer);
    }


    /**
     * 发送字符串
     *
     * @param key
     * @param message
     */
    public static void sendStr(String key, String message) {
        ByteBuf buffer = Unpooled.wrappedBuffer(message.getBytes(StandardCharsets.UTF_8));
        sendBuffer(key, buffer);
    }

    public static void sendBuffer(String key, ByteBuf buffer) {
        ChannelHandlerContext channelHandlerContext = NettyServerStore.getChannelHandlerContext(key);
        if (channelHandlerContext != null) {
            MxLog.debug("key:===" + "message:" + ByteBufUtil.hexDump(buffer));
            try {
                channelHandlerContext.writeAndFlush(buffer);
            } catch (Exception exception) {
                MxLog.error("发送失败:", exception);
            }
        }
    }

}
