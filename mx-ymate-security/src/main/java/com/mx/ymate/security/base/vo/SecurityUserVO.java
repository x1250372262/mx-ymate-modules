package com.mx.ymate.security.base.vo;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class SecurityUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String userName;

    private String photoUri;

    private String realName;

    private Integer gender;

    private Integer founder;

    private Long lastModifyTime;

    private Integer loginErrorCount;

    private Long loginLockStartTime;

    private Long loginLockEndTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getLoginErrorCount() {
        return loginErrorCount;
    }

    public void setLoginErrorCount(Integer loginErrorCount) {
        this.loginErrorCount = loginErrorCount;
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

    public Long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Integer getFounder() {
        return founder;
    }

    public void setFounder(Integer founder) {
        this.founder = founder;
    }

    @Override
    public String toString() {
        return "SecurityUserVO{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", photoUri='" + photoUri + '\'' +
                ", realName='" + realName + '\'' +
                ", gender=" + gender +
                ", founder=" + founder +
                ", lastModifyTime=" + lastModifyTime +
                ", loginErrorCount=" + loginErrorCount +
                ", loginLockStartTime=" + loginLockStartTime +
                ", loginLockEndTime=" + loginLockEndTime +
                '}';
    }
}
