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
    String SECRET_ID = "secret_id";
    String SECRET_KEY = "secret_key";
    String TX_APP_ID = "tx_app_id";
    String TX_REGION = "tx_region";
    String TX_SIGN_NAME = "tx_sign_name";
    String TX_TEMPLATE_ID = "tx_template_id";
    String ALI_SIGN = "ali_sign";
    String ALI_TEMPLATE_CODE = "ali_template_code";
    String ALI_ENDPOINT = "ali_endpoint";

    String TYPE_CHINESE = "smschinese";
    String TYPE_TX = "tx";
    String TYPE_ALI = "ali";

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
