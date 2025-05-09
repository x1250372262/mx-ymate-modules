package com.mx.ymate.security.web.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.support.page.PageBean;
import com.mx.ymate.dev.support.page.Pages;
import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.SaUtil;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.annotation.OperationLog;
import com.mx.ymate.security.base.bean.SecurityUserBean;
import com.mx.ymate.security.base.enums.OperationType;
import com.mx.ymate.security.base.enums.ResourceType;
import com.mx.ymate.security.base.model.SecurityUser;
import com.mx.ymate.security.base.model.SecurityUserRole;
import com.mx.ymate.security.base.vo.SecurityUserListVO;
import com.mx.ymate.security.base.vo.SecurityUserRoleVO;
import com.mx.ymate.security.base.vo.SecurityUserVO;
import com.mx.ymate.security.handler.IResourceHandler;
import com.mx.ymate.security.handler.IUserHandler;
import com.mx.ymate.security.web.dao.ISecurityUserDao;
import com.mx.ymate.security.web.dao.ISecurityUserRoleDao;
import com.mx.ymate.security.web.service.ISecurityUserService;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.commons.util.UUIDUtils;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.annotation.Transaction;
import net.ymate.platform.persistence.jdbc.query.Cond;
import net.ymate.platform.persistence.jdbc.query.Where;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.mx.ymate.security.I18nConstant.*;
import static com.mx.ymate.security.Security.DEFAULT_PASSWORD;
import static com.mx.ymate.security.base.code.SecurityCode.SECURITY_USER_ROLE_EXISTS;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Bean
@Transaction
public class SecurityUserServiceImpl implements ISecurityUserService {

    @Inject
    private ISecurityUserDao iSecurityUserDao;
    @Inject
    private ISecurityUserRoleDao iSecurityUserRoleDao;

    private final ISecurityConfig config = Security.get().getConfig();
    private final IUserHandler userHandler = config.userHandlerClass();
    private final IResourceHandler resourceHandler = config.resourceHandlerClass();


    @Override
    public MxResult list(String userName, String realName, Integer disableStatus, PageBean pageBean) throws Exception {
        String resourceId = StringUtils.defaultIfBlank(resourceHandler.buildResourceId(ResourceType.USER, SaUtil.loginId()), config.client());
        IResultSet<SecurityUserListVO> resultData = iSecurityUserDao.findAll(userName, realName, disableStatus, config.client(), resourceId, pageBean.toPage());
        return MxResult.ok().data(Pages.create(resultData));
    }

    @Override
    public MxResult select(Integer disableStatus) throws Exception {
        Cond cond = Cond.create().eqOne()
                .exprNotEmpty(disableStatus, c -> c.and().eqWrap(SecurityUser.FIELDS.DISABLE_STATUS).param(disableStatus));
        IResultSet<SecurityUser> resultSet = SecurityUser.builder().build().find(Where.create(cond));
        return MxResult.ok().data(resultSet.getResultData());
    }

    @Override
    @Transaction
    @OperationLog(operationType = OperationType.CREATE, title = LOG_USER_CREATE_TITLE_MSG, i18nKey = LOG_USER_CREATE_TITLE_I18N_KEY)
    public MxResult create(String password, SecurityUserBean userBean) throws Exception {
        String resourceId = StringUtils.defaultIfBlank(resourceHandler.buildResourceId(ResourceType.USER, SaUtil.loginId()), config.client());
        Map<String, String> params = ServletUtil.getParamMap(WebContext.getRequest());
        MxResult r = userHandler.createBefore(params);
        if (r == null) {
            return Security.error();
        }
        if (!r.isSuccess()) {
            return r;
        }
        SecurityUser securityUser = iSecurityUserDao.findByUserNameAndClientAndResourceId(userBean.getUserName(), config.client(), resourceId);
        if (securityUser != null) {
            return MxResult.sameData("用户名");
        }
        String salt = RandomUtil.randomString(6);
        password = DigestUtils.md5Hex(Base64.encodeBase64((password + salt).getBytes(StandardCharsets.UTF_8)));
        securityUser = BeanUtil.copy(userBean, SecurityUser::new);
        securityUser.setId(UUIDUtils.UUID());
        securityUser.setClient(config.client());
        securityUser.setResourceId(resourceId);
        securityUser.setPassword(password);
        securityUser.setCreateUser(SaUtil.loginId());
        securityUser.setCreateTime(DateTimeUtils.currentTimeMillis());
        securityUser.setLastModifyUser(SaUtil.loginId());
        securityUser.setLastModifyTime(DateTimeUtils.currentTimeMillis());
        securityUser.setSalt(salt);
        r = userHandler.createAfter(params, securityUser);
        if (r == null) {
            return Security.error();
        }
        if (!r.isSuccess()) {
            return r;
        }
        securityUser = iSecurityUserDao.create(securityUser);
        return MxResult.result(securityUser);
    }

    @Override
    public MxResult detail(String id) throws Exception {
        return MxResult.ok().data(detailInfo(id));
    }

    @Override
    public SecurityUserVO detailInfo(String id) throws Exception {
        return BeanUtil.copy(iSecurityUserDao.findById(id), SecurityUserVO::new);
    }

    @Override
    @OperationLog(operationType = OperationType.OTHER, title = LOG_USER_UPDATE_STATUS_TITLE_MSG, i18nKey = LOG_USER_UPDATE_STATUS_TITLE_I18N_KEY)
    public MxResult status(String id, Long lastModifyTime, Integer status) throws Exception {
        SecurityUser securityUser = iSecurityUserDao.findById(id);
        if (securityUser == null) {
            return MxResult.noData();
        }
        if (MxResult.checkVersion(securityUser.getLastModifyTime(), lastModifyTime)) {
            return MxResult.noVersion();
        }
        securityUser.setLastModifyUser(SaUtil.loginId());
        securityUser.setLastModifyTime(DateTimeUtils.currentTimeMillis());
        securityUser.setDisableStatus(status);
        securityUser = iSecurityUserDao.update(securityUser, SecurityUser.FIELDS.LAST_MODIFY_USER, SecurityUser.FIELDS.LAST_MODIFY_TIME, SecurityUser.FIELDS.DISABLE_STATUS);
        return MxResult.result(securityUser);
    }

    @Override
    @OperationLog(operationType = OperationType.OTHER, title = LOG_USER_UNLOCK_TITLE_MSG, i18nKey = LOG_USER_UNLOCK_TITLE_I18N_KEY)
    public MxResult unlock(String id, Long lastModifyTime) throws Exception {
        SecurityUser securityUser = iSecurityUserDao.findById(id);
        if (securityUser == null) {
            return MxResult.noData();
        }
        if (MxResult.checkVersion(securityUser.getLastModifyTime(), lastModifyTime)) {
            return MxResult.noVersion();
        }
        securityUser.setLastModifyUser(SaUtil.loginId());
        securityUser.setLastModifyTime(DateTimeUtils.currentTimeMillis());
        securityUser.setLoginErrorCount(0);
        securityUser.setLoginLockStatus(Constants.BOOL_FALSE);
        securityUser.setLoginLockStartTime(0L);
        securityUser.setLoginLockEndTime(0L);
        securityUser = iSecurityUserDao.update(securityUser, SecurityUser.FIELDS.LAST_MODIFY_USER, SecurityUser.FIELDS.LAST_MODIFY_TIME,
                SecurityUser.FIELDS.LOGIN_LOCK_END_TIME, SecurityUser.FIELDS.LOGIN_LOCK_STATUS, SecurityUser.FIELDS.LOGIN_LOCK_START_TIME, SecurityUser.FIELDS.LOGIN_LOCK_END_TIME);
        return MxResult.result(securityUser);
    }

    @Override
    @OperationLog(operationType = OperationType.OTHER, title = LOG_USER_RESET_PASSWORD_TITLE_MSG, i18nKey = LOG_USER_RESET_PASSWORD_TITLE_I18N_KEY)
    public MxResult resetPassword(String id, Long lastModifyTime) throws Exception {
        SecurityUser securityUser = iSecurityUserDao.findById(id);
        if (securityUser == null) {
            return MxResult.noData();
        }
        if (MxResult.checkVersion(securityUser.getLastModifyTime(), lastModifyTime)) {
            return MxResult.noVersion();
        }
        securityUser.setLastModifyUser(SaUtil.loginId());
        securityUser.setLastModifyTime(DateTimeUtils.currentTimeMillis());
        String password = DigestUtils.md5Hex(DEFAULT_PASSWORD.getBytes(StandardCharsets.UTF_8));
        try {
            password = DigestUtils.md5Hex(Base64.encodeBase64((password + securityUser.getSalt()).getBytes(Constants.DEFAULT_CHARSET)));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        securityUser.setPassword(password);
        securityUser.setLastModifyUser(SaUtil.loginId());
        securityUser.setLastModifyTime(DateTimeUtils.currentTimeMillis());
        securityUser = iSecurityUserDao.update(securityUser, SecurityUser.FIELDS.LAST_MODIFY_USER, SecurityUser.FIELDS.LAST_MODIFY_TIME, SecurityUser.FIELDS.PASSWORD);
        return MxResult.result(securityUser);
    }

    @Override
    public MxResult roleList(String userId, PageBean pageBean) throws Exception {
        IResultSet<SecurityUserRoleVO> resultData = iSecurityUserRoleDao.roleList(userId, config.client(), pageBean.toPage());
        return MxResult.ok().data(Pages.create(resultData));
    }

    @Override
    @OperationLog(operationType = OperationType.CREATE, title = LOG_USER_ROLE_CREATE_TITLE_MSG, i18nKey = LOG_USER_ROLE_CREATE_TITLE_I18N_KEY)
    public MxResult roleCreate(String userId, String roleId) throws Exception {
        SecurityUserRole securityUserRole = iSecurityUserRoleDao.findByUserIdAndRoleIdAndClient(userId, roleId, config.client());
        if (securityUserRole != null) {
            return MxResult.create(SECURITY_USER_ROLE_EXISTS);
        }
        securityUserRole = SecurityUserRole.builder()
                .id(UUIDUtils.UUID())
                .userId(userId)
                .roleId(roleId)
                .client(config.client())
                .createUser(SaUtil.loginId())
                .createTime(DateTimeUtils.currentTimeMillis())
                .lastModifyUser(SaUtil.loginId())
                .lastModifyTime(DateTimeUtils.currentTimeMillis())
                .build();
        securityUserRole = iSecurityUserRoleDao.create(securityUserRole);
        return MxResult.result(securityUserRole);
    }

    @Override
    @OperationLog(operationType = OperationType.DELETE, title = LOG_USER_ROLE_DELETE_TITLE_MSG, i18nKey = LOG_USER_ROLE_DELETE_TITLE_I18N_KEY)
    public MxResult roleDelete(String[] ids) throws Exception {
        return MxResult.result(iSecurityUserRoleDao.deleteByIds(ids));
    }

}
