package com.mx.ymate.dev.support.mvc.i18n.validate;

import com.mx.ymate.dev.support.mvc.i18n.I18nHelper;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.validation.IValidator;
import net.ymate.platform.validation.ValidateContext;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.annotation.Validator;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 邮箱地址格式验证
 */
@Validator(VMxEmail.class)
public final class MxEmailValidator implements IValidator {

    private static final String REGEX_STR;

    static {
        REGEX_STR = System.getProperty("email.regex", "\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,8}$");
    }

    private static final String I18N_MESSAGE_KEY = "ymp.validation.email";

    private static final String I18N_MESSAGE_DEFAULT_VALUE = "{0} not a valid email address.";

    /**
     * @param email 邮件地址
     * @return 返回email字符串是否为合法邮件地址
     * @since 2.1.0
     */
    public static boolean validate(String email) {
        return StringUtils.trimToEmpty(email).matches(REGEX_STR);
    }

    @Override
    public ValidateResult validate(ValidateContext context) {
        Object paramValue = context.getParamValue();
        if (paramValue != null) {
            boolean matched = false;
            if (context.getParamValue().getClass().isArray()) {
                Object[] values = (Object[]) paramValue;
                for (Object pValue : values) {
                    matched = !validate(BlurObject.bind(pValue).toStringValue());
                    if (matched) {
                        break;
                    }
                }
            } else {
                matched = !validate(BlurObject.bind(paramValue).toStringValue());
            }
            if (matched) {
                VMxEmail ann = (VMxEmail) context.getAnnotation();
                return ValidateResult.builder(context, I18nHelper.getMsg(ann.i18nKey(), ann.msg()), I18N_MESSAGE_KEY, I18N_MESSAGE_DEFAULT_VALUE).matched(true).build();
            }
        }
        return null;
    }
}
