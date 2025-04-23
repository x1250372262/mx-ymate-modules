package com.mx.ymate.security.base.annotation;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description: 不需要检查锁屏注解
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoCheck {

}
