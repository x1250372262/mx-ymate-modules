package com.mx.ymate.dev.support.mvc.i18n.validate;

import net.ymate.platform.core.beans.annotation.Ignored;

import java.util.Collections;
import java.util.Set;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Ignored
public interface IMxDataRangeValuesProvider {

    /**
     * 获取数据取值范围集合
     *
     * @return 返回数据集合
     */
    default Set<String> values() {
        return Collections.emptySet();
    }
}
