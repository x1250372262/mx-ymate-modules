package com.mx.ymate.dev.support.jdbc;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @create: 2023-08-03 16:58
 * @Description: 通用vo
 */
public abstract class AbstractCommonVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private Long createTime;

    private Long lastModifyTime;

    private String createUser;

    private String lastModifyUser;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getLastModifyUser() {
        return lastModifyUser;
    }

    public void setLastModifyUser(String lastModifyUser) {
        this.lastModifyUser = lastModifyUser;
    }

    @Override
    public String toString() {
        return "AbstractCommonVO{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", lastModifyTime=" + lastModifyTime +
                ", createUser='" + createUser + '\'' +
                ", lastModifyUser='" + lastModifyUser + '\'' +
                '}';
    }
}
