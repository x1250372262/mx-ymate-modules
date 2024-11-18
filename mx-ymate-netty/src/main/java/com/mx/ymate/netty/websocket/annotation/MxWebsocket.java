package com.mx.ymate.netty.websocket.annotation;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @create: 2024-05-14 14:00
 * @Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MxWebsocket {

    /**
     * websocket路径
     */
    String mapping();

}
