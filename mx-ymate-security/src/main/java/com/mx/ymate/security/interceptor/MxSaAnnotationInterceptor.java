package com.mx.ymate.security.interceptor;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.strategy.SaStrategy;
import com.mx.ymate.dev.code.Code;
import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.dev.result.MxResult;
import com.mx.ymate.satoken.annotation.NoLogin;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.SaUtil;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.code.SecurityCode;
import com.mx.ymate.security.base.model.SecurityUser;
import com.mx.ymate.security.dao.ISecurityUserDao;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.core.beans.intercept.AbstractInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.core.beans.intercept.InterceptException;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 注解式鉴权 - 拦截器
 *
 * @author kong
 */
public class MxSaAnnotationInterceptor extends AbstractInterceptor {

    private final ISecurityConfig securityConfig = Security.get().getConfig();

    private final ISecurityUserDao iSecurityUserDao = YMP.get().getBeanFactory().getBean(ISecurityUserDao.class);

    private final SaUtil saUtil = YMP.get().getBeanFactory().getBean(SaUtil.class);

    /**
     * 构建： 注解式鉴权 - 拦截器
     */
    public MxSaAnnotationInterceptor() {
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
            SecurityUser securityUser = iSecurityUserDao.findById(saUtil.loginId());
            if (securityUser == null) {
                throw new NotLoginException(Code.NOT_LOGIN.code(), null, null);
            }
            if (Objects.equals(Constants.BOOL_TRUE, securityUser.getDisableStatus())) {
                throw new NotLoginException(SecurityCode.SECURITY_LOGIN_USER_DISABLE.msg(), null, null);
            }
            securityConfig.loginHandlerClass().checkLoginCustom(securityUser);

        } catch (NotLoginException notLoginException) {
            return MxResult.create(Code.NOT_LOGIN).toMxJsonView();
        }catch (NotPermissionException | NotRoleException notPermissionException) {
            return MxResult.create(Code.NOT_PERMISSION).toMxJsonView();
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
