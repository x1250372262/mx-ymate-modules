package com.mx.ymate.dev.support.jdbc.cut;

import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.persistence.IShardingRule;
import net.ymate.platform.core.persistence.IShardingable;
import net.ymate.platform.core.persistence.base.EntityMeta;
import net.ymate.platform.core.persistence.base.Type;
import net.ymate.platform.persistence.jdbc.annotation.Dialect;
import net.ymate.platform.persistence.jdbc.dialect.AbstractDialect;

@Dialect(value = Type.DATABASE.MYSQL, driverClass = "com.mysql.jdbc.Driver")
public class MySQLDialect extends AbstractDialect {

    private ITableHandler tableHandler;

    public MySQLDialect() {
        super("`", "`");
    }

    @Override
    public String getName() {
        return Type.DATABASE.MYSQL;
    }

    @Override
    public String buildTableName(String prefix, EntityMeta entityMeta, IShardingable shardingable) {
        if (tableHandler == null) {
            tableHandler = YMP.get().getBeanFactory().getBean(ITableHandler.class);
        }
        String entityName = entityMeta.getEntityName();
        if (tableHandler.isCutTable(entityMeta)) {
            entityName = tableHandler.buildTableName(entityMeta);
        }
        IShardingRule shardingRule = null;
        if (shardingable != null && entityMeta.getShardingRule() != null) {
            shardingRule = ClassUtils.impl(entityMeta.getShardingRule(), IShardingRule.class);
        }
        return super.buildTableName(prefix, entityName, shardingRule, shardingable);
    }
}
