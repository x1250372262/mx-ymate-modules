package com.mx.ymate.dev.support.sort;


import cn.hutool.core.util.StrUtil;
import net.ymate.platform.webmvc.annotation.RequestParam;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @create: 2021-07-02 16:58
 * @Description: 排序dto
 */
public class SortDTO implements Serializable {


    /**
     * 排序字段 a,b,c
     */
    @RequestParam
    private String field;

    /**
     * 排序方式 desc,asc,asc  如果为空 则所有默认都是asc
     */
    @RequestParam
    private String order;


    public SortDTO() {
    }

    public SortBean toBean() throws Exception {
        if (StringUtils.isBlank(field)) {
            return new SortBean(false);
        }
        String[] fieldArray = field.split(StrUtil.COMMA);
        String[] orderArray;
        boolean orderBool = false;
        if (StringUtils.isNotBlank(order)) {
            orderBool = true;
            orderArray = order.split(StrUtil.COMMA);
        } else {
            orderArray = new String[]{"asc"};
        }
        return new SortBean(fieldArray, orderArray, true, orderBool);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }


    @Override
    public String toString() {
        return "SortDTO{" +
                "field='" + field + '\'' +
                ", order='" + order + '\'' +
                '}';
    }
}
