package com.mx.ymate.dev.support.mvc.i18n.validate;

import com.mx.ymate.dev.support.mvc.i18n.I18nHelper;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.validation.IValidator;
import net.ymate.platform.validation.ValidateContext;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.annotation.Validator;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Validator(VMxDataRange.class)
public final class MxDataRangeValidator implements IValidator {

    private static final String I18N_MESSAGE_KEY = "ymp.validation.data_range_invalid";

    private static final String I18N_MESSAGE_DEFAULT_VALUE = "{0} value is out of the data range.";

    /**
     * 判断paramValue是否被collection包含
     *
     * @param collection 取值范围集合
     * @param paramValue 待验证的值对象
     * @param ignoreCase 是否忽略大小写
     * @return 若paramValue包含于collection集合中则返回true
     * @since 2.1.0
     */
    public static boolean validate(Collection<String> collection, Object paramValue, boolean ignoreCase) {
        boolean contained = false;
        if (paramValue.getClass().isArray()) {
            Object[] values = (Object[]) paramValue;
            for (Object pValue : values) {
                contained = containsValue(collection, pValue, ignoreCase);
                if (contained) {
                    break;
                }
            }
        } else {
            contained = containsValue(collection, paramValue, ignoreCase);
        }
        return contained;
    }

    private static boolean containsValue(Collection<String> collection, Object paramValue, boolean ignoreCase) {
        String pValueStr = BlurObject.bind(paramValue).toStringValue();
        if (ignoreCase) {
            for (String value : collection) {
                if (StringUtils.equalsIgnoreCase(pValueStr, value)) {
                    return true;
                }
            }
        } else {
            return collection.contains(pValueStr);
        }
        return false;
    }

    @Override
    public ValidateResult validate(ValidateContext context) {
        Object paramValue = context.getParamValue();
        if (paramValue != null) {
            boolean matched;
            VMxDataRange vDataRange = (VMxDataRange) context.getAnnotation();
            IMxDataRangeValuesProvider provider = null;
            if (!vDataRange.providerClass().equals(IMxDataRangeValuesProvider.class)) {
                provider = context.getOwner().getBeanFactory().getBean(vDataRange.providerClass());
                if (provider == null) {
                    provider = ClassUtils.impl(vDataRange.providerClass(), IMxDataRangeValuesProvider.class);
                }
            }
            if (provider != null) {
                matched = !validate(provider.values(), paramValue, vDataRange.ignoreCase());
            } else {
                matched = !validate(Arrays.asList(vDataRange.value()), paramValue, vDataRange.ignoreCase());
            }
            if (matched) {
                return ValidateResult.builder(context, I18nHelper.getMsg(vDataRange.i18nKey(), vDataRange.msg()), I18N_MESSAGE_KEY, I18N_MESSAGE_DEFAULT_VALUE).matched(true).build();
            }
        }
        return null;
    }
}