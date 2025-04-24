package com.mx.ymate.dev.support.mvc.i18n.validate;

import com.mx.ymate.dev.support.mvc.i18n.I18nHelper;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.validation.IValidator;
import net.ymate.platform.validation.ValidateContext;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.annotation.Validator;
import org.apache.commons.lang3.StringUtils;

/**
 * 目前匹配号段:<br>
 * - 中国电信号段: 133、153、173、177、180、181、189、190、191、193、199<br>
 * - 中国联通号段: 130、131、132、145、155、156、166、167、171、175、176、185、186、196<br>
 * - 中国移动号段: 134(0-8)、135、136、137、138、139、1440、147、148、150、151、152、157、158、159、172、178、182、183、184、187、188、195、197、198<br>
 * - 中国广电号段: 192<br>
 * - 其他号段: 14号段部分为上网卡专属号段：中国联通145，中国移动147，中国电信149<br>
 * - 虚拟运营商<br>
 * + 电信：1700、1701、1702、162<br>
 * + 移动：1703、1705、1706、165<br>
 * + 联通：1704、1707、1708、1709、171、167<br>
 * + 卫星通信：1349、174<br>
 * + 物联网：140、141、144、146、148<br>
 *
 * @Author: mengxiang.
 * @Date: 2025-04-24
 * @Time: 11:00
 * @Description: 手机号码验证
 */
@Validator(VMxMobile.class)
public final class MxMobileValidator implements IValidator {

    private static final String REGEX_STR;

    static {
        // 手机号段可能随时变化，允许通过系统参数重设！
        REGEX_STR = System.getProperty("mobile.regex", "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");
    }

    private static final String I18N_MESSAGE_KEY = "ymp.validation.mobile";

    private static final String I18N_MESSAGE_DEFAULT_VALUE = "{0} not a valid mobile phone number.";

    /**
     * @param mobile 手机号码
     * @return 返回mobile字符串是否为合法手机号码
     * @since 2.1.0
     */
    public static boolean validate(String mobile) {
        return StringUtils.trimToEmpty(mobile).matches(REGEX_STR);
    }

    /**
     * @param mobile 手机号码
     * @param regex  自定义正则表达式, 若为空则使用默认值
     * @return 返回mobile字符串是否为合法手机号码
     * @since 2.1.0
     */
    public static boolean validate(String mobile, String regex) {
        return StringUtils.trimToEmpty(mobile).matches(StringUtils.defaultIfBlank(regex, REGEX_STR));
    }

    @Override
    public ValidateResult validate(ValidateContext context) {
        Object paramValue = context.getParamValue();
        if (paramValue != null) {
            boolean matched = false;
            VMxMobile ann = (VMxMobile) context.getAnnotation();
            String regex = StringUtils.trimToNull(ann.regex());
            if (context.getParamValue().getClass().isArray()) {
                Object[] values = (Object[]) paramValue;
                for (Object pValue : values) {
                    matched = !validate(BlurObject.bind(pValue).toStringValue(), regex);
                    if (matched) {
                        break;
                    }
                }
            } else {
                matched = !validate(BlurObject.bind(paramValue).toStringValue(), regex);
            }
            if (matched) {
                return ValidateResult.builder(context, I18nHelper.getMsg(ann.i18nKey(), ann.msg()), I18N_MESSAGE_KEY, I18N_MESSAGE_DEFAULT_VALUE).matched(true).build();
            }
        }
        return null;
    }
}
