package com.mx.ymate.dev.support.mvc.i18n.validate;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 数值类型参数验证注解
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VMxNumeric {

    /**
     * @return 仅检查值是否为数字（当取值为true时生效，同时其它参数将失效）
     * @since 2.1.3
     */
    boolean digits() default false;

    /**
     * @return 设置最小值，0为不限制
     */
    double min() default 0;

    /**
     * @return 设置最大值，0为不限制
     */
    double max() default 0;

    /**
     * @return 设置值相等，0为不限制
     */
    double eq() default 0;

    /**
     * @return 设置小数位数，0为不限制
     */
    int decimals() default 0;

    /**
     * @return 自定义验证消息
     */
    String msg() default StringUtils.EMPTY;

    /**
     * @return i18nKey
     */
    String i18nKey() default StringUtils.EMPTY;
}
