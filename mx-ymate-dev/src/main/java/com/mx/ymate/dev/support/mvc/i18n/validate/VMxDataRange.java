package com.mx.ymate.dev.support.mvc.i18n.validate;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VMxDataRange {

    /**
     * @return 允许参数集合，若提供者类存在则此值将被忽略
     */
    String[] value() default {};

    /**
     * @return 忽略大小写
     */
    boolean ignoreCase() default true;

    /**
     * @return 允许参数集合数据提供者类
     * @since 2.1.0
     */
    Class<? extends IMxDataRangeValuesProvider> providerClass() default IMxDataRangeValuesProvider.class;

    /**
     * @return 自定义验证消息
     */
    String msg() default StringUtils.EMPTY;

    /**
     * @return i18nKey
     */
    String i18nKey() default StringUtils.EMPTY;
}