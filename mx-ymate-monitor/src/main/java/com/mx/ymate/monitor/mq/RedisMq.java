package com.mx.ymate.monitor.mq;

import com.alibaba.fastjson.JSON;
import com.mx.ymate.monitor.bean.project.ProjectBean;
import com.mx.ymate.monitor.bean.server.ServerBean;
import com.mx.ymate.monitor.enums.TopicEnum;
import com.mx.ymate.redis.api.RedisApi;
import redis.clients.jedis.JedisPubSub;

public class RedisMq {

    public static void pushServer(ServerBean serverBean) throws Exception {
        RedisApi.publish(TopicEnum.SERVER_STATUS.type(), JSON.toJSONString(serverBean));
    }

    public static void pushProject(ProjectBean projectBean) throws Exception {
        RedisApi.publish(TopicEnum.PROJECT_STATUS.type(), JSON.toJSONString(projectBean));
    }

    public static void subscribe(IDataSubscribeListener redisSubscribeListener) throws Exception {
        RedisApi.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                try {
                    redisSubscribeListener.subscribe(TopicEnum.valueTo(channel), message);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, TopicEnum.SERVER_STATUS.type(), TopicEnum.PROJECT_STATUS.type());
    }
}
