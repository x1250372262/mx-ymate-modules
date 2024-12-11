package com.mx.ymate.security.web.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.SaUtil;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.annotation.OperationLog;
import com.mx.ymate.security.base.bean.LoginResult;
import com.mx.ymate.security.base.bean.LoginUser;
import com.mx.ymate.security.base.bean.SecurityLoginInfoBean;
import com.mx.ymate.security.base.enums.OperationType;
import com.mx.ymate.security.base.model.SecurityUser;
import com.mx.ymate.security.base.vo.SecurityLoginVO;
import com.mx.ymate.security.base.vo.SecurityMenuNavVO;
import com.mx.ymate.security.base.vo.SecurityUserVO;
import com.mx.ymate.security.handler.ILoginHandler;
import com.mx.ymate.security.web.dao.ISecurityUserDao;
import com.mx.ymate.security.web.service.ISecurityLoginService;
import com.mx.ymate.security.web.service.ISecurityMenuService;
import com.mx.ymate.security.web.service.ISecurityUserRoleService;
import com.mx.ymate.security.web.service.ISecurityUserService;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.mx.ymate.security.base.code.SecurityCode.*;

/**
 * @Author: mengxiang.
 * @create: 2021-09-03 15:21
 * @Description:
 */
@Bean
public class SecurityLoginServiceImpl implements ISecurityLoginService {

    @Inject
    private ISecurityUserDao iSecurityUserDao;
    @Inject
    private ISecurityUserRoleService iSecurityUserRoleService;
    @Inject
    private ISecurityMenuService iSecurityMenuService;
    @Inject
    private ISecurityUserService iSecurityUserService;
    private final ISecurityConfig config = Security.get().getConfig();

    @Override
    @OperationLog(operationType = OperationType.LOGIN, title = "管理员登录")
    public MxResult login(String userName, String password) throws Exception {
        Map<String, String> params = ServletUtil.getParamMap(WebContext.getRequest());
        ILoginHandler loginHandler = config.loginHandlerClass();
        MxResult r = loginHandler.loginBefore(params);
        if (Security.error(r)) {
            return r;
        }
        SecurityUser securityUser = r.attr("securityUser");
        if (securityUser == null) {
            securityUser = iSecurityUserDao.findByUserNameAndClient(userName, config.client());
        }
        if (securityUser == null) {
            return MxResult.create(SECURITY_LOGIN_USER_NAME_NOT_EXIST);
        }
        if (Objects.equals(Constants.BOOL_TRUE, securityUser.getDisableStatus())) {
            return MxResult.create(SECURITY_LOGIN_USER_DISABLE);
        }
        //锁住了
        if (Objects.equals(Constants.BOOL_TRUE, securityUser.getLoginLockStatus()) && DateTimeUtils.currentTimeMillis() < securityUser.getLoginLockEndTime()) {
            String msg = SECURITY_LOGIN_USER_LOCKED.msg();
            msg = StrUtil.format(msg, DateTimeUtils.formatTime(securityUser.getLoginLockEndTime(), DateTimeUtils.YYYY_MM_DD_HH_MM_SS));
            return MxResult.create(SECURITY_LOGIN_USER_LOCKED.code()).msg(msg);
        } else if (Objects.equals(Constants.BOOL_TRUE, securityUser.getLoginLockStatus()) && DateTimeUtils.currentTimeMillis() > securityUser.getLoginLockEndTime()) {
            securityUser.setLoginLockStatus(Constants.BOOL_FALSE);
            securityUser.setLoginErrorCount(0);
            securityUser.setLoginLockStartTime(0L);
            securityUser.setLoginLockEndTime(0L);
        }
        password = DigestUtils.md5Hex(Base64.encodeBase64((password + securityUser.getSalt()).getBytes(StandardCharsets.UTF_8)));
        if (!password.equals(securityUser.getPassword())) {
            r = loginHandler.loginFail(params, securityUser);
            if (Security.error(r)) {
                return r;
            }
            //次数+1 到数之后直接冻结
            securityUser.setLoginErrorCount(securityUser.getLoginErrorCount() + 1);
            //冻结了 配置设置了错误次数 并且实际错误次数大于等于设置的错误次数
            if (config.errorCount() > 0 && securityUser.getLoginErrorCount() >= config.errorCount()) {
                securityUser.setLoginLockStatus(Constants.BOOL_TRUE);
                securityUser.setLoginLockStartTime(DateTimeUtils.currentTimeMillis());
                securityUser.setLoginLockEndTime(DateTimeUtils.currentTimeMillis() + DateTimeUtils.DAY);
            }
            iSecurityUserDao.update(securityUser, SecurityUser.FIELDS.LOGIN_ERROR_COUNT, SecurityUser.FIELDS.LOGIN_LOCK_STATUS,
                    SecurityUser.FIELDS.LOGIN_LOCK_START_TIME, SecurityUser.FIELDS.LOGIN_LOCK_END_TIME);
            return MxResult.create(SECURITY_LOGIN_USER_NAME_OR_PASSWORD_ERROR);
        }
        r = loginHandler.loginSuccess(params, securityUser);
        if (Security.error(r)) {
            return r;
        }
        //重置时间和次数
        securityUser.setLoginLockStatus(Constants.BOOL_FALSE);
        securityUser.setLoginErrorCount(0);
        securityUser.setLoginLockStartTime(0L);
        securityUser.setLoginLockEndTime(0L);
        securityUser.setLoginTime(DateTimeUtils.currentTimeMillis());
        securityUser.setLoginIp(ServletUtil.getClientIP(WebContext.getRequest()));
        iSecurityUserDao.update(securityUser, SecurityUser.FIELDS.LOGIN_ERROR_COUNT, SecurityUser.FIELDS.LOGIN_LOCK_STATUS,
                SecurityUser.FIELDS.LOGIN_LOCK_START_TIME, SecurityUser.FIELDS.LOGIN_LOCK_END_TIME, SecurityUser.FIELDS.LOGIN_IP, SecurityUser.FIELDS.LOGIN_TIME);
        StpUtil.login(securityUser.getId(),config.device());
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        LoginResult loginResult = BeanUtil.copy(saTokenInfo, LoginResult::new);
        loginResult.setAttrs(r.attrs());
        doLogin(loginResult, securityUser);
        //登录完成的事件  不处理成功失败
        loginHandler.loginComplete(params, securityUser, saTokenInfo);
        return MxResult.ok().data(loginResult);
    }

    @Override
    @OperationLog(operationType = OperationType.LOGIN, title = "管理员扫码登录")
    public MxResult scanLogin(SecurityUser securityUser) throws Exception {
        //重置时间和次数
        securityUser.setLoginLockStatus(Constants.BOOL_FALSE);
        securityUser.setLoginErrorCount(0);
        securityUser.setLoginLockStartTime(0L);
        securityUser.setLoginLockEndTime(0L);
        securityUser.setLoginTime(DateTimeUtils.currentTimeMillis());
        securityUser.setLoginIp(ServletUtil.getClientIP(WebContext.getRequest()));
        iSecurityUserDao.update(securityUser, SecurityUser.FIELDS.LOGIN_ERROR_COUNT, SecurityUser.FIELDS.LOGIN_LOCK_STATUS,
                SecurityUser.FIELDS.LOGIN_LOCK_START_TIME, SecurityUser.FIELDS.LOGIN_LOCK_END_TIME, SecurityUser.FIELDS.LOGIN_IP, SecurityUser.FIELDS.LOGIN_TIME);
        StpUtil.login(securityUser.getId(),config.device());
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        LoginResult loginResult = BeanUtil.copy(saTokenInfo, LoginResult::new);
        doLogin(loginResult, securityUser);
        return MxResult.ok().data(loginResult);
    }

    private void doLogin(LoginResult loginResult, SecurityUser securityUser) throws Exception {
        //设置用户到缓存
        List<String> permissionList = cacheUser(securityUser);
        //处理菜单数据
        List<SecurityMenuNavVO> navList = iSecurityMenuService.navList(securityUser.getId(), permissionList, Objects.equals(Constants.BOOL_TRUE, securityUser.getFounder()));
        loginResult.setNavList(navList);
        //处理用户数据
        SecurityUserVO securityUserVO = iSecurityUserService.detailInfo(securityUser.getId());
        loginResult.setUserInfo(securityUserVO);
        //处理权限数据
        loginResult.setPermissionList(permissionList);
    }

    @Override
    public MxResult unlock(String id, String password) throws Exception {
        SecurityUser securityUser = iSecurityUserDao.findById(id, SecurityUser.FIELDS.PASSWORD, SecurityUser.FIELDS.SALT);
        password = DigestUtils.md5Hex(Base64.encodeBase64((password + securityUser.getSalt()).getBytes(StandardCharsets.UTF_8)));
        if (password.equals(securityUser.getPassword())) {
            SaUtil.unlock(id);
            return MxResult.ok();
        }
        return MxResult.fail();
    }

    @Override
    public MxResult lock(String id) throws Exception {
        SaUtil.lock(id);
        return MxResult.ok();
    }

    @Override
    public MxResult checkLock(String id) throws Exception {
        boolean isLock = SaUtil.checkLock(id);
        if (isLock) {
            return MxResult.ok();
        }
        return MxResult.fail();
    }

    @Override
    @OperationLog(operationType = OperationType.LOGIN, title = "管理员退出")
    public MxResult logout() throws Exception {
        Map<String, String> params = ServletUtil.getParamMap(WebContext.getRequest());
        ILoginHandler loginHandler = config.loginHandlerClass();
        String loginId = SaUtil.loginId();
        MxResult r = loginHandler.logoutBefore(params,loginId);
        if (Security.error(r)) {
            return r;
        }
        SaUtil.clearUser();
        SaUtil.clearPermission();
        StpUtil.logout(loginId,config.device());
        r = loginHandler.logoutAfter(params,loginId);
        if (Security.error(r)) {
            return r;
        }
        return MxResult.ok();
    }

    public List<String> cacheUser(SecurityUser securityUser) throws Exception {
        LoginUser loginUser = BeanUtil.copy(securityUser, LoginUser::new);
        //设置用户信息到redis
        SaUtil.cacheUser(loginUser);
        //设置权限到redis
        //先删除redis的数据
        List<String> permissionList = iSecurityUserRoleService.securityUserPermissionList(securityUser.getId());
        SaUtil.cachePermission(loginUser.getId(), permissionList);
        return permissionList;
    }


    @Override
    public SecurityLoginVO info() throws Exception {
        SecurityUser securityUser = iSecurityUserDao.findById(SaUtil.loginId());
        SecurityLoginVO securityUserVO = BeanUtil.copy(securityUser, SecurityLoginVO::new);
        return BeanUtil.copy(securityUserVO, SecurityLoginVO::new);
    }

    @Override
    @OperationLog(operationType = OperationType.UPDATE, title = "修改管理员信息")
    public MxResult update(SecurityLoginInfoBean securityLoginInfoBean) throws Exception {
        SecurityUser securityUser = iSecurityUserDao.findById(SaUtil.loginId());
        if (securityUser == null) {
            return MxResult.noData();
        }
        securityUser = BeanUtil.duplicate(securityLoginInfoBean, securityUser);
        securityUser.setLastModifyUser(SaUtil.loginId());
        securityUser.setLastModifyTime(DateTimeUtils.currentTimeMillis());
        securityUser = iSecurityUserDao.update(securityUser, SecurityUser.FIELDS.REAL_NAME, SecurityUser.FIELDS.PHOTO_URI,
                SecurityUser.FIELDS.MOBILE, SecurityUser.FIELDS.GENDER, SecurityUser.FIELDS.LAST_MODIFY_USER, SecurityUser.FIELDS.LAST_MODIFY_TIME);
        //处理用户数据
        SecurityUserVO securityUserVO = iSecurityUserService.detailInfo(securityUser.getId());
        return MxResult.result(securityUser).attr("userInfo", securityUserVO);
    }

    @Override
    @OperationLog(operationType = OperationType.UPDATE, title = "修改管理员密码")
    public MxResult password(String oldPassword, String newPassword, String rePassword) throws Exception {
        if (!newPassword.equals(rePassword)) {
            return MxResult.create(SECURITY_USER_PASSWORD_NOT_SAME);
        }
        SecurityUser securityUser = iSecurityUserDao.findById(SaUtil.loginId());
        if (securityUser == null) {
            return MxResult.noData();
        }
        oldPassword = DigestUtils.md5Hex(Base64.encodeBase64((oldPassword + securityUser.getSalt()).getBytes(StandardCharsets.UTF_8)));
        if (!oldPassword.equals(securityUser.getPassword())) {
            return MxResult.create(SECURITY_USER_OLD_PASSWORD_ERROR);
        }
        newPassword = DigestUtils.md5Hex(Base64.encodeBase64((newPassword + securityUser.getSalt()).getBytes(StandardCharsets.UTF_8)));
        if (newPassword.equals(securityUser.getPassword())) {
            return MxResult.create(SECURITY_USER_NEW_PASSWORD_NOT_SAME_OLD_PASSWORD);
        }
        securityUser.setPassword(newPassword);
        securityUser.setLastModifyTime(DateTimeUtils.currentTimeMillis());
        securityUser.setLastModifyUser(securityUser.getId());
        securityUser = iSecurityUserDao.update(securityUser, SecurityUser.FIELDS.PASSWORD, SecurityUser.FIELDS.LAST_MODIFY_USER, SecurityUser.FIELDS.LAST_MODIFY_TIME);
        return MxResult.result(securityUser);
    }
}
