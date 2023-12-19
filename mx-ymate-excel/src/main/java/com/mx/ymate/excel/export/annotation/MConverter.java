package com.mx.ymate.excel.export.annotation;


import com.mx.ymate.excel.export.converter.HandlerBean;
import com.mx.ymate.excel.export.converter.IMxDataHandler;

import java.lang.annotation.*;

/**
 * @author mengxiang
 * @Date 2020/5/20.
 * @Time: 16:57.
 * @Description: 阿里excel导出数据处理
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MConverter {

    /**
     * 数据处理类
     *
     * @return
     */
    Class<? extends IMxDataHandler> handleClass() default IMxDataHandler.class;

    /**
     * 方法名称
     *
     * @return
     */
    String method() default "";

    /**
     * 类型
     *
     * @return
     */
    HandlerBean.Type type() default HandlerBean.Type.TEXT;

}
