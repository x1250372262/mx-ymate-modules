package com.mx.ymate.workrobot.bean;

import java.util.List;

/**
 * @Author: xujianpeng.
 * @Date: 2023-06-09 15:14
 * @Description:
 */
public class WechatText {

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
}
