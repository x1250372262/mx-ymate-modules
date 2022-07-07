package com.mx.ymate.security.base.vo;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description: 菜单角色信息
 */
public class SecurityMenuRoleVO implements Serializable {

    private String id;

    private String name;

    private Long createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }


    @Override
    public String toString() {
        return "SecurityMenuRoleVO{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", createTime=" + createTime + '}';
    }
}
