package com.mx.ymate.sms.impl;

import com.mx.ymate.sms.ISms;
import com.mx.ymate.sms.ISmsConfig;
import com.mx.ymate.sms.adapter.ISmsAdapter;
import com.mx.ymate.sms.adapter.impl.AliSmsAdapter;
import com.mx.ymate.sms.adapter.impl.ChineseSmsAdapter;
import com.mx.ymate.sms.adapter.impl.TxSmsAdapter;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.configuration.IConfigReader;
import net.ymate.platform.core.configuration.impl.MapSafeConfigReader;
import net.ymate.platform.core.module.IModuleConfigurer;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: mengxiang.
 * @Date 2024/10/9.
 * @Time: 09:36.
 * @Description:
 */
public final class DefaultSmsConfig implements ISmsConfig {

    private boolean enabled;
    private final Map<String, ISmsAdapter> SMS_ADAPTER_MAP = new HashMap<>();


    private boolean initialized;


    public static DefaultSmsConfig create(IModuleConfigurer moduleConfigurer) {
        return new DefaultSmsConfig(moduleConfigurer);
    }


    private DefaultSmsConfig() {
    }

    private DefaultSmsConfig(IModuleConfigurer moduleConfigurer) {
        IConfigReader configReader = moduleConfigurer.getConfigReader();
        enabled = configReader.getBoolean(ENABLED, true);
        String[] channelList = StringUtils.split(configReader.getString(CHANNEL, DEFAULT_CHANNEL), "|");
        for (String channel : channelList) {
            if (SMS_ADAPTER_MAP.containsKey(channel)) {
                continue;
            }
            Map<String, String> smsConfigMap = configReader.getMap(String.format("%s.", channel));
            if (smsConfigMap.isEmpty()) {
                continue;
            }
            IConfigReader smsConfigReader = MapSafeConfigReader.bind(smsConfigMap);
            String type = smsConfigReader.getString(TYPE);
            if (StringUtils.isBlank(type)) {
                throw new RuntimeException("短信服务商不能为空");
            }
            ISmsAdapter smsAdapter;
            switch (type) {
                case TYPE_CHINESE:
                    smsAdapter = new ChineseSmsAdapter(smsConfigReader.getString(SECRET_ID), smsConfigReader.getString(SECRET_KEY));
                    break;
                case TYPE_TX:
                    smsAdapter = new TxSmsAdapter(smsConfigReader.getString(SECRET_ID), smsConfigReader.getString(SECRET_KEY), smsConfigReader.getString(TX_APP_ID), smsConfigReader.getString(TX_REGION), smsConfigReader.getString(TX_SIGN_NAME), smsConfigReader.getString(TX_TEMPLATE_ID));
                    break;
                case TYPE_ALI:
                    smsAdapter = new AliSmsAdapter(smsConfigReader.getString(SECRET_ID), smsConfigReader.getString(SECRET_KEY), smsConfigReader.getString(ALI_SIGN), smsConfigReader.getString(ALI_TEMPLATE_CODE), smsConfigReader.getString(ALI_ENDPOINT));
                    break;
                default:
                    smsAdapter = ClassUtils.impl(type, ISmsAdapter.class, getClass());
                    break;
            }
            if (smsAdapter == null) {
                continue;
            }
            SMS_ADAPTER_MAP.put(channel, smsAdapter);
        }
    }

    @Override
    public void initialize(ISms owner) throws Exception {
        if (!initialized) {
            initialized = true;
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Map<String, ISmsAdapter> smsAdapterMap() {
        return SMS_ADAPTER_MAP;
    }

    @Override
    public ISmsAdapter smsAdapter(String channel) {
        return SMS_ADAPTER_MAP.get(channel);
    }


}
