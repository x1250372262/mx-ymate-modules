package com.mx.ymate.security.interceptor;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.strategy.SaStrategy;
import com.mx.ymate.dev.code.Code;
import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.util.PathMatchUtil;
import com.mx.ymate.satoken.annotation.NoLogin;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.SaUtil;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.code.SecurityCode;
import com.mx.ymate.security.base.model.SecurityUser;
import com.mx.ymate.security.dao.ISecurityUserDao;
import com.mx.ymate.security.exception.MxLoginException;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.beans.intercept.AbstractInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.core.beans.intercept.InterceptException;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

public class MxSaAnnotationInterceptor extends AbstractInterceptor {

    private final ISecurityConfig securityConfig = Security.get().getConfig();

    private final ISecurityUserDao iSecurityUserDao = YMP.get().getBeanFactory().getBean(ISecurityUserDao.class);


    /**
     * 构建： 注解式鉴权 - 拦截器
     */
    public MxSaAnnotationInterceptor() {
    }

    /**
     * 检查路径 返回true代表不验证
     *
     * @param requestUri
     * @return
     */
    private boolean checkPath(String requestUri) {
        if (requestUri.endsWith("/")) {
            requestUri = requestUri.substring(0, requestUri.length() - 1);
        }
        String excludePathPatterns = securityConfig.excludePathPatterns();
        if (StringUtils.isBlank(excludePathPatterns)) {
            return false;
        }
        String[] excludePathPatternArray = excludePathPatterns.split("\\|");
        if (excludePathPatternArray.length == 0) {
            return false;
        }
        boolean flag = false;
        for (String excludePathPattern : excludePathPatternArray) {
            flag = new PathMatchUtil().isMatch(requestUri, excludePathPattern);
            if (flag) {
                break;
            }
        }
        return flag;
    }

    @Override
    protected Object before(InterceptContext context) throws InterceptException {
        String requestUri = WebContext.getRequest().getRequestURI();
        if (checkPath(requestUri)) {
            return null;
        }
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
            SaStrategy.instance.checkMethodAnnotation.accept(method);
            SecurityUser securityUser = iSecurityUserDao.findById(SaUtil.loginId());
            if (securityUser == null) {
                throw new NotLoginException(Code.NOT_LOGIN.msg(), null, null);
            }
            if (Objects.equals(Constants.BOOL_TRUE, securityUser.getDisableStatus())) {
                throw new MxLoginException(SecurityCode.SECURITY_LOGIN_USER_DISABLE.msg());
            }
            securityConfig.loginHandlerClass().checkLoginCustom(securityUser);

        } catch (NotLoginException notLoginException) {
            return MxResult.create(Code.NOT_LOGIN).toJsonView();
        } catch (MxLoginException mxLoginException) {
            return MxResult.create(Code.NOT_LOGIN.code()).msg(mxLoginException.getMessage()).toJsonView();
        } catch (NotPermissionException | NotRoleException notPermissionException) {
            return MxResult.create(Code.NOT_PERMISSION).toJsonView();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 通过验证
        return null;
    }

    @Override
    protected Object after(InterceptContext context) throws InterceptException {
        return null;
    }


}
