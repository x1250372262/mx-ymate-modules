package com.mx.ymate.mqtt;

import com.mx.ymate.mqtt.enums.QosEnum;

import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2024/10/29.
 * @Time: 15:13.
 * @Description:
 */
public class ResubscribeBean {

    private List<String> topicList;

    private QosEnum qosEnum;


    public ResubscribeBean(List<String> topicList, QosEnum qosEnum) {
        this.topicList = topicList;
        this.qosEnum = qosEnum;
    }

    public List<String> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<String> topicList) {
        this.topicList = topicList;
    }

    public QosEnum getQosEnum() {
        return qosEnum;
    }

    public void setQosEnum(QosEnum qosEnum) {
        this.qosEnum = qosEnum;
    }
}
