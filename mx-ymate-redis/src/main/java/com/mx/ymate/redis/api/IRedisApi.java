package com.mx.ymate.redis.api;


import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: mengxiang.
 * @create: 2021-09-04 18:59
 * @Description:
 */
public interface IRedisApi {


    /**
     * 获取所有key
     * @param pattern
     * @return
     * @throws Exception
     */
    Set<String> keys(String pattern) throws Exception;

    /**
     * 设置过期时间
     *
     * @param key
     * @param time
     * @return
     * @throws Exception
     */
    Long setExpire(String key, long time) throws Exception;


    /**
     * 设置过期时间
     *
     * @param key
     * @param time
     * @return
     * @throws Exception
     */
    Long setExpire(byte[] key, long time) throws Exception;

    /**
     * 获取过期时间
     *
     * @param key
     * @return
     */
    Long getExpire(String key) throws Exception;

    /**
     * 获取过期时间
     *
     * @param key
     * @return
     */
    Long getExpire(byte[] key) throws Exception;

    /**
     * 新增一个字符串类型的值
     *
     * @param key
     * @param value
     * @throws Exception
     */
    void strSet(String key, String value) throws Exception;

    /**
     * 新增一个字符串类型的值
     *
     * @param key
     * @param value
     * @param time
     * @throws Exception
     */
    void strSet(String key, String value, long time) throws Exception;

    /**
     * 新增一个字符串类型的值
     *
     * @param key
     * @param value
     * @throws Exception
     */
    void strSet(byte[] key, byte[] value) throws Exception;

    /**
     * 新增一个字符串类型的值
     *
     * @param key
     * @param value
     * @param time
     * @throws Exception
     */
    void strSet(byte[] key, byte[] value, long time) throws Exception;


    /**
     * 以增量的方式
     *
     * @param key
     * @param delta
     * @return
     * @throws Exception
     */
    Long strIncr(String key, long delta) throws Exception;

    /**
     * strIncr反之
     *
     * @param key
     * @param delta
     * @return
     * @throws Exception
     */
    Long strDecr(String key, long delta) throws Exception;

    /**
     * 获取一个字符串类型的值
     *
     * @param key
     * @return
     * @throws Exception
     */
    String strGet(String key) throws Exception;

    /**
     * 获取一个字符串类型的值
     *
     * @param key
     * @return
     * @throws Exception
     */
    byte[] strGet (byte[] key) throws Exception;

    /**
     * 删除一个字符串类型的值
     *
     * @param key
     * @throws Exception
     */
    void strDelete(String key) throws Exception;

    /**
     * 删除一个字符串类型的值
     *
     * @param key
     * @throws Exception
     */
    void strDelete(byte[] key) throws Exception;

    /**
     * 判断字符串值是否存在
     *
     * @param key
     * @return
     * @throws Exception
     */
    boolean strIsExist(String key) throws Exception;

    /**
     * 删除所有的字符串值
     *
     * @throws Exception
     */
    void strDeleteAll() throws Exception;

    /**
     * 获取key中的值
     *
     * @param key
     * @return
     * @throws Exception
     */
    Set<String> setGet(String key) throws Exception;

    /**
     * 判断是否存在
     *
     * @param key
     * @param value
     * @return
     * @throws Exception
     */
    boolean setHasKey(String key, String value) throws Exception;

    /**
     * 向key中批量添加值
     *
     * @param key
     * @param values
     * @return
     * @throws Exception
     */
    Long set(String key, String... values) throws Exception;

    /**
     * 向key中批量添加值
     *
     * @param key
     * @param time
     * @param values
     * @return
     * @throws Exception
     */
    Long set(String key, long time, String... values) throws Exception;

    /**
     * 获取长度
     *
     * @param key
     * @return
     * @throws Exception
     */
    Long getSetSize(String key) throws Exception;

    /**
     * 删除
     *
     * @param key
     * @param values
     * @return
     * @throws Exception
     */
    Long setRemove(String key, String... values) throws Exception;

    /**
     * 获取list值
     *
     * @param key
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    List<String> listGet(String key, long start, long end) throws Exception;

    /**
     * 获取大小
     *
     * @param key
     * @return
     * @throws Exception
     */
    Long listSize(String key) throws Exception;

    /**
     * 获取索引对应的值
     *
     * @param key
     * @param index
     * @return
     * @throws Exception
     */
    String listIndex(String key, long index) throws Exception;

    /**
     * 设置值
     *
     * @param key
     * @param value
     * @throws Exception
     */
    void listSet(String key, String value) throws Exception;

    /**
     * 设置值
     *
     * @param key
     * @param value
     * @param time
     * @throws Exception
     */
    void listSet(String key, String value, long time) throws Exception;

    /**
     * 设置值
     *
     * @param key
     * @param value
     * @throws Exception
     */
    void listSet(String key, List<String> value) throws Exception;

    /**
     * 设置值
     *
     * @param key
     * @param value
     * @param time
     * @throws Exception
     */
    void listSet(String key, List<String> value, long time) throws Exception;

    /**
     * 修改索引对应的值
     *
     * @param key
     * @param index
     * @param value
     * @throws Exception
     */
    void lisetUpdateIndex(String key, long index, String value) throws Exception;

    /**
     * 删除
     *
     * @param key
     * @param count
     * @param value
     * @return
     * @throws Exception
     */
    Long listDelete(String key, long count, String value) throws Exception;

    /**
     * 获取hash值
     *
     * @param key
     * @param item
     * @return
     * @throws Exception
     */
    String hashGet(String key, String item) throws Exception;

    /**
     * 获取hash值
     *
     * @param key
     * @return
     * @throws Exception
     */
    Map<String, String> hashGet(String key) throws Exception;

    /**
     * 设置hash值
     *
     * @param key
     * @param map
     * @throws Exception
     */
    void hashSet(String key, Map<String, String> map) throws Exception;

    /**
     * 设置hash值
     *
     * @param key
     * @param item
     * @param value
     * @throws Exception
     */
    void hashSet(String key, String item, String value) throws Exception;

    /**
     * 删除hash值
     *
     * @param key
     * @param item
     * @throws Exception
     */
    void hashDel(String key, String... item) throws Exception;

    /**
     * 是否包含
     *
     * @param key
     * @param item
     * @return
     * @throws Exception
     */
    boolean hashHasKey(String key, String item) throws Exception;

    /**
     * 增量方式
     *
     * @param key
     * @param item
     * @param by
     * @return
     * @throws Exception
     */
    double hashIncr(String key, String item, double by) throws Exception;

    /**
     * hashIncr反之
     *
     * @param key
     * @param item
     * @param by
     * @return
     * @throws Exception
     */
    double hasDecr(String key, String item, double by) throws Exception;


    /**
     * 删除一个值
     *
     * @param key
     * @return
     * @throws Exception
     */
    Long delete(String key) throws Exception;


}
