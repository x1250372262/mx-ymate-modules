package com.mx.ymate.dev.support.mvc.i18n.validate;

import com.mx.ymate.dev.support.mvc.i18n.I18nHelper;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.validation.IValidator;
import net.ymate.platform.validation.ValidateContext;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.annotation.Validator;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 日期字符串参数验证
 */
@Validator(VMxDateTime.class)
public final class MxDateTimeValidator implements IValidator {

    private static final String I18N_MESSAGE_KEY = "ymp.validation.datetime";

    private static final String I18N_MESSAGE_DEFAULT_VALUE = "{0} not a valid datetime.";

    private static final String I18N_MESSAGE_MAX_DAYS_KEY = "ymp.validation.datetime_max_days";

    private static final String I18N_MESSAGE_MAX_DAYS_DEFAULT_VALUE = "{0} has exceeded the max days.";

    /**
     * 根据paramValue字符串创建时间日期对象
     *
     * @param paramValue 参数值
     * @return 返回日期对象，若非法则返回null
     * @since 2.1.0
     */
    public static Date parseDate(String paramValue) {
        return parseDate(paramValue, DateTimeUtils.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 验证paramValue字符串是否为合法的时间戳
     *
     * @param paramValue 参数值
     * @param pattern    日期时间格式
     * @return 返回日期对象，若非法则返回null
     * @since 2.1.0
     */
    public static Date parseDate(String paramValue, String pattern) {
        try {
            if (StringUtils.isNumeric(paramValue)) {
                return new Date(Long.parseLong(paramValue));
            }
            return DateTimeUtils.parseDateTime(paramValue, StringUtils.defaultIfBlank(pattern, DateTimeUtils.YYYY_MM_DD_HH_MM_SS));
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * @param paramName  自定参数名称
     * @param paramValue 待验证参数值对象（支持使用 today|yesterday|week|month|year 常量）
     * @param pattern    日期格式字符串
     * @param separator  时间段字符串之间的分割符号
     * @param maxDays    时间段之间的天数最大差值，默认为0表示不限制
     * @param single     仅接收单日期(即所选日期的00点00分00秒0毫秒到所选日期的23点59分59秒0毫秒)
     * @return 返回结果为0表示合法，为1表示日期字符串格式无效，为2表示时间段差值超过限定天数，为3表示开始日期时间大于结束日期时间
     */
    public static int validate(String paramName, String paramValue, String pattern, String separator, int maxDays, boolean single) {
        MxDateTimeValue dateTimeValue = MxDateTimeValue.parse(paramValue, pattern, separator, single);
        int result = validate(dateTimeValue, maxDays, single);
        if (dateTimeValue != null && result == 0 && StringUtils.isNotBlank(paramName)) {
            ValidateContext.getLocalAttributes().put(paramName, dateTimeValue);
        }
        return result;
    }

    /**
     * @param dateTimeValue 日期时间值对象
     * @param maxDays       时间段之间的天数最大差值，默认为0表示不限制
     * @param single        仅接收单日期(即所选日期的00点00分00秒0毫秒到所选日期的23点59分59秒0毫秒)
     * @return 返回结果为0表示合法，为1表示日期字符串格式无效，为2表示时间段差值超过限定天数，为3表示开始日期时间大于结束日期时间
     * @since 2.1.3
     */
    public static int validate(MxDateTimeValue dateTimeValue, int maxDays, boolean single) {
        int result = 0;
        if (dateTimeValue == null) {
            result = 1;
        } else if (!single) {
            if (dateTimeValue.getStartDateTimeMillis() > dateTimeValue.getEndDateTimeMillis()) {
                result = 3;
            } else if (maxDays > 0) {
                long days = dateTimeValue.getMaxDays();
                if (days < 0 || days > maxDays) {
                    result = 2;
                }
            }
        }
        return result;
    }

    @Override
    public ValidateResult validate(ValidateContext context) {
        Object paramValue = context.getParamValue();
        if (paramValue != null) {
            VMxDateTime vDateTime = (VMxDateTime) context.getAnnotation();
            int result = 0;
            String paramName = StringUtils.defaultIfBlank(vDateTime.value(), context.getParamInfo().getName());
            if (context.getParamValue().getClass().isArray()) {
                Object[] values = (Object[]) paramValue;
                for (Object pValue : values) {
                    String pValueStr = BlurObject.bind(pValue).toStringValue();
                    result = validate(paramName, pValueStr, vDateTime.pattern(), vDateTime.separator(), vDateTime.maxDays(), vDateTime.single());
                    if (result > 0) {
                        break;
                    }
                }
            } else {
                if (paramValue instanceof MxDateTimeValue) {
                    result = validate((MxDateTimeValue) paramValue, vDateTime.maxDays(), vDateTime.single());
                } else {
                    String pValueStr = BlurObject.bind(paramValue).toStringValue();
                    result = validate(paramName, pValueStr, vDateTime.pattern(), vDateTime.separator(), vDateTime.maxDays(), vDateTime.single());
                }
            }
            if (result > 0) {
                ValidateResult.Builder builder = ValidateResult.builder(context).matched(true);
                if (StringUtils.isNotBlank(vDateTime.msg())) {
                    return builder.msg(I18nHelper.getMsg(vDateTime.i18nKey(),vDateTime.msg())).build();
                }
                switch (result) {
                    case 2:
                    case 3:
                        builder.msg(I18N_MESSAGE_MAX_DAYS_KEY, I18N_MESSAGE_MAX_DAYS_DEFAULT_VALUE);
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
