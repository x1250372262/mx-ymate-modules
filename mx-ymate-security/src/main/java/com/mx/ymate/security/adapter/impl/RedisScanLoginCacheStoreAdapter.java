package com.mx.ymate.security.adapter.impl;

import com.alibaba.fastjson.JSONObject;
import com.mx.ymate.redis.api.RedisApi;
import com.mx.ymate.security.adapter.AbstractScanLoginCacheStoreAdapter;
import com.mx.ymate.security.base.bean.LoginUser;
import com.mx.ymate.security.base.bean.ScanQrcode;
import net.ymate.platform.commons.json.JsonWrapper;

/**
 * @Author: xujianpeng.
 * @Date 2024/12/5.
 * @Time: 14:06.
 * @Description:
 */
public class RedisScanLoginCacheStoreAdapter extends AbstractScanLoginCacheStoreAdapter {

    @Override
    public void cacheScanQrcode(String qrcodeKey, ScanQrcode scanQrcode) throws Exception {
        RedisApi.strDelete(qrcodeKey);
        RedisApi.strSet(qrcodeKey, JSONObject.toJSONString(scanQrcode),scanQrcode.getExpire());
    }

    @Override
    public ScanQrcode getScanQrcode(String qrcodeKey) throws Exception {
        return JsonWrapper.deserialize(RedisApi.strGet(qrcodeKey), ScanQrcode.class);
    }

    @Override
    public void deleteScanQrcode(String qrcodeKey) throws Exception {
        RedisApi.strDelete(qrcodeKey);
    }
}
