package com.mx.ymate.excel.analysis.bean;

import com.mx.ymate.excel.analysis.annotation.Validate;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class ValidateBean {

    private Object validate;

    private Method method;

    private Class<?> parameterType;

    private Boolean required;

    private ValidateBean(Validate validate) throws Exception {
        Class<?> validateClass = validate.validateClass();
        this.required = validate.required();
        if (!validateClass.isInterface() && StringUtils.isNotBlank(validate.method())) {
            this.validate = validateClass.newInstance();

            //得到方法对象,有参的方法需要指定参数类型
            this.parameterType = validate.parameterType();
            if (this.parameterType != null) {
                this.method = this.validate.getClass().getMethod(validate.method(), this.parameterType, ErrorInfo.class);
            } else {
                this.method = this.validate.getClass().getMethod(validate.method(), ErrorInfo.class);
            }
        }
    }


    public static ValidateBean create(Validate validate) throws Exception {
        return new ValidateBean(validate);
    }

    public Object getValidate() {
        return validate;
    }

    public void setValidate(Object validate) {
        this.validate = validate;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }
}
