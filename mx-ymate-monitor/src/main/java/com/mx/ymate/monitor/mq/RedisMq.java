//package com.mx.ymate.monitor.mq;
//
//import com.alibaba.fastjson.JSON;
//import com.mx.ymate.monitor.enums.TopicEnum;
//import com.mx.ymate.redis.api.RedisApi;
//import redis.clients.jedis.JedisPubSub;
//
//public class RedisMq {
//
//    public final static String FUWUQI_STATUS = "FUWUQI_STATUS";
//
//    public final static String SERVER_STATUS = "SERVER_STATUS";
//
//    public static void push(TopicEnum topicEnum,) throws Exception {
//        RedisApi.publish(REDIS_CHANNEL_STATION_DATA_INIT, JSON.toJSONString(stationMap));
//    }
//
//    public static void stationDataChangePublish(Station station) throws Exception {
//        RedisApi.publish(REDIS_CHANNEL_STATION_DATA_CHANGE, JSON.toJSONString(station));
//    }
//
//    public static void stationStatusPublish(String timeStr) throws Exception {
//        RedisApi.publish(REDIS_CHANNEL_STATION_STATUS_CHANGE, timeStr);
//    }
//
//    public static void stationDataSubscribe(AbstractRedisSubscribeListener redisSubscribeListener) throws Exception {
//        RedisApi.subscribe(new JedisPubSub() {
//            @Override
//            public void onMessage(String channel, String message) {
//                try {
//                    redisSubscribeListener.subscribe(channel, message);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        },REDIS_CHANNEL_STATION_DATA_INIT,REDIS_CHANNEL_STATION_DATA_CHANGE,REDIS_CHANNEL_STATION_STATUS_CHANGE);
//    }
//}
