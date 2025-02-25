package com.mx.ymate.dev.support.event.annotation;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @create: 2025-02-25
 * @Description:
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Initializer {

    /**
     * 排序 从小到大执行
     *
     * @return
     */
    int value() default 0;

}
