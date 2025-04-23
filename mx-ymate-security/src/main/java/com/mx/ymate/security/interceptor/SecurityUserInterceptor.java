package com.mx.ymate.security.interceptor;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.stp.StpUtil;
import com.mx.ymate.dev.code.Code;
import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.util.PathMatchUtil;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.SaUtil;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.annotation.NoLogin;
import com.mx.ymate.security.base.annotation.NoPermission;
import com.mx.ymate.security.base.code.SecurityCode;
import com.mx.ymate.security.base.exception.MxLockException;
import com.mx.ymate.security.base.exception.MxLoginException;
import com.mx.ymate.security.base.model.SecurityUser;
import com.mx.ymate.security.web.dao.ISecurityUserDao;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.beans.annotation.Interceptor;
import net.ymate.platform.core.beans.intercept.AbstractInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.core.beans.intercept.InterceptException;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

import static com.mx.ymate.dev.constants.Constants.XG;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description: 用户拦截器
 */
@Interceptor
public class SecurityUserInterceptor extends AbstractInterceptor {

    private final ISecurityConfig securityConfig = Security.get().getConfig();

    private final ISecurityUserDao iSecurityUserDao = YMP.get().getBeanFactory().getBean(ISecurityUserDao.class);


    /**
     * 构建： 注解式鉴权 - 拦截器
     */
    public SecurityUserInterceptor() {
    }

    /**
     * 检查路径 返回true代表不验证
     *
     * @param requestUri
     * @return
     */
    private boolean checkPath(String requestUri) {
        if (requestUri.endsWith(XG)) {
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
        Class<?> targetClass = method.getDeclaringClass();
        // 进行验证
        try {
            //有notlogin注解就不验证
            NoLogin annotation = method.getAnnotation(NoLogin.class);
            if (annotation != null) {
                return null;
            }
            annotation = targetClass.getAnnotation(NoLogin.class);
            if (annotation != null) {
                return null;
            }
            StpUtil.checkLogin();
            SecurityUser securityUser = iSecurityUserDao.findById(SaUtil.loginId());
            if (securityUser == null) {
                throw new NotLoginException(Code.NOT_LOGIN.msg(), null, null);
            }
            if (Objects.equals(Constants.BOOL_TRUE, securityUser.getDisableStatus())) {
                throw new MxLoginException(SecurityCode.SECURITY_LOGIN_USER_DISABLE.msg());
            }
            securityConfig.loginHandlerClass().checkLoginCustom(securityUser);

            //检查锁定
            checkLock(securityUser);
            //权限拦截
            checkPermission(targetClass, method);
        } catch (NotLoginException notLoginException) {
            return MxResult.create(Code.NOT_LOGIN).toJsonView();
        } catch (MxLoginException mxLoginException) {
            return MxResult.create(Code.NOT_LOGIN.code()).msg(mxLoginException.getMessage()).toJsonView();
        } catch (NotPermissionException | NotRoleException notPermissionException) {
            return MxResult.create(Code.NOT_PERMISSION).toJsonView();
        } catch (MxLockException lockException) {
            return MxResult.create(Code.LOCKED).toJsonView();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 通过验证
        return null;
    }

    @Override
    protected Object after(InterceptContext context) throws InterceptException {
        return super.after(context);
    }

    /**
     * 检查锁定
     *
     * @param method
     */
    private void checkLock(SecurityUser securityUser) throws Exception {
        if (securityConfig.checkLock()) {
            if (SaUtil.checkLock(securityUser.getId())) {
                throw new MxLockException(SecurityCode.SECURITY_LOGIN_USER_LOCK_SCREEN.msg());
            }
        }
    }


    /**
     * 检查权限
     *
     * @param method
     */
    private void checkPermission(Class<?> targetClass, Method method) {
        //有NoPermission注解就不验证
        NoPermission noPermission = method.getAnnotation(NoPermission.class);
        if (noPermission != null) {
            return;
        }
        noPermission = targetClass.getAnnotation(NoPermission.class);
        if (noPermission != null) {
            return;
        }
        SaCheckPermission saCheckPermission = method.getAnnotation(SaCheckPermission.class);
        if (saCheckPermission == null) {
            saCheckPermission = targetClass.getAnnotation(SaCheckPermission.class);
        }
        if (saCheckPermission == null) {
            return;
        }
        String[] permission = saCheckPermission.value();
        if (permission == null || permission.length == 0) {
            return;
        }
        if (SaMode.OR == saCheckPermission.mode()) {
            StpUtil.checkPermissionOr(permission);
        } else {
            StpUtil.checkPermissionAnd(permission);
        }
    }

}
