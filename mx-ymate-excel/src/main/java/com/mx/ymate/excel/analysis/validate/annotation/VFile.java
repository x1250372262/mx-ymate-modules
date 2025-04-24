package com.mx.ymate.excel.analysis.validate.annotation;

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
public @interface VFile {

    String msg() default StringUtils.EMPTY;

    String i18nKey() default StringUtils.EMPTY;

}