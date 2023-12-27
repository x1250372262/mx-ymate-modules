package com.mx.ymate.workrobot.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: mengxiang.
 * @Date: 2023-06-09 15:14
 * @Description:
 */
public class WechatNews implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<WechatNewsItem> articles;

    public List<WechatNewsItem> getArticles() {
        return articles;
    }

    public void setArticles(List<WechatNewsItem> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "WechatNews{" +
                "articles=" + articles +
                '}';
    }
}
