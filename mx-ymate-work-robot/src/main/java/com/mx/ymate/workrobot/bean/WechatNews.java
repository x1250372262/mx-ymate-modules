package com.mx.ymate.workrobot.bean;

import java.util.List;

/**
 * @Author: xujianpeng.
 * @Date: 2023-06-09 15:14
 * @Description:
 */
public class WechatNews {

    private List<WechatNewsItem> articles;

    public List<WechatNewsItem> getArticles() {
        return articles;
    }

    public void setArticles(List<WechatNewsItem> articles) {
        this.articles = articles;
    }
}
