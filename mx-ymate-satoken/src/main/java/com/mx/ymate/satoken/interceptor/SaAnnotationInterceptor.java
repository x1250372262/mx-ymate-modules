package com.mx.ymate.satoken.interceptor;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.strategy.SaStrategy;
import com.mx.ymate.dev.code.Code;
import com.mx.ymate.dev.result.MxResult;
import com.mx.ymate.satoken.annotation.NoLogin;
import net.ymate.platform.core.beans.intercept.AbstractInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.core.beans.intercept.InterceptException;

import java.lang.reflect.Method;

/**
 * 注解式鉴权 - 拦截器
 *
 * @author kong
 */
public class SaAnnotationInterceptor extends AbstractInterceptor {

    /**
     * 构建： 注解式鉴权 - 拦截器
     */
    public SaAnnotationInterceptor() {
    }

    @Override
    protected Object before(InterceptContext context) throws InterceptException {
        Method method = context.getTargetMethod();
        // 进行验证
        try {
            //有notlogin注解就不验证
            NoLogin annotation = method.getAnnotation(NoLogin.class);
            if (annotation != null) {
                return null;
            }
            annotation = method.getDeclaringClass().getAnnotation(NoLogin.class);
            if (annotation != null) {
                return null;
            }
            SaStrategy.me.checkMethodAnnotation.accept(method);
        } catch (NotLoginException notLoginException) {
            return MxResult.create(Code.NOT_LOGIN);
        }
        // 通过验证
        return null;
    }

    @Override
    protected Object after(InterceptContext context) throws InterceptException {
        return null;
    }


}
