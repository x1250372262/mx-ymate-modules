package com.mx.ymate.excel.analysis.validate.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VFile {

    String msg() default "";

}