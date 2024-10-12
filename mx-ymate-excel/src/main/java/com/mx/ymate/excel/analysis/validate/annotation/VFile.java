package com.mx.ymate.excel.analysis.validate.annotation;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date: 2024-10-11 17:00
 * @Description:
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VFile {

    String msg() default "";

}