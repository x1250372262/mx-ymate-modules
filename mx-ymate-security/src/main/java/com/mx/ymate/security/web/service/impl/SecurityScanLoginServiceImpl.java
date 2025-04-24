package com.mx.ymate.security.web.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.adapter.AbstractScanLoginCacheStoreAdapter;
import com.mx.ymate.security.base.bean.ScanQrcode;
import com.mx.ymate.security.base.enums.ScanQrcodeEnum;
import com.mx.ymate.security.base.model.SecurityUser;
import com.mx.ymate.security.handler.ILoginHandler;
import com.mx.ymate.security.web.dao.ISecurityUserDao;
import com.mx.ymate.security.web.service.ISecurityLoginService;
import com.mx.ymate.security.web.service.ISecurityScanLoginService;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.webmvc.context.WebContext;

import java.util.Map;
import java.util.Objects;

import static com.mx.ymate.security.base.code.SecurityCode.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Bean
public class SecurityScanLoginServiceImpl implements ISecurityScanLoginService {

    private final ISecurityConfig config = Security.get().getConfig();

    @Inject
    private ISecurityLoginService iSecurityLoginService;
    @Inject
    private ISecurityUserDao iSecurityUserDao;


    @Override
    public MxResult generateQrcode() throws Exception {
        int qrCodeExpires = config.scanLoginQrCodeExpire();
        if (qrCodeExpires <= 0) {
            return MxResult.create(SECURITY_LOGIN_SCAN_QRCODE_EXPIRES_ERROR);
        }
        AbstractScanLoginCacheStoreAdapter scanLoginCacheStoreAdapter = config.scanLoginCacheStoreAdapter();
        //生成二维码key
        String loginKey = scanLoginCacheStoreAdapter.createLoginKey();
        //保存redis 设置过期时间 可以配置文件配置
        long expire = System.currentTimeMillis() + DateTimeUtils.SECOND * qrCodeExpires;
        ScanQrcode scanQrcode = new ScanQrcode(loginKey, expire, ScanQrcodeEnum.WAIT);
        String qrcodeKey = scanLoginCacheStoreAdapter.createStoreKey(loginKey);
        scanLoginCacheStoreAdapter.cacheScanQrcode(qrcodeKey, scanQrcode);

        return MxResult.okData(scanQrcode).attr("qrcode","");
    }

    @Override
    public MxResult checkQrcode(String loginKey) throws Exception {
        AbstractScanLoginCacheStoreAdapter scanLoginCacheStoreAdapter = config.scanLoginCacheStoreAdapter();
        String qrcodeKey = scanLoginCacheStoreAdapter.createStoreKey(loginKey);
        ScanQrcode scanQrcode = scanLoginCacheStoreAdapter.getScanQrcode(qrcodeKey);
        if (scanQrcode == null) {
            scanLoginCacheStoreAdapter.deleteScanQrcode(qrcodeKey);
            return MxResult.create(SECURITY_LOGIN_SCAN_QRCODE_TIMEOUT);
        }
        //过期
        if (scanQrcode.getExpire() <= System.currentTimeMillis()) {
            scanLoginCacheStoreAdapter.deleteScanQrcode(qrcodeKey);
            return MxResult.create(SECURITY_LOGIN_SCAN_QRCODE_TIMEOUT);
        }
        return MxResult.okData(scanQrcode.getScanQrcodeEnum().name())
                .attr("loginResult",scanQrcode.getLoginResult());
    }

    @Override
    public MxResult scan(String loginKey) throws Exception {
        AbstractScanLoginCacheStoreAdapter scanLoginCacheStoreAdapter = config.scanLoginCacheStoreAdapter();
        String qrcodeKey = scanLoginCacheStoreAdapter.createStoreKey(loginKey);
        ScanQrcode scanQrcode = scanLoginCacheStoreAdapter.getScanQrcode(qrcodeKey);
        if (scanQrcode == null) {
            scanLoginCacheStoreAdapter.deleteScanQrcode(qrcodeKey);
            return MxResult.create(SECURITY_LOGIN_SCAN_QRCODE_TIMEOUT);
        }
        //过期
        if (scanQrcode.getExpire() <= System.currentTimeMillis()) {
            scanLoginCacheStoreAdapter.deleteScanQrcode(qrcodeKey);
            return MxResult.create(SECURITY_LOGIN_SCAN_QRCODE_TIMEOUT);
        }
        //已经扫过码了
        if (ScanQrcodeEnum.WAIT != scanQrcode.getScanQrcodeEnum()) {
            return MxResult.create(SECURITY_LOGIN_SCAN_QRCODE_REPEAT);
        }
        scanQrcode.setScanQrcodeEnum(ScanQrcodeEnum.SCAN);
        scanLoginCacheStoreAdapter.cacheScanQrcode(qrcodeKey, scanQrcode);
        return MxResult.ok();
    }

    @Override
    public MxResult login(String loginId, String loginKey) throws Exception {
        AbstractScanLoginCacheStoreAdapter scanLoginCacheStoreAdapter = config.scanLoginCacheStoreAdapter();
        String qrcodeKey = scanLoginCacheStoreAdapter.createStoreKey(loginKey);
        ScanQrcode scanQrcode = scanLoginCacheStoreAdapter.getScanQrcode(qrcodeKey);
        if (scanQrcode == null) {
            scanLoginCacheStoreAdapter.deleteScanQrcode(qrcodeKey);
            return MxResult.create(SECURITY_LOGIN_SCAN_QRCODE_TIMEOUT);
        }
        //过期
        if (scanQrcode.getExpire() <= System.currentTimeMillis()) {
            scanLoginCacheStoreAdapter.deleteScanQrcode(qrcodeKey);
            return MxResult.create(SECURITY_LOGIN_SCAN_QRCODE_TIMEOUT);
        }
        //没有扫码
        if (ScanQrcodeEnum.SCAN != scanQrcode.getScanQrcodeEnum()) {
            return MxResult.create(SECURITY_LOGIN_SCAN_QRCODE_NEED_SCAN);
        }
        Map<String, String> params = ServletUtil.getParamMap(WebContext.getRequest());
        ILoginHandler loginHandler = config.loginHandlerClass();
        MxResult r = loginHandler.loginBefore(params);
        if(r == null){
            return Security.error();
        }
        if (!r.isSuccess()) {
            return r;
        }
        SecurityUser securityUser = r.attr("securityUser");
        if (securityUser == null) {
            securityUser = iSecurityUserDao.findById(loginId);
        }
        if (securityUser == null) {
            return MxResult.create(SECURITY_LOGIN_USER_NOT_EXIST);
        }
        if (Objects.equals(Constants.BOOL_TRUE, securityUser.getDisableStatus())) {
            return MxResult.create(SECURITY_LOGIN_USER_DISABLE);
        }
        //锁住了
        if (Objects.equals(Constants.BOOL_TRUE, securityUser.getLoginLockStatus()) && System.currentTimeMillis() < securityUser.getLoginLockEndTime()) {
            String msg = SECURITY_LOGIN_USER_LOCKED.msg();
            msg = StrUtil.format(msg, DateTimeUtils.formatTime(securityUser.getLoginLockEndTime(), DateTimeUtils.YYYY_MM_DD_HH_MM_SS));
            return MxResult.create(SECURITY_LOGIN_USER_LOCKED.code()).msg(msg);
        } else if (Objects.equals(Constants.BOOL_TRUE, securityUser.getLoginLockStatus()) && System.currentTimeMillis() > securityUser.getLoginLockEndTime()) {
            securityUser.setLoginLockStatus(Constants.BOOL_FALSE);
            securityUser.setLoginErrorCount(0);
            securityUser.setLoginLockStartTime(0L);
            securityUser.setLoginLockEndTime(0L);
        }
        MxResult mxResult = iSecurityLoginService.scanLogin(securityUser);
        if (mxResult.isSuccess()) {
            scanQrcode.setScanQrcodeEnum(ScanQrcodeEnum.LOGIN_SUCCESS);
            scanQrcode.setLoginResult(mxResult.data());
        } else {
            scanQrcode.setScanQrcodeEnum(ScanQrcodeEnum.LOGIN_FAIL);
        }
        scanLoginCacheStoreAdapter.cacheScanQrcode(qrcodeKey,scanQrcode);
        return mxResult;
    }

    @Override
    public MxResult cancelLogin(String loginKey) throws Exception {
        AbstractScanLoginCacheStoreAdapter scanLoginCacheStoreAdapter = config.scanLoginCacheStoreAdapter();
        String qrcodeKey = scanLoginCacheStoreAdapter.createStoreKey(loginKey);
        ScanQrcode scanQrcode = scanLoginCacheStoreAdapter.getScanQrcode(qrcodeKey);
        if (scanQrcode != null) {
            scanLoginCacheStoreAdapter.deleteScanQrcode(qrcodeKey);
        }
        return MxResult.ok();
    }

}
