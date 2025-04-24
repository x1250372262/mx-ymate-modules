package com.mx.ymate.dev.support.mvc.i18n.validate;

import net.ymate.platform.core.beans.annotation.Ignored;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Ignored
public interface IMxRSAKeyProvider {

    /**
     * 获取RSA公钥
     *
     * @return 返回RSA公钥
     */
    String getPublicKey();

    /**
     * 获取RSA私钥
     *
     * @return 返回RSA私钥
     */
    String getPrivateKey();
}
