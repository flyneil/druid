package com.alibaba.druid.bvt.pool.basic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.druid.mock.MockDriver;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.DruidDataSourceStatManager;

public class ConnectionTest extends TestCase {

    private MockDriver      driver;
    private DruidDataSource dataSource;

    protected void setUp() throws Exception {
        Assert.assertEquals(0, DruidDataSourceStatManager.getInstance().getDataSourceList().size());

        driver = new MockDriver();

        dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mock:xxx");
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
        dataSource.setFilters("stat");
    }

    protected void tearDown() throws Exception {
        dataSource.close();
        Assert.assertEquals(0, DruidDataSourceStatManager.getInstance().getDataSourceList().size());
    }

    public void test_prepare() throws Exception {
        Connection conn = dataSource.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT 1", ResultSet.FETCH_FORWARD, ResultSet.CONCUR_READ_ONLY);
        stmt.close();

        conn.close();
    }

    public void test_prepare2() throws Exception {
        Connection conn = dataSource.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT 1", ResultSet.FETCH_FORWARD, ResultSet.CONCUR_READ_ONLY,
                                                       ResultSet.HOLD_CURSORS_OVER_COMMIT);
        stmt.close();

        conn.close();
    }

    public void test_prepare3() throws Exception {
        Connection conn = dataSource.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT 1", new int[0]);
        stmt.close();

        conn.close();
    }

    public void test_prepare4() throws Exception {
        Connection conn = dataSource.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT 1", new String[0]);
        stmt.close();

        conn.close();
    }

    public void test_prepare5() throws Exception {
        Connection conn = dataSource.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT 1", Statement.RETURN_GENERATED_KEYS);
        stmt.close();

        conn.close();
    }

    public void test_prepareCall() throws Exception {
        Connection conn = dataSource.getConnection();

        PreparedStatement stmt = conn.prepareCall("SELECT 1");
        stmt.close();

        conn.close();
    }

    public void test_prepareCall1() throws Exception {
        Connection conn = dataSource.getConnection();

        PreparedStatement stmt = conn.prepareCall("SELECT 1", ResultSet.FETCH_FORWARD, ResultSet.CONCUR_READ_ONLY);
        stmt.close();

        conn.close();
    }

    public void test_prepareCall2() throws Exception {
        Connection conn = dataSource.getConnection();

        PreparedStatement stmt = conn.prepareCall("SELECT 1", ResultSet.FETCH_FORWARD, ResultSet.CONCUR_READ_ONLY,
                                                  ResultSet.HOLD_CURSORS_OVER_COMMIT);
        stmt.close();

        conn.close();
    }

    public void test_create() throws Exception {
        Connection conn = dataSource.getConnection();

        Statement stmt = conn.createStatement();
        stmt.close();

        conn.close();
    }

    public void test_create1() throws Exception {
        Connection conn = dataSource.getConnection();

        Statement stmt = conn.createStatement(ResultSet.FETCH_FORWARD, ResultSet.CONCUR_READ_ONLY);
        stmt.close();

        conn.close();
    }

    public void test_create2() throws Exception {
        Connection conn = dataSource.getConnection();

        Statement stmt = conn.createStatement(ResultSet.FETCH_FORWARD, ResultSet.CONCUR_READ_ONLY,
                                              ResultSet.HOLD_CURSORS_OVER_COMMIT);
        stmt.close();

        conn.close();
    }
}
