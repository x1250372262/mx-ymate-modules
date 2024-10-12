package com.mx.ymate.dev.support.monitor.bean;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @Date: 2024-10-11 17:00
 * @Description:
 */
public class DiskBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件系统的挂载点
     */
    private String dirName;

    /**
     * 文件系统名称
     */
    private String sysTypeName;

    /**
     * 文件系统的类型(FAT，NTFS，etx2，ext4等)
     */
    private String typeName;

    /**
     * 总大小
     */
    private Double total;

    /**
     * 剩余大小
     */
    private Double free;

    /**
     * 已经使用量
     */
    private Double used;

    /**
     * 资源的使用率
     */
    private Double usage = 100D;

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getSysTypeName() {
        return sysTypeName;
    }

    public void setSysTypeName(String sysTypeName) {
        this.sysTypeName = sysTypeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getFree() {
        return free;
    }

    public void setFree(Double free) {
        this.free = free;
    }

    public Double getUsed() {
        return used;
    }

    public void setUsed(Double used) {
        this.used = used;
    }

    public Double getUsage() {
        return usage;
    }

    public void setUsage(Double usage) {
        this.usage = usage;
    }

    @Override
    public String toString() {
        return "DisksInfo{" +
                "dirName='" + dirName + '\'' +
                ", sysTypeName='" + sysTypeName + '\'' +
                ", typeName='" + typeName + '\'' +
                ", total=" + total +
                ", free=" + free +
                ", used=" + used +
                ", usage=" + usage +
                '}';
    }
}
