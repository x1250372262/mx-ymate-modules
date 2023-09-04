package com.mx.ymate.monitor.mq;

public interface IDataSubscribeListener {

    void subscribe(String message) throws Exception;
}
