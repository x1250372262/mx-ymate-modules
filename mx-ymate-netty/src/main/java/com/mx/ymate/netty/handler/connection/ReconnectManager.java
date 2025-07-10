package com.mx.ymate.netty.handler.connection;

import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/13.
 * @Time: 11:18.
 * @Description:
 */
public class ReconnectManager {

    private final int interval;
    private final int maxAttempts;
    private int attemptCount = 0;

    public ReconnectManager(int interval, int maxAttempts) {
        this.interval = interval;
        this.maxAttempts = maxAttempts;
    }

    /**
     * 调度重连任务，返回是否已调度
     */
    public void scheduleReconnect(EventLoop eventLoop, Runnable reconnectTask) {
        if (maxAttempts > 0 && attemptCount >= maxAttempts) {
            return;
        }
        attemptCount++;
        eventLoop.schedule(reconnectTask, interval, TimeUnit.SECONDS);
    }

    public boolean canReconnect() {
        return maxAttempts <= 0 || attemptCount < maxAttempts;
    }

    /**
     * 成功连接后重置尝试次数
     */
    public void reset() {
        attemptCount = 0;
    }
}
