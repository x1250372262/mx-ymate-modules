package com.mx.ymate.dev.support.jdbc;

public abstract class AbstractCommonVO extends AbstractBaseVO {

  private Long createTime;

  private Long lastModifyTime;

  private String createUser;

  private String lastModifyUser;


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
                "createTime=" + createTime +
                ", lastModifyTime=" + lastModifyTime +
                ", createUser='" + createUser + '\'' +
                ", lastModifyUser='" + lastModifyUser + '\'' +
                "} " + super.toString();
    }
}
