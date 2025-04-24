package com.mx.ymate.dev.support.mvc.i18n.validate;

import com.mx.ymate.dev.support.mvc.i18n.I18nHelper;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.validation.IValidator;
import net.ymate.platform.validation.ValidateContext;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.annotation.Validator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 必填项验证
 */
@Validator(VMxRequired.class)
public final class MxRequiredValidator implements IValidator {

    private static final String I18N_MESSAGE_KEY = "ymp.validation.required";

    private static final String I18N_MESSAGE_DEFAULT_VALUE = "{0} is required.";

    /**
     * 验证paramValue值是否不空
     *
     * @param paramValue 待验证值对象
     * @return 若为空则返回true
     * @since 2.1.0
     */
    public static boolean validate(Object paramValue) {
        boolean result;
        if (paramValue == null) {
            result = true;
        } else if (paramValue.getClass().isArray()) {
            result = ArrayUtils.isEmpty((Object[]) paramValue) || Arrays.stream(((Object[]) paramValue)).anyMatch(o -> StringUtils.isBlank(BlurObject.bind(o).toStringValue()));
        } else {
            result = StringUtils.isBlank(BlurObject.bind(paramValue).toStringValue());
        }
        return result;
    }

    @Override
    public ValidateResult validate(ValidateContext context) {
        boolean matched = validate(context.getParamValue());
        if (matched) {
            VMxRequired ann = (VMxRequired) context.getAnnotation();
            return ValidateResult.builder(context, I18nHelper.getMsg(ann.i18nKey(), ann.msg()), I18N_MESSAGE_KEY, I18N_MESSAGE_DEFAULT_VALUE).matched(true).build();
        }
        return null;
    }
}
