package com.mx.ymate.security.web.service;


import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.security.base.bean.SecurityLoginInfoBean;
import com.mx.ymate.security.base.model.SecurityUser;
import com.mx.ymate.security.base.vo.SecurityLoginVO;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public interface ISecurityLoginService {

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    MxResult login(String userName, String password) throws Exception;

    /**
     * 扫码登录
     * @param securityUser
     * @return
     * @throws Exception
     */
    MxResult scanLogin(SecurityUser securityUser) throws Exception;

    /**
     * 解锁
     * @param id
     * @param password
     * @return
     * @throws Exception
     */
    MxResult unlock(String id, String password) throws Exception;

    /**
     * 锁定
     * @param id
     * @return
     * @throws Exception
     */
    MxResult lock(String id) throws Exception;

    /**
     * 检查是否加锁
     *
     * @param id
     * @return
     * @throws Exception
     */
    MxResult checkLock(String id) throws Exception;


    /**
     * 退出登录
     *
     * @return
     * @throws Exception
     */
    MxResult logout() throws Exception;


    /**
     * 获取登录人信息
     *
     * @return
     * @throws Exception
     */
    SecurityLoginVO info() throws Exception;

    /**
     * 修改登录人信息
     *
     * @param securityLoginInfoBean
     * @return
     * @throws Exception
     */
    MxResult update(SecurityLoginInfoBean securityLoginInfoBean) throws Exception;

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param rePassword
     * @return
     * @throws Exception
     */
    MxResult password(String oldPassword, String newPassword, String rePassword) throws Exception;
}
