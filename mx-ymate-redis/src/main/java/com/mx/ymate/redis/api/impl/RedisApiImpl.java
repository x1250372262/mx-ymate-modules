package com.mx.ymate.redis.api.impl;

import com.mx.ymate.redis.api.IRedisApi;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.persistence.redis.IRedisCommander;
import net.ymate.platform.persistence.redis.IRedisSessionExecutor;
import net.ymate.platform.persistence.redis.Redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description: redisapi实现类
 */
@Bean
public class RedisApiImpl implements IRedisApi {

    @Override
    public Set<String> keys(String pattern) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().keys(pattern));
    }

    @Override
    public Long setExpire(String key, long time) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().expire(key, time));
    }

    @Override
    public Long setExpire(byte[] key, long time) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().expire(key, time));
    }

    @Override
    public Long getExpire(String key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().ttl(key));
    }

    @Override
    public Long getExpire(byte[] key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().ttl(key));
    }

    @Override
    public void strSet(String key, String value) throws Exception {
        Redis.get().openSession((IRedisSessionExecutor<Object>) session -> session.getConnectionHolder().getConnection().set(key, value));
    }

    @Override
    public void strSet(String key, String value, long time) throws Exception {
        Redis.get().openSession((IRedisSessionExecutor<Object>) session -> session.getConnectionHolder().getConnection().setex(key, time, value));
    }

    @Override
    public void strSet(byte[] key, byte[] value) throws Exception {
        Redis.get().openSession((IRedisSessionExecutor<Object>) session -> session.getConnectionHolder().getConnection().set(key, value));
    }

    @Override
    public void strSet(byte[] key, byte[] value, long time) throws Exception {
        Redis.get().openSession((IRedisSessionExecutor<Object>) session -> session.getConnectionHolder().getConnection().setex(key, time, value));
    }


    @Override
    public Long strIncr(String key, long delta) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().incrBy(key, delta));
    }

    @Override
    public Long strDecr(String key, long delta) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().decrBy(key, delta));
    }

    @Override
    public String strGet(String key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().get(key));
    }

    @Override
    public byte[] strGet(byte[] key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().get(key));
    }

    @Override
    public void strDelete(String key) throws Exception {
        Redis.get().openSession((IRedisSessionExecutor<Object>) session -> session.getConnectionHolder().getConnection().del(key));
    }

    @Override
    public void strDelete(byte[] key) throws Exception {
        Redis.get().openSession((IRedisSessionExecutor<Object>) session -> session.getConnectionHolder().getConnection().del(key));
    }

    @Override
    public boolean strIsExist(String key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().exists(key));
    }

    @Override
    public void strDeleteAll() throws Exception {
        Set<String> keys = keys("*");
        Redis.get().openSession(session -> {
            IRedisCommander redisCommander = session.getConnectionHolder().getConnection();
            keys.forEach(redisCommander::del);
            return keys;
        });
    }

    @Override
    public Set<String> setGet(String key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().smembers(key));
    }

    @Override
    public boolean setHasKey(String key, String value) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().sismember(key, value));
    }

    @Override
    public Long set(String key, String... values) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().sadd(key, values));
    }

    @Override
    public Long set(String key, long time, String... values) throws Exception {
        Long count = set(key, values);
        setExpire(key, time);
        return count;
    }

    @Override
    public Long getSetSize(String key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().scard(key));
    }

    @Override
    public Long setRemove(String key, String... values) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().srem(key, values));
    }

    @Override
    public List<String> listGet(String key, long start, long end) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().lrange(key, start, end));
    }

    @Override
    public Long listSize(String key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().llen(key));
    }

    @Override
    public String listIndex(String key, long index) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().lindex(key, index));
    }

    @Override
    public void listSet(String key, String value) throws Exception {
        Redis.get().openSession(session -> session.getConnectionHolder().getConnection().rpush(key, value));
    }

    @Override
    public void listSet(String key, String value, long time) throws Exception {
        Redis.get().openSession(session -> session.getConnectionHolder().getConnection().rpush(key, value));
        setExpire(key, time);
    }

    @Override
    public void listSet(String key, List<String> value) throws Exception {
        Redis.get().openSession(session -> session.getConnectionHolder().getConnection().rpush(key, value.toArray(value.toArray(new String[0]))));
    }

    @Override
    public void listSet(String key, List<String> value, long time) throws Exception {
        Redis.get().openSession(session -> session.getConnectionHolder().getConnection().rpush(key, value.toArray(value.toArray(new String[0]))));
        setExpire(key, time);
    }


    @Override
    public void lisetUpdateIndex(String key, long index, String value) throws Exception {
        Redis.get().openSession((IRedisSessionExecutor<Object>) session -> session.getConnectionHolder().getConnection().lset(key, index, value));
    }

    @Override
    public Long listDelete(String key, long count, String value) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().lrem(key, count, value));
    }

    @Override
    public String hashGet(String key, String item) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().hget(key, item));
    }

    @Override
    public Map<String, String> hashGet(String key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().hgetAll(key));
    }

    @Override
    public void hashSet(String key, Map<String, String> map) throws Exception {
        Redis.get().openSession(session -> session.getConnectionHolder().getConnection().hmset(key, map));
    }

    @Override
    public void hashSet(String key, String item, String value) throws Exception {
        Redis.get().openSession(session -> session.getConnectionHolder().getConnection().hset(key, item, value));
    }

    @Override
    public void hashDel(String key, String... item) throws Exception {
        Redis.get().openSession(session -> session.getConnectionHolder().getConnection().hdel(key, item));
    }

    @Override
    public boolean hashHasKey(String key, String item) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().hexists(key, item));
    }

    @Override
    public double hashIncr(String key, String item, double by) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().hincrByFloat(key, item, by));
    }

    @Override
    public double hasDecr(String key, String item, double by) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().hincrByFloat(key, item, -by));
    }

    @Override
    public Long delete(String key) throws Exception {
        return Redis.get().openSession(session -> session.getConnectionHolder().getConnection().del(key));
    }
}
