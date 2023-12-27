package com.mx.ymate.dev.support.Ip2region;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @create: 2023-08-03 16:58
 * @Description: ip地址显示结果类
 */
public class IpRegionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String country;

    private String province;

    private String city;

    private String isp;

    public IpRegionBean(String country, String province, String city, String isp) {
        if (StringUtils.isNotBlank(country) && !"0".equals(country)) {
            this.country = country;
        } else {
            this.country = "未知";
        }
        if (StringUtils.isNotBlank(province) && !"0".equals(province)) {
            this.province = province;
        } else {
            this.province = "未知";
        }
        if (StringUtils.isNotBlank(city) && !"0".equals(city)) {
            this.city = city;
        } else {
            this.city = "未知";
        }
        if (StringUtils.isNotBlank(isp) && !"0".equals(isp)) {
            this.isp = isp;
        } else {
            this.isp = "未知";
        }
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        if (StringUtils.isNotBlank(country) && !"0".equals(country)) {
            this.country = country;
        } else {
            this.country = "未知";
        }
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        if (StringUtils.isNotBlank(province) && !"0".equals(province)) {
            this.province = province;
        } else {
            this.province = "未知";
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (StringUtils.isNotBlank(city) && !"0".equals(city)) {
            this.city = city;
        } else {
            this.city = "未知";
        }
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        if (StringUtils.isNotBlank(isp) && !"0".equals(isp)) {
            this.isp = isp;
        } else {
            this.isp = "未知";
        }
    }
}
