package com.mx.ymate.redis.api;

import net.ymate.platform.persistence.redis.IRedisCommander;
import net.ymate.platform.persistence.redis.IRedisSessionExecutor;
import net.ymate.platform.persistence.redis.Redis;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description: redisapi实现类
 */
public class RedisApi {


    public static Set<String> keys(String pattern) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().keys(pattern));
    }


    public static Long setExpire(String key, long time) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().expire(key, time));
    }


    public static Long setExpire(byte[] key, long time) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().expire(key, time));
    }


    public static Long getExpire(String key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().ttl(key));
    }


    public static Long getExpire(byte[] key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().ttl(key));
    }


    public static void strSet(String key, String value) throws Exception {
        Redis.get().openSession((IRedisSessionExecutor<Object>) session -> session.getConnectionHolder().getConnection().set(key, value));
    }


    public static void strSet(String key, String value, long time) throws Exception {
        Redis.get().openSession((IRedisSessionExecutor<Object>) session -> session.getConnectionHolder().getConnection().setex(key, time, value));
    }


    public static void strSet(byte[] key, byte[] value) throws Exception {
        Redis.get().openSession((IRedisSessionExecutor<Object>) session -> session.getConnectionHolder().getConnection().set(key, value));
    }


    public static void strSet(byte[] key, byte[] value, long time) throws Exception {
        Redis.get().openSession((IRedisSessionExecutor<Object>) session -> session.getConnectionHolder().getConnection().setex(key, time, value));
    }


    public static Long strIncr(String key, long delta) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().incrBy(key, delta));
    }


    public static Long strDecr(String key, long delta) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().decrBy(key, delta));
    }


    public static String strGet(String key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().get(key));
    }


    public static byte[] strGet(byte[] key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().get(key));
    }


    public static void strDelete(String key) throws Exception {
        Redis.get().openSession((IRedisSessionExecutor<Object>) session -> session.getConnectionHolder().getConnection().del(key));
    }


    public static void strDelete(byte[] key) throws Exception {
        Redis.get().openSession((IRedisSessionExecutor<Object>) session -> session.getConnectionHolder().getConnection().del(key));
    }


    public static boolean strIsExist(String key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().exists(key));
    }


    public static void strDeleteAll() throws Exception {
        Set<String> keys = keys("*");
        Redis.get().openSession(session -> {
            IRedisCommander redisCommander = session.getConnectionHolder().getConnection();
            keys.forEach(redisCommander::del);
            return keys;
        });
    }


    public static Set<String> setGet(String key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().smembers(key));
    }


    public static boolean setHasKey(String key, String value) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().sismember(key, value));
    }


    public static Long set(String key, String... values) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().sadd(key, values));
    }


    public static Long set(String key, long time, String... values) throws Exception {
        Long count = set(key, values);
        setExpire(key, time);
        return count;
    }


    public static Long getSetSize(String key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().scard(key));
    }


    public static Long setRemove(String key, String... values) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().srem(key, values));
    }


    public static List<String> listGet(String key, long start, long end) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().lrange(key, start, end));
    }


    public static Long listSize(String key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().llen(key));
    }


    public static String listIndex(String key, long index) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().lindex(key, index));
    }


    public static void listSet(String key, String value) throws Exception {
        Redis.get().openSession(session -> session.getConnectionHolder().getConnection().rpush(key, value));
    }


    public static void listSet(String key, String value, long time) throws Exception {
        Redis.get().openSession(session -> session.getConnectionHolder().getConnection().rpush(key, value));
        setExpire(key, time);
    }


    public static void listSet(String key, List<String> value) throws Exception {
        Redis.get().openSession(session -> session.getConnectionHolder().getConnection().rpush(key, value.toArray(value.toArray(new String[0]))));
    }


    public static void listSet(String key, List<String> value, long time) throws Exception {
        Redis.get().openSession(session -> session.getConnectionHolder().getConnection().rpush(key, value.toArray(value.toArray(new String[0]))));
        setExpire(key, time);
    }


    public static void lisetUpdateIndex(String key, long index, String value) throws Exception {
        Redis.get().openSession((IRedisSessionExecutor<Object>) session -> session.getConnectionHolder().getConnection().lset(key, index, value));
    }


    public static Long listDelete(String key, long count, String value) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().lrem(key, count, value));
    }


    public static String hashGet(String key, String item) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().hget(key, item));
    }


    public static Map<String, String> hashGet(String key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().hgetAll(key));
    }


    public static void hashSet(String key, Map<String, String> map) throws Exception {
        Redis.get().openSession(session -> session.getConnectionHolder().getConnection().hmset(key, map));
    }


    public static void hashSet(String key, String item, String value) throws Exception {
        Redis.get().openSession(session -> session.getConnectionHolder().getConnection().hset(key, item, value));
    }


    public static void hashDel(String key, String... item) throws Exception {
        Redis.get().openSession(session -> session.getConnectionHolder().getConnection().hdel(key, item));
    }


    public static boolean hashHasKey(String key, String item) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().hexists(key, item));
    }


    public static double hashIncr(String key, String item, double by) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().hincrByFloat(key, item, by));
    }


    public static double hasDecr(String key, String item, double by) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().hincrByFloat(key, item, -by));
    }


    public static Long delete(String key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().del(key));
    }

    public static void subscribe(JedisPubSub jedisPubSub, String... channels) throws Exception {
        Redis.get().subscribe(jedisPubSub, channels);
    }

    public static void publish(String channel, String message) throws Exception {
        Redis.get().openSession((IRedisSessionExecutor<Object>) session -> session.getConnectionHolder().getConnection().publish(channel, message));
    }

    public static void publish(byte[] channel, byte[] message) throws Exception {
        Redis.get().openSession((IRedisSessionExecutor<Object>) session -> session.getConnectionHolder().getConnection().publish(channel, message));
    }
}
