package com.mx.ymate.dev.support.mvc.i18n.validate;

import net.ymate.platform.commons.util.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 日期字符串参数验证注解
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VMxDateTime {

    /**
     * @return 自定参数名称
     */
    String value() default StringUtils.EMPTY;

    /**
     * @return 日期格式字符串
     */
    String pattern() default DateTimeUtils.YYYY_MM_DD_HH_MM_SS;

    /**
     * @return 仅接收单日期(即所选日期的00点00分00秒0毫秒到所选日期的23点59分59秒0毫秒)
     */
    boolean single() default true;

    /**
     * @return 时间段字符串之间的分割符号
     */
    String separator() default "/";

    /**
     * @return 时间段之间的天数最大差值，默认为0表示不限制
     */
    int maxDays() default 0;

    /**
     * @return 自定义验证消息
     */
    String msg() default StringUtils.EMPTY;

    /**
     * @return i18nKey
     */
    String i18nKey() default StringUtils.EMPTY;
}
