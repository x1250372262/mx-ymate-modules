package com.mx.ymate.monitor.mq;

import com.alibaba.fastjson.JSON;
import com.mx.ymate.monitor.bean.server.ServerBean;
import com.mx.ymate.redis.api.RedisApi;
import redis.clients.jedis.JedisPubSub;

public class RedisMq {

    public static final String TOPIC = "mx_server_status_topic";

    public static void pushServer(ServerBean serverBean) throws Exception {
        RedisApi.publish(TOPIC, JSON.toJSONString(serverBean));
    }

    public static void subscribe(IDataSubscribeListener redisSubscribeListener) throws Exception {
        RedisApi.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                try {
                    redisSubscribeListener.subscribe(message);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, TOPIC);
    }
}
