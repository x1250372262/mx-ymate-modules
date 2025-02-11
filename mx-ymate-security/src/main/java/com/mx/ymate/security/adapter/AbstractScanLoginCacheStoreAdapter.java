package com.mx.ymate.security.adapter;

import cn.hutool.core.util.StrUtil;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.bean.ScanQrcode;
import net.ymate.platform.commons.util.UUIDUtils;

import static com.mx.ymate.security.base.config.SecurityConstants.QRCODE_KEY;

/**
 * @Author: xujianpeng.
 * @Date 2024/11/22.
 * @Time: 13:34.
 * @Description:
 */
public abstract class AbstractScanLoginCacheStoreAdapter {

    public String createLoginKey() throws Exception {
        return "SCAN_LOGIN-" + UUIDUtils.UUID();
    }

    public String createStoreKey(String loginKey) throws Exception {
        return StrUtil.format(QRCODE_KEY, Security.get().getConfig().project(), loginKey);
    }


    /**
     * 缓存二维码信息
     *
     * @param qrcodeKey
     * @param scanQrcode
     * @throws Exception
     */
    public abstract void cacheScanQrcode(String qrcodeKey, ScanQrcode scanQrcode) throws Exception;

    /**
     * 获取二维码信息
     *
     * @param qrcodeKey
     * @return
     * @throws Exception
     */
    public abstract ScanQrcode getScanQrcode(String qrcodeKey) throws Exception;

    /**
     * 删除二维码信息
     *
     * @param qrcodeKey
     * @throws Exception
     */
    public abstract void deleteScanQrcode(String qrcodeKey) throws Exception;

    /**
     * 清理过期缓存
     * @param config
     * @param time
     * @throws Exception
     */
    public abstract void clearCache(ISecurityConfig config, long time) throws Exception;
}
