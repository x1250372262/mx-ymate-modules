package com.mx.ymate.workrobot.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: mengxiang.
 * @Date: 2023-06-09 14:52
 * @Description:
 */
public class WechatWorkRobotMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private final static String TYPE_TEXT = "text";
    private final static String TYPE_NEWS = "news";
    private final static String ALL = "@all";

    private String webhook;

    private String msgtype;

    private WechatText text;

    private WechatNews news;


    /**
     * 图文消息
     *
     * @param wechatNewsItems
     * @return
     */
    public static WechatWorkRobotMessage buildNewsMessage(List<WechatNewsItem> wechatNewsItems) {
        WechatWorkRobotMessage wechatWorkRobotMessage = new WechatWorkRobotMessage();
        wechatWorkRobotMessage.setMsgtype(TYPE_NEWS);
        WechatNews wechatNews = new WechatNews();
        wechatNews.setArticles(wechatNewsItems);
        wechatWorkRobotMessage.setNews(wechatNews);
        return wechatWorkRobotMessage;
    }

    /**
     * 图文消息
     *
     * @param wechatNewsItem
     * @return
     */
    public static WechatWorkRobotMessage buildNewsMessage(WechatNewsItem wechatNewsItem) {
        WechatWorkRobotMessage wechatWorkRobotMessage = new WechatWorkRobotMessage();
        wechatWorkRobotMessage.setMsgtype(TYPE_NEWS);
        WechatNews wechatNews = new WechatNews();
        List<WechatNewsItem> list = new ArrayList<>();
        list.add(wechatNewsItem);
        wechatNews.setArticles(list);
        wechatWorkRobotMessage.setNews(wechatNews);
        return wechatWorkRobotMessage;
    }


    /**
     * 文本消息
     *
     * @param content
     * @return
     */
    public static WechatWorkRobotMessage buildText(String content) {
        return buildText(content, false);
    }

    /**
     * 文本消息
     *
     * @param content
     * @param atAll
     * @return
     */
    public static WechatWorkRobotMessage buildText(String content, boolean atAll) {
        WechatWorkRobotMessage wechatWorkRobotMessage = new WechatWorkRobotMessage();
        wechatWorkRobotMessage.setMsgtype(TYPE_TEXT);
        WechatText wechatText = new WechatText();
        wechatText.setContent(content);
        List<String> mentioned_list = wechatText.getMentioned_list();
        if (atAll) {
            if (mentioned_list == null) {
                mentioned_list = new ArrayList<>();
            }
            mentioned_list.add(ALL);
            wechatText.setMentioned_list(mentioned_list);
        }
        wechatWorkRobotMessage.setText(wechatText);
        return wechatWorkRobotMessage;
    }


    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public WechatText getText() {
        return text;
    }

    public void setText(WechatText text) {
        this.text = text;
    }

    public WechatNews getNews() {
        return news;
    }

    public void setNews(WechatNews news) {
        this.news = news;
    }

    @Override
    public String toString() {
        return "WechatWorkRobotMessage{" +
                "webhook='" + webhook + '\'' +
                ", msgtype='" + msgtype + '\'' +
                ", text=" + text +
                ", news=" + news +
                '}';
    }
}
