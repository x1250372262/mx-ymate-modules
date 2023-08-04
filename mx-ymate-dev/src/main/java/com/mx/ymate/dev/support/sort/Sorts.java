package com.mx.ymate.dev.support.sort;

import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.persistence.Fields;
import net.ymate.platform.persistence.jdbc.query.OrderBy;

/**
 * @Author: 徐建鹏.
 * @create: 2022-08-16 11:54
 * @Description: 排序工具类
 */
public class Sorts {

    public static final String DESC = "desc";

    /**
     * 创建排序
     *
     * @param prefixArray 排序和fieldArray对应
     * @param sortBean
     * @return
     */
    public static OrderBy create(String[] prefixArray, SortBean sortBean) {
        OrderBy orderBy = OrderBy.create();
        if (!sortBean.isSort()) {
            return orderBy;
        }
        String[] fieldArray = sortBean.getFieldArray();
        String[] orderArray = sortBean.getOrderArray();
        for (int i = 0; i < fieldArray.length; i++) {
            String order = sortBean.isOrder() ? orderArray[i] : orderArray[0];
            if (DESC.equals(order)) {
                orderBy.desc(Fields.field(prefixArray[i], ClassUtils.fieldNameToPropertyName(fieldArray[i],0)));
            } else {
                orderBy.asc(Fields.field(prefixArray[i], ClassUtils.fieldNameToPropertyName(fieldArray[i],0)));
            }
        }
        return orderBy;
    }
}
