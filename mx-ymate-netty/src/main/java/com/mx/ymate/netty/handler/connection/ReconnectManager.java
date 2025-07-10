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

    private final boolean backoff;
    private final boolean resetOnSuccess;
    private final int baseInterval;
    private final int maxInterval;
    private int currentInterval;

    public ReconnectManager(boolean backoff, boolean resetOnSuccess, int baseInterval, int maxInterval) {
        this.backoff = backoff;
        this.resetOnSuccess = resetOnSuccess;
        this.baseInterval = baseInterval;
        this.maxInterval = maxInterval;
        this.currentInterval = baseInterval;
    }

    public void scheduleReconnect(EventLoop eventLoop, Runnable reconnectTask) {
        eventLoop.schedule(reconnectTask, currentInterval, TimeUnit.SECONDS);
        if (backoff) {
            currentInterval = Math.min(currentInterval * 2, maxInterval);
        }
    }

    public void reset() {
        if (resetOnSuccess) {
            currentInterval = baseInterval;
        }
    }
}
