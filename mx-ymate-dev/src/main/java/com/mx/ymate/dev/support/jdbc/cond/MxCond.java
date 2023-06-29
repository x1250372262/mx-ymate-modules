package com.mx.ymate.dev.support.jdbc.cond;

import net.ymate.platform.persistence.jdbc.query.Cond;

import java.lang.annotation.*;

/**
 * @author mengxiang
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
     * 前缀
     *
     * @return
     */
    Cond.OPT opt();

    /**
     * 表里字段
     * @return
     */
    String tableField();

    /**
     * 是否检查空值
     * @return
     */
    boolean checkEmpty() default true;

}
