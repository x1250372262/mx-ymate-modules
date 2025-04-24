package com.mx.ymate.dev.support.mvc.i18n.validate;

import com.mx.ymate.dev.support.mvc.i18n.I18nHelper;
import net.ymate.platform.commons.MathCalcHelper;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.validation.IValidator;
import net.ymate.platform.validation.ValidateContext;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.annotation.Validator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 数值类型参数验证
 */
@Validator(VMxNumeric.class)
public final class MxNumericValidator implements IValidator {

    private static final String I18N_MESSAGE_KEY = "ymp.validation.numeric";

    private static final String I18N_MESSAGE_DEFAULT_VALUE = "{0} not a valid numeric.";

    private static final String I18N_MESSAGE_BETWEEN_KEY = "ymp.validation.numeric_between";

    private static final String I18N_MESSAGE_BETWEEN_DEFAULT_VALUE = "{0} numeric must be between {1} and {2}.";

    private static final String I18N_MESSAGE_DECIMALS_KEY = "ymp.validation.numeric_decimals";

    private static final String I18N_MESSAGE_DECIMALS_DEFAULT_VALUE = "{0} numeric must be keep {1} decimals.";

    private static final String I18N_MESSAGE_MAX_KEY = "ymp.validation.numeric_max";

    private static final String I18N_MESSAGE_MAX_DEFAULT_VALUE = "{0} numeric must be less than or equal to {1}.";

    private static final String I18N_MESSAGE_MIN_KEY = "ymp.validation.numeric_min";

    private static final String I18N_MESSAGE_MIN_DEFAULT_VALUE = "{0} numeric must be greater than or equal to {1}.";

    private static final String I18N_MESSAGE_EQ_KEY = "ymp.validation.numeric_eq";

    private static final String I18N_MESSAGE_EQ_DEFAULT_VALUE = "{0} numeric must be equal to {1}.";

    /**
     * @since 2.1.3
     */
    private static final String I18N_MESSAGE_DIGITS_KEY = "ymp.validation.numeric_digits";

    /**
     * @since 2.1.3
     */
    private static final String I18N_MESSAGE_DIGITS_DEFAULT_VALUE = "{0} not a valid digits.";

    /**
     * 验证paramValue是否为合法数值
     *
     * @param paramValue 待验证的值对象
     * @param min        最小数值（0为不限制）
     * @param max        最大数值（0为不限制）
     * @param decimals   小数位数（0为不限制）
     * @return 返回结果为0表示合法，为1表示不是有效的数值，为2表示小数位数超出范围，为3表示数值不在min和max之间，为4表示数值小于min值，为5表示数值大于max值，为6表示数值不相等
     * @since 2.1.0
     */
    public static int validate(Object paramValue, double min, double max, int decimals) {
        int result = 0;
        try {
            String numStr = BlurObject.bind(paramValue).toStringValue();
            Number number = NumberUtils.createNumber(numStr);
            if (number == null) {
                result = 1;
            } else {
                if (min != 0 && MathCalcHelper.eq(min, max) && number.doubleValue() != min) {
                    result = 6;
                } else {
                    boolean cond = min != 0 && max != 0 && (number.doubleValue() < min || number.doubleValue() > max);
                    if (cond) {
                        result = 3;
                    } else if (min != 0 && number.doubleValue() < min) {
                        result = 4;
                    } else if (max != 0 && number.doubleValue() > max) {
                        result = 5;
                    }
                }
                if (result == 0 && decimals > 0 && StringUtils.substringAfter(numStr, ".").length() > decimals) {
                    result = 2;
                }
            }
        } catch (NumberFormatException e) {
            result = 1;
        }
        return result;
    }

    /**
     * 验证paramValue是否由数字组成
     *
     * @param paramValue 待验证的值对象
     * @return 返回true表示值是由数字组成
     * @since 2.1.3
     */
    public static boolean validate(Object paramValue) {
        return StringUtils.isNumeric(BlurObject.bind(paramValue).toStringValue());
    }

    @Override
    public ValidateResult validate(ValidateContext context) {
        Object paramValue = context.getParamValue();
        if (paramValue != null) {
            VMxNumeric vNumeric = (VMxNumeric) context.getAnnotation();
            int result = 0;
            if (paramValue.getClass().isArray()) {
                Object[] values = (Object[]) paramValue;
                if (vNumeric.digits()) {
                    result = Arrays.stream(values).anyMatch(pValue -> !validate(pValue)) ? 7 : 0;
                } else {
                    for (Object pValue : values) {
                        result = validate(pValue, vNumeric.eq() != 0 ? vNumeric.eq() : vNumeric.min(), vNumeric.eq() != 0 ? vNumeric.eq() : vNumeric.max(), vNumeric.decimals());
                        if (result > 0) {
                            break;
                        }
                    }
                }
            } else {
                if (vNumeric.digits()) {
                    result = !validate(paramValue) ? 7 : 0;
                } else {
                    result = validate(paramValue, vNumeric.eq() != 0 ? vNumeric.eq() : vNumeric.min(), vNumeric.eq() != 0 ? vNumeric.eq() : vNumeric.max(), vNumeric.decimals());
                }
            }
            if (result > 0) {
                ValidateResult.Builder builder = ValidateResult.builder(context).matched(true);
                if (StringUtils.isNotBlank(vNumeric.msg())) {
                    return builder.msg(I18nHelper.getMsg(vNumeric.i18nKey(),vNumeric.msg())).build();
                }
                switch (result) {
                    case 2:
                        builder.msg(I18N_MESSAGE_DECIMALS_KEY, I18N_MESSAGE_DECIMALS_DEFAULT_VALUE, vNumeric.decimals());
                        break;
                    case 3:
                        builder.msg(I18N_MESSAGE_BETWEEN_KEY, I18N_MESSAGE_BETWEEN_DEFAULT_VALUE, vNumeric.min(), vNumeric.max());
                        break;
                    case 4:
                        builder.msg(I18N_MESSAGE_MIN_KEY, I18N_MESSAGE_MIN_DEFAULT_VALUE, vNumeric.min());
                        break;
                    case 5:
                        builder.msg(I18N_MESSAGE_MAX_KEY, I18N_MESSAGE_MAX_DEFAULT_VALUE, vNumeric.max());
                        break;
                    case 6:
                        builder.msg(I18N_MESSAGE_EQ_KEY, I18N_MESSAGE_EQ_DEFAULT_VALUE, vNumeric.eq());
                        break;
                    case 7:
                        builder.msg(I18N_MESSAGE_DIGITS_KEY, I18N_MESSAGE_DIGITS_DEFAULT_VALUE);
                        break;
                    default:
                        builder.msg(I18N_MESSAGE_KEY, I18N_MESSAGE_DEFAULT_VALUE);
                }
                return builder.build();
            }
        }
        return null;
    }
}
