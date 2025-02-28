package com.mx.ymate.qwen.annotation;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @create: 2025-02-25
 * @Description:
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QwenFunction {

    /**
     * 方法名
     *
     * @return
     */
    String name();

    /**
     * 方法描述
     *
     * @return
     */
    String description();

}
