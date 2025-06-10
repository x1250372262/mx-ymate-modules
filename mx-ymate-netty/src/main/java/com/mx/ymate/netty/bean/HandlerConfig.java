package com.mx.ymate.netty.bean;

import io.netty.channel.ChannelHandler;
import net.ymate.platform.commons.util.ClassUtils;

import java.util.function.Supplier;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/4.
 * @Time: 16:09.
 * @Description:
 */
public class HandlerConfig {

    private final Class<? extends ChannelHandler> handlerClass;
    private final ChannelHandler handlerObj;
    private final int order;
    private final boolean sharable;
    private final Supplier<ChannelHandler> handlerSupplier;

    public HandlerConfig(ChannelHandler handlerObj, int order, boolean sharable) {
        this.handlerClass = handlerObj.getClass();
        this.order = order;
        this.sharable = sharable;
        this.handlerObj = handlerObj;
        this.handlerSupplier = null;
    }

    public HandlerConfig(Class<? extends ChannelHandler> handlerClass, int order, boolean sharable) {
        this.handlerClass = handlerClass;
        this.order = order;
        this.sharable = sharable;
        this.handlerObj = sharable ? ClassUtils.impl(handlerClass, ChannelHandler.class) : null;
        this.handlerSupplier = null;
    }

    public HandlerConfig(Supplier<ChannelHandler> supplier, int order,boolean sharable) {
        if (supplier == null) {
            throw new IllegalArgumentException("supplier 不能为空");
        }
        ChannelHandler instance = supplier.get();
        this.handlerSupplier = supplier;
        this.handlerObj = sharable ? instance : null;
        this.handlerClass = instance.getClass();
        this.sharable = sharable;
        this.order = order;
    }
    public boolean isSharable() {
        return sharable;
    }

    public ChannelHandler getHandlerObj() {
        return handlerObj;
    }

    public Class<? extends ChannelHandler> getHandlerClass() {
        return handlerClass;
    }

    // ✅ 推荐调用方式
    public ChannelHandler newInstance() {
        if (sharable && handlerObj != null) {
            return handlerObj;
        } else if (handlerSupplier != null) {
            return handlerSupplier.get();
        } else {
            return ClassUtils.impl(handlerClass, ChannelHandler.class);
        }
    }

    public int getOrder() {
        return order;
    }
}
