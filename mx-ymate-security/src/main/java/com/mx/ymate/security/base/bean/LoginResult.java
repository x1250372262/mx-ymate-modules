package com.mx.ymate.security.base.bean;

import com.mx.ymate.security.base.vo.SecurityMenuNavVO;
import com.mx.ymate.security.base.vo.SecurityUserVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description: 登录结果
 */
public class LoginResult {

    public String tokenName;

    public String tokenValue;

    public Boolean isLogin;

    public Object loginId;

    public String loginType;

    public long tokenTimeout;

    public long sessionTimeout;

    public long tokenSessionTimeout;

    public long tokenActivityTimeout;

    public String loginDevice;

    public String tag;

    private Map<String, Object> attrs = new HashMap<>();

    private List<SecurityMenuNavVO> navList;

    private SecurityUserVO userInfo;

    /**
     * @return token名称
     */
    public String getTokenName() {
        return tokenName;
    }

    /**
     * @param tokenName token名称
     */
    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    /**
     * @return token值
     */
    public String getTokenValue() {
        return tokenValue;
    }

    /**
     * @param tokenValue token值
     */
    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    /**
     * @return 此token是否已经登录
     */
    public Boolean getIsLogin() {
        return isLogin;
    }

    /**
     * @param isLogin 此token是否已经登录
     */
    public void setIsLogin(Boolean isLogin) {
        this.isLogin = isLogin;
    }

    /**
     * @return 此token对应的LoginId，未登录时为null
     */
    public Object getLoginId() {
        return loginId;
    }

    /**
     * @param loginId 此token对应的LoginId，未登录时为null
     */
    public void setLoginId(Object loginId) {
        this.loginId = loginId;
    }

    /**
     * @return 账号类型
     */
    public String getLoginType() {
        return loginType;
    }

    /**
     * @param loginType 账号类型
     */
    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    /**
     * @return token剩余有效期 (单位: 秒)
     */
    public long getTokenTimeout() {
        return tokenTimeout;
    }

    /**
     * @param tokenTimeout token剩余有效期 (单位: 秒)
     */
    public void setTokenTimeout(long tokenTimeout) {
        this.tokenTimeout = tokenTimeout;
    }

    /**
     * @return User-Session剩余有效时间 (单位: 秒)
     */
    public long getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * @param sessionTimeout User-Session剩余有效时间 (单位: 秒)
     */
    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    /**
     * @return Token-Session剩余有效时间 (单位: 秒)
     */
    public long getTokenSessionTimeout() {
        return tokenSessionTimeout;
    }

    /**
     * @param tokenSessionTimeout Token-Session剩余有效时间 (单位: 秒)
     */
    public void setTokenSessionTimeout(long tokenSessionTimeout) {
        this.tokenSessionTimeout = tokenSessionTimeout;
    }

    /**
     * @return token剩余无操作有效时间 (单位: 秒)
     */
    public long getTokenActivityTimeout() {
        return tokenActivityTimeout;
    }

    /**
     * @param tokenActivityTimeout token剩余无操作有效时间 (单位: 秒)
     */
    public void setTokenActivityTimeout(long tokenActivityTimeout) {
        this.tokenActivityTimeout = tokenActivityTimeout;
    }

    /**
     * @return 登录设备标识
     */
    public String getLoginDevice() {
        return loginDevice;
    }

    /**
     * @param loginDevice 登录设备标识
     */
    public void setLoginDevice(String loginDevice) {
        this.loginDevice = loginDevice;
    }

    /**
     * @return 自定义数据
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag 自定义数据
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    public Map<String, Object> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    public List<SecurityMenuNavVO> getNavList() {
        return navList;
    }

    public void setNavList(List<SecurityMenuNavVO> navList) {
        this.navList = navList;
    }

    public SecurityUserVO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(SecurityUserVO userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "tokenName='" + tokenName + '\'' +
                ", tokenValue='" + tokenValue + '\'' +
                ", isLogin=" + isLogin +
                ", loginId=" + loginId +
                ", loginType='" + loginType + '\'' +
                ", tokenTimeout=" + tokenTimeout +
                ", sessionTimeout=" + sessionTimeout +
                ", tokenSessionTimeout=" + tokenSessionTimeout +
                ", tokenActivityTimeout=" + tokenActivityTimeout +
                ", loginDevice='" + loginDevice + '\'' +
                ", tag='" + tag + '\'' +
                ", attrs=" + attrs +
                ", navList=" + navList +
                ", userInfo=" + userInfo +
                '}';
    }
}
