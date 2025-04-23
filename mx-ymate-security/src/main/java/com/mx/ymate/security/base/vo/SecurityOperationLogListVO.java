package com.mx.ymate.security.base.vo;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @create: 2021-09-24 16:00
 * @Description:
 */
public class SecurityOperationLogListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String title;

    private String typeName;

    private String returnCode;

    private String userName;

    private Long createTime;

    private String requestUrl;

    private String ip;

    private String location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    @Override
    public String toString() {
        return "SecurityOperationLogListVO{" + "id='" + id + '\'' + ", title='" + title + '\'' + ", typeName='" + typeName + '\'' + ", userName='" + userName + '\'' + ", createTime=" + createTime + ", requestUrl='" + requestUrl + '\'' + ", ip='" + ip + '\'' + ", location='" + location + '\'' + '}';
    }
}
