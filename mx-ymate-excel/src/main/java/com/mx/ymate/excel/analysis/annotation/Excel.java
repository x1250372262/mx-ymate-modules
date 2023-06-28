package com.mx.ymate.excel.analysis.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Excel {

    /**
     * exlce取值方式
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
     * exlce取值方式 默认索引
     *
     * @return
     */
    TYPE type() default TYPE.IDX;
}
