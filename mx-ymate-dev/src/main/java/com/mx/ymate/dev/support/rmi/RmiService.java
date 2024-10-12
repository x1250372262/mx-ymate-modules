package com.mx.ymate.dev.support.rmi;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date: 2024-10-11 17:00
 * @Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RmiService {

    /**
     * 名称
     *
     * @return
     */
    String name();
}
