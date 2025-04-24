package com.mx.ymate.qwen.annotation;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface QwenParam {

    /**
     * 参数名
     *
     * @return
     */
    String name();

    /**
     * 参数描述
     *
     * @return
     */
    String description();

}
