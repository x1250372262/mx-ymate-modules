package com.mx.ymate.dev.support.mvc.i18n.validate;

import net.ymate.platform.validation.annotation.VField;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 参数值比较验证注解
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VMxCompare {

    /**
     * 比较条件
     */
    enum Cond {
        /**
         * 相等
         */
        EQ,

        /**
         * 不相等
         */
        NOT_EQ,

        /**
         * 大于
         */
        GT,

        /**
         * 大于等于
         */
        GT_EQ,

        /**
         * 小于
         */
        LT,

        /**
         * 小于等于
         */
        LT_EQ
    }

    /**
     * @return 比较的条件
     */
    Cond cond() default Cond.EQ;

    /**
     * @return 与之比较的参数名称
     */
    String with();

    /**
     * @return 与之比较的参数及标签名称
     * @since 2.1.0 调整方法数据为VField注解类型
     */
    VField withLabel() default @VField;

    /**
     * @return 自定义验证消息
     */
    String msg() default StringUtils.EMPTY;

    /**
     * @return i18nKey
     */
    String i18nKey() default StringUtils.EMPTY;
}
