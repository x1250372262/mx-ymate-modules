package com.mx.ymate.excel.analysis.annotation;

import com.mx.ymate.excel.analysis.IValidate;

import java.lang.annotation.*;

/**
 * @author mengxiang
 * @Date 2020.01.02.
 * @Time: 14:30.
 * @Description: excel导入验证
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
