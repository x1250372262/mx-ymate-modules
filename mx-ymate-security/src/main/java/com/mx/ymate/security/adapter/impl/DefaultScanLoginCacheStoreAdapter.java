package com.mx.ymate.security.adapter.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.adapter.AbstractScanLoginCacheStoreAdapter;
import com.mx.ymate.security.base.bean.ScanQrcode;
import net.ymate.platform.commons.util.ThreadUtils;

import java.util.concurrent.TimeUnit;

/**
 * @Author: xujianpeng.
 * @Date 2024/12/5.
 * @Time: 14:06.
 * @Description:
 */
public class DefaultScanLoginCacheStoreAdapter extends AbstractScanLoginCacheStoreAdapter {

    public Cache<String, ScanQrcode> dataMap;

    public DefaultScanLoginCacheStoreAdapter(int scanLoginQrCodeExpire){
        dataMap = Caffeine.newBuilder()
                .scheduler(Scheduler.forScheduledExecutorService(ThreadUtils.newScheduledThreadPool(1)))
                .expireAfterWrite(scanLoginQrCodeExpire, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public void cacheScanQrcode(String qrcodeKey, ScanQrcode scanQrcode) throws Exception {
        dataMap.invalidate(qrcodeKey);
        dataMap.put(qrcodeKey, scanQrcode);
    }

    @Override
    public ScanQrcode getScanQrcode(String qrcodeKey) throws Exception {
        return dataMap.getIfPresent(qrcodeKey);
    }

    @Override
    public void deleteScanQrcode(String qrcodeKey) throws Exception {
        dataMap.invalidate(qrcodeKey);
    }

    @Override
    public void clearCache(ISecurityConfig config, long time) throws Exception {
        dataMap.cleanUp();
    }
}
