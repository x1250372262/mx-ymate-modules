package com.mx.ymate.monitor.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ssh.ChannelType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import com.mx.ymate.dev.code.Code;
import com.mx.ymate.dev.util.WebShellUtil;
import com.mx.ymate.monitor.model.Server;
import com.mx.ymate.security.SaUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: xujianpeng.
 * @Create: 2024/2/27 11:09
 * @Description:
 */
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    /**
     * 存放会话和linux会话的关系
     */
    private static final Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * 存放会话和linux channel关系
     */
    private static final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

    /**
     * 发送指令：连接
     */
    public static final String OPERATE_CONNECT = "connect";
    /**
     * 发送指令：命令
     */
    public static final String OPERATE_COMMAND = "command";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String webSocketMsgStr = msg.text();
        if (StringUtils.isBlank(webSocketMsgStr)) {
            return;
        }
        WebSocketMsg webSocketMsg = JSON.parseObject(webSocketMsgStr, WebSocketMsg.class);
        if (!webSocketMsg.check()) {
//            failMsg(ctx, Code.ERROR.code(), "数据格式错误");
            return;
        }
        String userId = SaUtil.loginId(webSocketMsg.getToken());
        if (StringUtils.isBlank(userId)) {
//            failMsg(ctx, Code.NOT_LOGIN.code(), Code.NOT_LOGIN.msg());
            return;
        }
        Server server = Server.builder().id(webSocketMsg.getId()).build().load();
        if (server == null) {
//            failMsg(ctx, Code.NO_DATA.code(), "主机数据不存在");
            return;
        }
        String operate = webSocketMsg.getOperate();
        String key = ctx.channel().id().asShortText();
        if (OPERATE_CONNECT.equals(operate)) {
            initSsh(ctx,server.getIp(),server.getUser(),server.getPassword());
            WebShellUtil.connectToSsh(SESSION_MAP.get(key), CHANNEL_MAP.get(key), s -> {
                successMsg(ctx, s,"");
                return null;
            });
        } else if (OPERATE_COMMAND.equals(operate)) {
            WebShellUtil.sendMsg(CHANNEL_MAP.get(key), webSocketMsg.getCommand());
        } else {
            System.err.println("不支持的操作");
            WebShellUtil.close(SESSION_MAP.get(key), CHANNEL_MAP.get(key));
        }
    }

    private void successMsg(ChannelHandlerContext ctx, byte[] buffer, String operate) {
        String data = StrUtil.str(buffer, Charset.defaultCharset());
        WebSocketResult webSocketResult = new WebSocketResult(Code.SUCCESS.code(), Code.SUCCESS.msg());
        webSocketResult.setOperate(operate);
        webSocketResult.setData(data);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(webSocketResult)));
    }

    /**
     * 出现异常的处理 打印报错日志
     *
     * @param ctx   the ctx
     * @param cause the cause
     * @throws Exception the Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        //关闭上下文
        ctx.close();
    }

    /**
     * 监控浏览器上线
     *
     * @param ctx the ctx
     * @throws Exception the Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().id().asShortText() + "连接");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().id().asShortText() + "断开连接");
        String key = ctx.channel().id().asShortText();
        WebShellUtil.close(SESSION_MAP.get(key), CHANNEL_MAP.get(key));
    }

    private void initSsh(ChannelHandlerContext ctx, String ip,String user,String password) {
        Session session = WebShellUtil.getSession(ip,22, user, password);
        if (session == null) {
            throw new RuntimeException("linux会话获取失败");
        }
        Channel channel = WebShellUtil.getChannel(session, ChannelType.SHELL);
        if (channel == null) {
            throw new RuntimeException("channel获取失败");
        }
        String key = ctx.channel().id().asShortText();
        SESSION_MAP.put(key, session);
        CHANNEL_MAP.put(key, channel);
    }

    private void sendMsg(ChannelHandlerContext ctx, byte[] buffer) {
        String msg = StrUtil.str(buffer, Charset.defaultCharset());
        ctx.channel().writeAndFlush(new TextWebSocketFrame(msg));
    }

    public static class WebSocketMsg {

        private String id;

        private String operate;

        private String command;

        private String token;

        public boolean check() {
            return StringUtils.isNotBlank(token) && StringUtils.isNotBlank(operate) && StringUtils.isNotBlank(id);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOperate() {
            return operate;
        }

        public void setOperate(String operate) {
            this.operate = operate;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }


    public static class WebSocketResult {

        private String code;

        private String msg;

        private String operate;

        private String data;


        public WebSocketResult(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getOperate() {
            return operate;
        }

        public void setOperate(String operate) {
            this.operate = operate;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

}
