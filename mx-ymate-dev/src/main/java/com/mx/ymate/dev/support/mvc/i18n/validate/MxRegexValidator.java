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
 * @Description: 正则表达式验证
 */
@Validator(VMxRegex.class)
public final class MxRegexValidator implements IValidator {

    private static final String I18N_MESSAGE_KEY = "ymp.validation.regex";

    private static final String I18N_MESSAGE_DEFAULT_VALUE = "{0} regex not match.";

    /**
     * 验证paramValue是否匹配regex正则
     *
     * @param regex      正则表达式
     * @param paramValue 待验证值对象
     * @return 若匹配则返回true
     * @since 2.1.0
     */
    public static boolean validate(String regex, Object paramValue) {
        boolean result = false;
        if (paramValue != null) {
            if (paramValue.getClass().isArray()) {
                Object[] values = (Object[]) paramValue;
                for (Object pValue : values) {
                    result = !StringUtils.trimToEmpty(BlurObject.bind(pValue).toStringValue()).matches(regex);
                    if (result) {
                        break;
                    }
                }
            } else {
                result = !StringUtils.trimToEmpty(BlurObject.bind(paramValue).toStringValue()).matches(regex);
            }
        }
        return result;
    }

    @Override
    public ValidateResult validate(ValidateContext context) {
        Object paramValue = context.getParamValue();
        if (paramValue != null) {
            VMxRegex ann = (VMxRegex) context.getAnnotation();
            boolean matched = validate(ann.regex(), paramValue);
            if (matched) {
                return ValidateResult.builder(context, I18nHelper.getMsg(ann.i18nKey(), ann.msg()), I18N_MESSAGE_KEY, I18N_MESSAGE_DEFAULT_VALUE).matched(true).build();
            }
        }
        return null;
    }
}
