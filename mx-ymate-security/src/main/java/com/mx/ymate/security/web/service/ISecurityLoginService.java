package com.mx.ymate.security.web.service;


import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.security.base.bean.SecurityLoginInfoBean;
import com.mx.ymate.security.base.vo.SecurityLoginVO;

/**
 * @Author: mengxiang.
 * @create: 2021-09-03 15:20
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
