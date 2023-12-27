package com.mx.ymate.workrobot.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: mengxiang.
 * @Date: 2023-06-09 15:14
 * @Description:
 */
public class WechatText implements Serializable {

    private static final long serialVersionUID = 1L;

    private String content;
    private List<String> mentioned_list;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getMentioned_list() {
        return mentioned_list;
    }

    public void setMentioned_list(List<String> mentioned_list) {
        this.mentioned_list = mentioned_list;
    }

    @Override
    public String toString() {
        return "WechatText{" +
                "content='" + content + '\'' +
                ", mentioned_list=" + mentioned_list +
                '}';
    }
}
