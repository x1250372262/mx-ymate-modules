package com.mx.ymate.sms;

import com.mx.ymate.sms.adapter.ISmsAdapter;
import net.ymate.platform.core.beans.annotation.Ignored;
import net.ymate.platform.core.support.IInitialization;

import java.util.Map;

/**
 * @Author: mengxiang.
 * @Date 2024/10/9.
 * @Time: 09:36.
 * @Description:
 */
@Ignored
public interface ISmsConfig extends IInitialization<ISms> {

    String ENABLED = "enabled";

    String CHANNEL = "channel";
    String TYPE = "type";
    String SECRET_ID = "secretId";
    String SECRET_KEY = "secretKey";
    String TX_APP_ID = "txAppId";
    String TX_REGION = "txRegion";
    String TX_SIGN_NAME = "txSignName";
    String TX_TEMPLATE_ID = "txTemplateId";
    String ALI_SIGN = "aliSign";
    String ALI_TEMPLATE_CODE = "aliTemplateCode";
    String ALI_ENDPOINT = "aliEndpoint";

    String DEFAULT_CHANNEL = "default";


    /**
     * 模块是否已启用, 默认值: true
     *
     * @return 返回false表示禁用
     */
    boolean isEnabled();

    /**
     * 适配器
     *
     * @return
     */
    Map<String, ISmsAdapter> smsAdapterMap();

    /**
     * 获取某一个适配器
     *
     * @param channel
     * @return
     */
    ISmsAdapter smsAdapter(String channel);


}
