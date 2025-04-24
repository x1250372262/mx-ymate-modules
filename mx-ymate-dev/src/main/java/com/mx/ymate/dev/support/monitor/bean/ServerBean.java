package com.mx.ymate.dev.support.monitor.bean;


import java.io.Serializable;
import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class ServerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统
     */
    private String os;

    /**
     * 系统架构
     */
    private String osArch;

    /**
     * java版本
     */
    private String javaVersion;

    /**
     * 工作目录
     */
    private String userDir;

    /**
     * cpu核心数
     */
    private Integer cpuCount;

    /**
     * cpu型号
     */
    private String cpuModel;

    /**
     * cpu使用率
     */
    private Double cpuUsage;

    /**
     * 总内存
     */
    private Double totalMemory;

    /**
     * 使用内存
     */
    private Double usedMemory;

    /**
     * 内存使用率
     */
    private Double memoryUsage;

    /**
     * 可用虚拟总内存
     */
    private Double swapTotalMemory;

    /**
     * 已用虚拟内存
     */
    private Double swapUsedMemory;

    /**
     * 磁盘信息
     */
    private List<DiskBean> disksList;

    /**
     * 网卡信息
     */
    private List<NetworkBean> networkList;


    /**
     * 主机host
     */
    private String host;

    /**
     * 主机名称
     */
    private String hostName;

    /**
     * 系统启动时间
     */
    private Long bootTime;

    /**
     * 系统运行时间
     */
    private String runTime;

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsArch() {
        return osArch;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public String getUserDir() {
        return userDir;
    }

    public void setUserDir(String userDir) {
        this.userDir = userDir;
    }

    public Integer getCpuCount() {
        return cpuCount;
    }

    public void setCpuCount(Integer cpuCount) {
        this.cpuCount = cpuCount;
    }

    public String getCpuModel() {
        return cpuModel;
    }

    public void setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(Double totalMemory) {
        this.totalMemory = totalMemory;
    }

    public Double getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(Double usedMemory) {
        this.usedMemory = usedMemory;
    }

    public Double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Double getSwapTotalMemory() {
        return swapTotalMemory;
    }

    public void setSwapTotalMemory(Double swapTotalMemory) {
        this.swapTotalMemory = swapTotalMemory;
    }

    public Double getSwapUsedMemory() {
        return swapUsedMemory;
    }

    public void setSwapUsedMemory(Double swapUsedMemory) {
        this.swapUsedMemory = swapUsedMemory;
    }

    public List<DiskBean> getDisksList() {
        return disksList;
    }

    public void setDisksList(List<DiskBean> disksList) {
        this.disksList = disksList;
    }

    public List<NetworkBean> getNetworkList() {
        return networkList;
    }

    public void setNetworkList(List<NetworkBean> networkList) {
        this.networkList = networkList;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Long getBootTime() {
        return bootTime;
    }

    public void setBootTime(Long bootTime) {
        this.bootTime = bootTime;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    @Override
    public String toString() {
        return "ServerBean{" +
                "os='" + os + '\'' +
                ", osArch='" + osArch + '\'' +
                ", javaVersion='" + javaVersion + '\'' +
                ", userDir='" + userDir + '\'' +
                ", cpuCount=" + cpuCount +
                ", cpuModel='" + cpuModel + '\'' +
                ", cpuUsage=" + cpuUsage +
                ", totalMemory=" + totalMemory +
                ", usedMemory=" + usedMemory +
                ", memoryUsage=" + memoryUsage +
                ", swapTotalMemory=" + swapTotalMemory +
                ", swapUsedMemory=" + swapUsedMemory +
                ", disksList=" + disksList +
                ", networkList=" + networkList +
                ", host='" + host + '\'' +
                ", hostName='" + hostName + '\'' +
                ", bootTime=" + bootTime +
                ", runTime='" + runTime + '\'' +
                "} " + super.toString();
    }
}
