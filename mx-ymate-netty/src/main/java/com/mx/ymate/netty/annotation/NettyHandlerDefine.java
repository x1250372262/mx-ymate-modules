package com.mx.ymate.netty.annotation;


import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NettyHandlerDefine {

    /**
     * 排序 从小到大执行
     *
     * @return
     */
    int order();

    /**
     * 是否是共享 Handler
     *
     * @return
     */
    boolean sharable();
}
