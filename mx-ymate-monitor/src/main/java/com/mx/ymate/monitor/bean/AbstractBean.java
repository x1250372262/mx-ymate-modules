package com.mx.ymate.monitor.bean;

import java.io.Serializable;

public abstract class AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String serverId;

    private String projectId;

    private Long createTime;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "AbstractBean{" +
                "serverId='" + serverId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
