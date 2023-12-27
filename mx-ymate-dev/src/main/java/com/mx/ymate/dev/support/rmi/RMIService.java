package com.mx.ymate.dev.support.rmi;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RMIService {

    /**
     * 名称
     *
     * @return
     */
    String name();
}
