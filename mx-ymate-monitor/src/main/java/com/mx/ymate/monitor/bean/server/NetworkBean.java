package com.mx.ymate.monitor.bean.server;

import java.io.Serializable;

public class NetworkBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ipv4地址
     */
    private String ipv4Address;

    /**
     * ipv6地址
     */
    private String ipv6Address;

    /**
     * mac地址
     */
    private String macAddress;

    /**
     * 网卡名称
     */
    private String networkName;


    public String getIpv4Address() {
        return ipv4Address;
    }

    public void setIpv4Address(String ipv4Address) {
        this.ipv4Address = ipv4Address;
    }

    public String getIpv6Address() {
        return ipv6Address;
    }

    public void setIpv6Address(String ipv6Address) {
        this.ipv6Address = ipv6Address;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }
}
