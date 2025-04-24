package com.mx.ymate.dev.support.mvc.i18n.validate;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VMxRSAData {

    /**
     * @return 自定参数名称
     */
    String value() default StringUtils.EMPTY;

    /**
     * @return RSA密钥数据提供者类
     * @since 2.1.0
     */
    Class<? extends IMxRSAKeyProvider> providerClass() default IMxRSAKeyProvider.class;

    /**
     * @return 自定义验证消息
     */
    String msg() default StringUtils.EMPTY;

    /**
     * @return i18nKey
     */
    String i18nKey() default StringUtils.EMPTY;
}
