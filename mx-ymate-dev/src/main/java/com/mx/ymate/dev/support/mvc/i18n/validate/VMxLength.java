package com.mx.ymate.dev.support.mvc.i18n.validate;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 字符串长度验证注解
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VMxLength {

    /**
     * @return 设置最小长度，0为不限制
     */
    int min() default 0;

    /**
     * @return 设置最大长度，0为不限制
     */
    int max() default 0;

    /**
     * @return 设置固定长度值，0为不限制
     */
    int eq() default 0;

    /**
     * @return 自定义验证消息
     */
    String msg() default StringUtils.EMPTY;

    /**
     * @return i18nKey
     */
    String i18nKey() default StringUtils.EMPTY;
}
