package com.mx.ymate.qwen.annotation;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
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
