
package com.mx.ymate.dev.util;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ssh.ChannelType;
import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.function.Function;


/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: webShell工具类
 */
public class WebShellUtil {

    /**
     * 获取session
     *
     * @param sshHost
     * @param sshPort
     * @param sshUser
     * @param sshPass
     * @return
     */
    public static Session getSession(String sshHost, int sshPort, String sshUser, String sshPass) {
        return JschUtil.openSession(sshHost, sshPort, sshUser, sshPass);
    }

    /**
     * 获取channel
     *
     * @param session
     * @param channelType
     * @return
     */
    public static Channel getChannel(Session session, ChannelType channelType) {
        return JschUtil.openChannel(session, channelType);
    }


    /**
     * 关闭连接
     *
     * @param session
     * @param channel
     */
    public static void close(Session session, Channel channel) {
        session.disconnect();
        channel.disconnect();
    }

    /**
     * 连接服务器
     *
     * @param session
     * @param channel
     * @param function
     * @throws JSchException
     */
    public static void connectToSsh(Session session, Channel channel, Function<byte[], Void> function) throws JSchException {

        ThreadUtil.execAsync(() -> {
            //读取终端返回的信息流
            try (InputStream inputStream = channel.getInputStream()) {
                //循环读取
                byte[] buffer = new byte[2048];
                int i;
                //如果没有数据来，线程会一直阻塞在这个地方等待数据。
                while ((i = inputStream.read(buffer)) != -1) {
                    function.apply(Arrays.copyOfRange(buffer, 0, i));
                }
            } catch (IOException e) {
                System.err.println(StrUtil.format("读取终端返回的信息流异常:{}", e.getMessage()));
            } finally {
                //断开连接后关闭会话
                close(session, channel);
            }
        });
    }

    /**
     * 将消息转发到终端
     *
     * @param channel Channel
     * @author zmzhou
     * @date 2021/2/23 21:13
     */
    public static void sendMsg(Channel channel, String command) {
        if (channel != null) {
            try {
                OutputStream outputStream = channel.getOutputStream();
                outputStream.write(command.getBytes());
                outputStream.flush();
            } catch (IOException e) {
                System.err.println(StrUtil.format("web shell将消息转发到终端异常:{}", e.getMessage()));
            }
        }
    }
}
