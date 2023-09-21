package com.mx.ymate.dev.support.jdbc.cut;

import net.ymate.platform.core.persistence.base.EntityMeta;

public interface ITableHandler {

    /**
     * 是否要分表
     * @param entityMeta
     * @return
     */
    boolean isCutTable(EntityMeta entityMeta);

    /**
     * 构建表名
     * @param entityMeta
     * @return
     */
    String buildTableName(EntityMeta entityMeta);
}
