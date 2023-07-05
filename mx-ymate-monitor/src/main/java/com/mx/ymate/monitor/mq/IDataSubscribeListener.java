package com.mx.ymate.monitor.mq;

import com.mx.ymate.monitor.enums.TopicEnum;

public interface IDataSubscribeListener {

    void subscribe(TopicEnum topicEnum, String message) throws Exception;
}
