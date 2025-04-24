package com.mx.ymate.dev.support.mvc.i18n.validate;

import com.mx.ymate.dev.support.mvc.i18n.I18nHelper;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.CodecUtils;
import net.ymate.platform.core.IApplication;
import net.ymate.platform.core.support.IInitialization;
import net.ymate.platform.validation.AbstractValidator;
import net.ymate.platform.validation.ValidateContext;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.annotation.Validator;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Validator(VMxRSAData.class)
public final class MxRSADataValidator extends AbstractValidator {

    private static final String I18N_MESSAGE_KEY = "ymp.validation.rsa_data_invalid";

    private static final String I18N_MESSAGE_DEFAULT_VALUE = "{0} invalid.";

    /**
     * @param owner         所属应用容器对象
     * @param providerClass 提供者类型
     * @return 加载RSA密钥提供者接口实现类
     * @since 2.1.2
     */
    public static IMxRSAKeyProvider getRSAKeyProvider(IApplication owner, Class<? extends IMxRSAKeyProvider> providerClass) {
        IMxRSAKeyProvider keyProvider = owner.getBeanFactory().getBean(providerClass);
        if (keyProvider == null) {
            // 若非默认值则不加载全局配置
            if (IMxRSAKeyProvider.class.equals(providerClass)) {
                keyProvider = ClassUtils.loadClass(IMxRSAKeyProvider.class);
            } else if (!providerClass.isInterface()) {
                keyProvider = ClassUtils.impl(providerClass, IMxRSAKeyProvider.class);
            }
        }
        // 判断是否需要初始化
        if (keyProvider instanceof IInitialization) {
            try {
                @SuppressWarnings("unchecked")
                IInitialization<IApplication> keyProviderInit = ((IInitialization<IApplication>) keyProvider);
                if (!keyProviderInit.isInitialized()) {
                    keyProviderInit.initialize(owner);
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return keyProvider;
    }

    /**
     * 对originalValue字符串内容采用RSA公钥加密
     *
     * @param keyProvider   RSA密钥数据提供者类
     * @param originalValue 字符串内容
     * @return 加密后的BASE64字符串
     * @throws Exception 可能产生的任何异常
     */
    public static String encryptStr(IMxRSAKeyProvider keyProvider, String originalValue) throws Exception {
        return CodecUtils.RSA.encryptPublicKey(originalValue, keyProvider.getPublicKey());
    }

    public static String encryptPrivateStr(IMxRSAKeyProvider keyProvider, String originalValue) throws Exception {
        return CodecUtils.RSA.encrypt(originalValue, keyProvider.getPrivateKey());
    }

    /**
     * 对str字符串内容采用RSA私钥解密
     *
     * @param keyProvider RSA密钥数据提供者类
     * @param str         字符串内容
     * @return 解密后的字符串内容
     * @throws Exception 可能产生的任何异常
     */
    public static String decryptStr(IMxRSAKeyProvider keyProvider, String str) throws Exception {
        return CodecUtils.RSA.decrypt(str, keyProvider.getPrivateKey());
    }

    public static String decryptPublicStr(IMxRSAKeyProvider keyProvider, String str) throws Exception {
        return CodecUtils.RSA.decryptPublicKey(str, keyProvider.getPublicKey());
    }

    @Override
    public ValidateResult validate(ValidateContext context) {
        VMxRSAData ann = (VMxRSAData) context.getAnnotation();
        Object paramValue = context.getParamValue();
        if (paramValue != null) {
            boolean matched = false;
            String value = getParamValue(paramValue, false);
            if (StringUtils.isNotBlank(value)) {
                IMxRSAKeyProvider keyProvider = getRSAKeyProvider(context.getOwner(), ann.providerClass());
                if (keyProvider == null) {
                    throw new NullArgumentException("providerClass");
                }
                try {
                    String originalValue = decryptStr(keyProvider, value);
                    setOriginValue(StringUtils.defaultIfBlank(ann.value(), context.getParamInfo().getName()), originalValue);
                } catch (Exception e) {
                    matched = true;
                }
            } else {
                matched = true;
            }
            if (matched) {
                return ValidateResult.builder(context, I18nHelper.getMsg(ann.i18nKey(), ann.msg()), I18N_MESSAGE_KEY, I18N_MESSAGE_DEFAULT_VALUE).matched(true).build();
            }
        }
        return null;
    }
}
