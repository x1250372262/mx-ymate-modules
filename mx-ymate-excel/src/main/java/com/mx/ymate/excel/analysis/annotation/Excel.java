package com.mx.ymate.excel.analysis.annotation;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date: 2024-10-11 17:00
 * @Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Excel {

    /**
     * excel取值方式
     */
    enum TYPE {
        /**
         * 标题
         */
        TITLE,
        /**
         * 索引
         */
        IDX
    }

    /**
     * excel取值方式 默认索引
     *
     * @return
     */
    TYPE type() default TYPE.IDX;
}
