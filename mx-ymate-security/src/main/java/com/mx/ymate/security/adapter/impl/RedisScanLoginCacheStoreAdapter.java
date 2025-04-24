package com.mx.ymate.security.adapter.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mx.ymate.redis.api.RedisApi;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.adapter.AbstractScanLoginCacheStoreAdapter;
import com.mx.ymate.security.base.bean.ScanQrcode;
import net.ymate.platform.commons.json.JsonWrapper;
import net.ymate.platform.log.Logs;
import net.ymate.platform.persistence.redis.Redis;
import redis.clients.jedis.ScanParams;

import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2024/12/5.
 * @Time: 14:06.
 * @Description:
 */
public class RedisScanLoginCacheStoreAdapter extends AbstractScanLoginCacheStoreAdapter {

    @Override
    public void cacheScanQrcode(String qrcodeKey, ScanQrcode scanQrcode) throws Exception {
        RedisApi.strDelete(qrcodeKey);
        RedisApi.strSet(qrcodeKey, JSONObject.toJSONString(scanQrcode), scanQrcode.getExpire());
    }

    @Override
    public ScanQrcode getScanQrcode(String qrcodeKey) throws Exception {
        return JsonWrapper.deserialize(RedisApi.strGet(qrcodeKey), ScanQrcode.class);
    }

    @Override
    public void deleteScanQrcode(String qrcodeKey) throws Exception {
        RedisApi.strDelete(qrcodeKey);
    }

    @Override
    public void clearCache(ISecurityConfig config, long time) throws Exception {
        String key = StrUtil.format("{}:qrcodeKey:*", config.project());
        ScanParams scanParams = new ScanParams().match(key).count(1000);
        List<String> scanResultList = Redis.get().openSession(session -> session.getConnectionHolder().getConnection().scan("0", scanParams)).getResult();
        for (String scanResult : scanResultList) {
            Long ttl = RedisApi.getExpire(scanResult);
            Logs.get().getLogger().debug("ttl::::" + ttl);
            // 检查是否已过期
            if (ttl != null && ttl < time) {
                RedisApi.strDelete(scanResult);
            }
        }
    }
}
