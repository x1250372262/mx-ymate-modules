package com.mx.ymate.dev.support.spi.annotation;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface SpiInject {

    /**
     * 名称 默认类名（两个spi实现类的时候 可以区分开）
     *
     * @return
     */
    String name() default "";

}