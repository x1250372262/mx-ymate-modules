package com.mx.ymate.clickhouse.adapter;

import com.clickhouse.jdbc.ClickHouseDataSource;
import net.ymate.platform.persistence.jdbc.AbstractDatabaseDataSourceAdapter;
import net.ymate.platform.persistence.jdbc.annotation.DataSourceAdapter;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @Author: mengxiang.
 * @Date: 2023-06-16
 * @Time: 14:41
 * @Description: clickhouse数据源适配器
 */
@DataSourceAdapter("clickhouse")
public class ClickhouseDataSourceAdapter extends AbstractDatabaseDataSourceAdapter {

    private ClickHouseDataSource dataSource;

    private InputStream openInputStream() throws IOException {
        return getDataSourceConfigFileAsStream("clickhouse", getDataSourceConfig().getName());
    }

    @Override
    protected void doInitialize() throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = openInputStream()) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        }

        dataSource = new ClickHouseDataSource(getDataSourceConfig().getConnectionUrl(), properties);
    }

    @Override
    public void doClose() throws Exception {
        super.doClose();
    }

    @Override
    public Connection getConnection() throws Exception {
        return dataSource.getConnection(getDataSourceConfig().getUsername(), getDataSourceConfig().getPassword());
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}

