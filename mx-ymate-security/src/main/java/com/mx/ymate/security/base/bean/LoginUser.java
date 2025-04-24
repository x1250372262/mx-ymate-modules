package com.mx.ymate.security.base.bean;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 登录结果
 */
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String resourceId;

    private String client;

    private String userName;

    private String realName;

    private String photoUri;

    private String mobile;

    private Integer gender;

    private String createUser;

    private Long createTime;

    private Long lastModifyTime;

    private String lastModifyUser;

    private Integer disableStatus;

    private Integer founder;

    private Integer loginErrorCount;

    private Integer loginLockStatus;

    private Long loginLockStartTime;

    private Long loginLockEndTime;

    private Long loginTime;

    private String loginIp;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getLastModifyUser() {
        return lastModifyUser;
    }

    public void setLastModifyUser(String lastModifyUser) {
        this.lastModifyUser = lastModifyUser;
    }

    public Integer getDisableStatus() {
        return disableStatus;
    }

    public void setDisableStatus(Integer disableStatus) {
        this.disableStatus = disableStatus;
    }

    public Integer getFounder() {
        return founder;
    }

    public void setFounder(Integer founder) {
        this.founder = founder;
    }

    public Integer getLoginErrorCount() {
        return loginErrorCount;
    }

    public void setLoginErrorCount(Integer loginErrorCount) {
        this.loginErrorCount = loginErrorCount;
    }

    public Integer getLoginLockStatus() {
        return loginLockStatus;
    }

    public void setLoginLockStatus(Integer loginLockStatus) {
        this.loginLockStatus = loginLockStatus;
    }

    public Long getLoginLockStartTime() {
        return loginLockStartTime;
    }

    public void setLoginLockStartTime(Long loginLockStartTime) {
        this.loginLockStartTime = loginLockStartTime;
    }

    public Long getLoginLockEndTime() {
        return loginLockEndTime;
    }

    public void setLoginLockEndTime(Long loginLockEndTime) {
        this.loginLockEndTime = loginLockEndTime;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }
}
