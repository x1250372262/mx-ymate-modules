package com.mx.ymate.dev.support.event.annotation;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
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
