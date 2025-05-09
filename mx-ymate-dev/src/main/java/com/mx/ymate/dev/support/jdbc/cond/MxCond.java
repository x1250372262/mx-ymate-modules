package com.mx.ymate.dev.support.jdbc.cond;

import net.ymate.platform.persistence.jdbc.query.Cond;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 查询条件构建
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MxCond {


    /**
     * 前缀
     *
     * @return
     */
    String prefix() default "";


    /**
     * 类型
     *
     * @return
     */
    Cond.OPT opt();

    /**
     * 表里字段
     *
     * @return
     */
    String tableField();

    /**
     * 是否检查空值
     *
     * @return
     */
    boolean checkEmpty() default true;

}
