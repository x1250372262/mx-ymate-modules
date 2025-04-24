package com.mx.ymate.excel.analysis.annotation;

import com.mx.ymate.excel.analysis.IValidate;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: excel导入注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validate {

    /**
     * 是否必填
     *
     * @return
     */
    boolean required() default false;

    /**
     * 自定义验证类
     *
     * @return
     */
    Class<? extends IValidate> validateClass() default IValidate.class;

    /**
     * 方法名称
     *
     * @return
     */
    String method() default "";

    /**
     * 方法参数类型
     *
     * @return
     */
    Class<?> parameterType();
}
