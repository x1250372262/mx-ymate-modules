package com.mx.ymate.maven.util;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: mengxiang.
 * @create: 2021-09-30 09:21
 * @Description: 实体类 数据库字段转换
 */
public class ModelUtils {

    /**
     * 数据库字段转换成java字段
     *
     * @param propertyName
     * @return
     */
    public static String propertyNameToFieldName(String propertyName) {
        if (StringUtils.contains(propertyName, StrUtil.UNDERLINE)) {
            String[] wordArray = StringUtils.split(propertyName, StrUtil.UNDERLINE);
            if (wordArray != null) {
                if (wordArray.length > 1) {
                    StringBuilder returnBuilder = new StringBuilder();
                    for (String word : wordArray) {
                        returnBuilder.append(StringUtils.capitalize(word.toLowerCase()));
                    }
                    return returnBuilder.toString();
                }
                return StringUtils.capitalize(wordArray[0].toLowerCase());
            }
        }
        return propertyName;
    }

}
