package com.mx.ymate.clickhouse.dialet;


import net.ymate.platform.persistence.jdbc.annotation.Dialect;
import net.ymate.platform.persistence.jdbc.dialect.AbstractDialect;

/**
 * @Author: mengxiang.
 * @Date: 2023-06-16
 * @Time: 14:41
 * @Description: clickhouse方言实现
 */
@Dialect(value = "CLICKHOUSE", driverClass = "com.clickhouse.jdbc.ClickHouseDriver")
public class ClickhouseDialect extends AbstractDialect {

    public ClickhouseDialect() {
        super("`", "`");
    }

    @Override
    public String getName() {
        return "CLICKHOUSE";
    }
}
