package com.mx.ymate.netty.handler;

import cn.hutool.core.util.HexUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.nio.charset.StandardCharsets;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 消息发送工具类，提供多种发送字符串和字节缓冲区的方式。
 */
public class MessageSender {

    private static final Log LOG = LogFactory.getLog(MessageSender.class);

    private MessageSender() {
    }

    /**
     * 直接发送字符串，依赖 Netty Pipeline 中的 StringEncoder 自动编码。
     *
     * @param key 客户端唯一标识
     * @param msg 要发送的字符串
     */
    public static void sendStrDirect(String key, String msg) {
        ChannelHandlerContext ctx = ConnectionManager.getContext(key);
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
    public static void sendStrAsBuffer(String key, String msg) {
        ChannelHandlerContext ctx = ConnectionManager.getContext(key);
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
    public static void sendHex(String key, String hex) {
        ChannelHandlerContext ctx = ConnectionManager.getContext(key);
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
    public static void sendBuffer(String key, ByteBuf buffer, String raw) {
        ChannelHandlerContext ctx = ConnectionManager.getContext(key);
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
    public static void send(String key, Object message) {
        ChannelHandlerContext ctx = ConnectionManager.getContext(key);
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
