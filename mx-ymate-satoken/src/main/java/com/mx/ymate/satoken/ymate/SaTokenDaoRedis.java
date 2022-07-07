package com.mx.ymate.satoken.ymate;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.hutool.core.util.StrUtil;
import com.mx.ymate.redis.api.IRedisApi;
import com.mx.ymate.redis.api.impl.RedisApiImpl;
import net.ymate.platform.commons.serialize.ISerializer;
import net.ymate.platform.commons.serialize.SerializerManager;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Bean
public class SaTokenDaoRedis implements SaTokenDao {

//    @Inject
    private final IRedisApi iRedisApi = new RedisApiImpl();

    private final ISerializer serializer = SerializerManager.getDefaultSerializer();

    private final String CACHE_NAME = "saTokenCache:{}";

    /**
     * 获取Value，如无返空
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        try {
            key = StrUtil.format(CACHE_NAME, key);
            return iRedisApi.strGet(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 写入Value，并设定存活时间 (单位: 秒)
     *
     * @param key
     * @param value
     * @param timeout
     */
    @Override
    public void set(String key, String value, long timeout) {
        if (timeout == 0 || timeout <= SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }
        key = StrUtil.format(CACHE_NAME, key);
        try {
            if (timeout == SaTokenDao.NEVER_EXPIRE) {
                iRedisApi.strSet(key, value);
            } else {
                iRedisApi.strSet(key, value, timeout);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改指定key-value键值对 (过期时间不变)
     *
     * @param key
     * @param value
     */
    @Override
    public void update(String key, String value) {
        long expire = getTimeout(key);
        // -2 = 无此键
        if (expire == SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }
        this.set(key, value, expire);
    }

    /**
     * 删除Value
     *
     * @param key
     */
    @Override
    public void delete(String key) {
        key = StrUtil.format(CACHE_NAME, key);
        try {
            iRedisApi.strDelete(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Value的剩余存活时间 (单位: 秒)
     *
     * @param key
     * @return
     */
    @Override
    public long getTimeout(String key) {
        key = StrUtil.format(CACHE_NAME, key);
        try {
            return iRedisApi.getExpire(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改Value的剩余存活时间 (单位: 秒)
     *
     * @param key
     * @param timeout
     */
    @Override
    public void updateTimeout(String key, long timeout) {
        key = StrUtil.format(CACHE_NAME, key);
        //判断是否想要设置为永久
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            long expire = getTimeout(key);
            if (expire != SaTokenDao.NEVER_EXPIRE) {
                // 如果尚未被设置为永久，那么再次set一次
                this.set(key, this.get(key), timeout);
            }
            return;
        }
        try {
            iRedisApi.setExpire(key, timeout);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Object，如无返空
     *
     * @param key
     * @return
     */
    @Override
    public Object getObject(String key) {
        try {
            return valueFromBytes(iRedisApi.strGet(keyToBytes(key)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 写入Object，并设定存活时间 (单位: 秒)
     *
     * @param key
     * @param object
     * @param timeout
     */
    @Override
    public void setObject(String key, Object object, long timeout) {
        if (timeout == 0 || timeout <= SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }
        try {
            if (timeout == SaTokenDao.NEVER_EXPIRE) {
                iRedisApi.strSet(keyToBytes(key), valueToBytes(object));
            } else {
                iRedisApi.strSet(keyToBytes(key), valueToBytes(object), timeout);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新Object (过期时间不变)
     *
     * @param key
     * @param object
     */
    @Override
    public void updateObject(String key, Object object) {
        long expire = getObjectTimeout(key);
        // -2 = 无此键
        if (expire == SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }
        this.setObject(key, object, expire);
    }

    /**
     * 删除Object
     *
     * @param key
     */
    @Override
    public void deleteObject(String key) {
        try {
            iRedisApi.strDelete(keyToBytes(key));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getObjectTimeout(String key) {
        try {
            return iRedisApi.getExpire(keyToBytes(key));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改Object的剩余存活时间 (单位: 秒)
     *
     * @param key
     * @param timeout
     */
    @Override
    public void updateObjectTimeout(String key, long timeout) {
        key = StrUtil.format(CACHE_NAME, key);
        //判断是否想要设置为永久
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            long expire = getObjectTimeout(key);
            if (expire != SaTokenDao.NEVER_EXPIRE) {
                // 如果尚未被设置为永久，那么再次set一次
                this.setObject(key, this.getObject(key), timeout);
            }
            return;
        }
        try {
            iRedisApi.setExpire(keyToBytes(key), timeout);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 搜索数据
     *
     * @param prefix
     * @param keyword
     * @param start
     * @param size
     * @return
     */
    @Override
    public List<String> searchData(String prefix, String keyword, int start, int size) {
        Set<String> keys;
        try {
            keys = iRedisApi.keys(prefix + "*" + keyword + "*");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<String> list = new ArrayList<>(keys);
        return SaFoxUtil.searchList(list, start, size);
    }


    protected byte[] keyToBytes(Object key) {
        return key.toString().getBytes();
    }

    protected byte[] valueToBytes(Object value) throws Exception {
        return serializer.serialize(value);
    }

    protected Object valueFromBytes(byte[] bytes) throws Exception {
        if(bytes == null){
            return null;
        }
        return serializer.deserialize(bytes, Object.class);
    }
}
