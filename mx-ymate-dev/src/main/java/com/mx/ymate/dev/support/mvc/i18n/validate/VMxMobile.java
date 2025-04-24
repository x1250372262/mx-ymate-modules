package com.mx.ymate.dev.support.mvc.i18n.validate;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 手机号码验证注解
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VMxMobile {

    /**
     * @return 自定义正则表达式(将覆盖原判断逻辑)
     */
    String regex() default StringUtils.EMPTY;

    /**
     * @return 自定义验证消息
     */
    String msg() default StringUtils.EMPTY;

    /**
     * @return i18nKey
     */
    String i18nKey() default StringUtils.EMPTY;
}
