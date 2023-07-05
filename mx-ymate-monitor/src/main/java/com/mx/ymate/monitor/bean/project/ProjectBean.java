package com.mx.ymate.monitor.bean.project;

import com.mx.ymate.monitor.IMonitorConfig;
import com.mx.ymate.monitor.bean.AbstractBean;
import com.mx.ymate.monitor.enums.ProjectStatusEnum;

import java.io.Serializable;

public class ProjectBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public static ProjectBean onlineBean(IMonitorConfig config){
        ProjectBean projectBean = new ProjectBean();
        projectBean.setServerId(config.serverId());
        projectBean.setProjectId(config.projectId());
        projectBean.setCreateTime(System.currentTimeMillis());
        return projectBean;
    }
}
