package com.mx.ymate.netty.websocket.annotation;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
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
