package com.mx.ymate.excel.analysis.annotation;

import com.mx.ymate.excel.analysis.IDataHandler;
import com.mx.ymate.excel.analysis.IDefaultClass;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ImportColumn {

    /**
     * @return 列下标
     */
    int idx() default 0;

    /**
     * 表头
     *
     * @return
     */
    String title() default "";

    /**
     * 数据处理类
     *
     * @return
     */
    Class<? extends IDataHandler> handleClass() default IDataHandler.class;

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
    Class<?> parameterType() default IDefaultClass.class;
}
