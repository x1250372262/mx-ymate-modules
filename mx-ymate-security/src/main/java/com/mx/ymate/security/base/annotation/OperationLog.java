package com.mx.ymate.security.base.annotation;


import com.mx.ymate.security.base.enums.OperationType;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 操作日志注解
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {

    /**
     * 标题
     */
    String title() default StringUtils.EMPTY;

    /**
     * i18nKey
     * @return
     */
    String i18nKey() default StringUtils.EMPTY;

    /**
     * 功能
     */
    OperationType operationType() default OperationType.UNKNOWN;

}
