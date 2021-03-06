package com.alibaba.druid.bvt.filter;

import java.sql.SQLException;
import java.util.Properties;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.druid.filter.FilterChainImpl;
import com.alibaba.druid.filter.trace.TraceFilter;
import com.alibaba.druid.mock.MockConnection;
import com.alibaba.druid.mock.MockDriver;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl;
import com.alibaba.druid.stat.DruidDataSourceStatManager;
import com.alibaba.druid.stat.JdbcStatContext;
import com.alibaba.druid.stat.JdbcStatManager;

public class TraceFilterTest_close_error extends TestCase {

    private MockDriver      driver;
    private DruidDataSource dataSource;
    private TraceFilter     filter;

    protected void setUp() throws Exception {
        Assert.assertEquals(0, DruidDataSourceStatManager.getInstance().getDataSourceList().size());

        driver = new MockDriver() {

        };

        dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mock:TraceFilterTest_close_error");
        dataSource.setDriver(driver);
        dataSource.setInitialSize(1);
        dataSource.setMaxActive(2);
        dataSource.setMaxIdle(2);
        dataSource.setMinIdle(1);
        dataSource.setMinEvictableIdleTimeMillis(300 * 1000); // 300 / 10
        dataSource.setTimeBetweenEvictionRunsMillis(180 * 1000); // 180 / 10
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setFilters("trace");

        filter = (TraceFilter) dataSource.getProxyFilters().get(0);
        JdbcStatContext statContext = new JdbcStatContext();
        statContext.setTraceEnable(true);
        JdbcStatManager.getInstance().setStatContext(statContext);
    }

    protected void tearDown() throws Exception {
        dataSource.close();
        Assert.assertEquals(0, DruidDataSourceStatManager.getInstance().getDataSourceList().size());
        JdbcStatManager.getInstance().setStatContext(null);
    }

    public void test_close_error() throws Exception {
        FilterChainImpl chain = new FilterChainImpl(dataSource);

        MockConnection conn = new MockConnection() {

            public void close() throws SQLException {
                throw new SQLException();
            }
        };

        {
            SQLException error = null;
            try {
                filter.connection_close(chain, new ConnectionProxyImpl(dataSource, conn, new Properties(), 0));
            } catch (SQLException ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }

    }

    public void test_close_error_2() throws Exception {
        FilterChainImpl chain = new FilterChainImpl(dataSource);

        MockConnection conn = new MockConnection() {

            public void close() throws SQLException {
                throw new RuntimeException();
            }
        };

        {
            Exception error = null;
            try {
                filter.connection_close(chain, new ConnectionProxyImpl(dataSource, conn, new Properties(), 0));
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }

    }
}
