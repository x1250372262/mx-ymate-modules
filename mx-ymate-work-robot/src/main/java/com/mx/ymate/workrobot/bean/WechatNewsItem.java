package com.mx.ymate.workrobot.bean;

/**
 * @Author: xujianpeng.
 * @Date: 2023-06-09 15:14
 * @Description:
 */
public class WechatNewsItem {

    private String title;

    private String description;

    private String url;

    private String picurl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }
}
