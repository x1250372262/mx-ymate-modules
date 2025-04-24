package com.mx.ymate.workrobot.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class WechatText implements Serializable {

    private static final long serialVersionUID = 1L;

    private String content;
    private List<String> mentionedList;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getMentionedList() {
        return mentionedList;
    }

    public void setMentionedList(List<String> mentionedList) {
        this.mentionedList = mentionedList;
    }

    @Override
    public String toString() {
        return "WechatText{" +
                "content='" + content + '\'' +
                ", mentionedList=" + mentionedList +
                '}';
    }
}
