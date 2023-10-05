package ru.adel.deliveryapp.dao.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class CustomDataSourceConfig {
    private static final String USER = System.getProperty("jdbc.username", "your_default_username");
    private static final String PASSWORD = System.getProperty("jdbc.password", "your_default_password");
    private static final String URL = System.getProperty("jdbc.url", "jdbc:postgresql://localhost:5332/delivery_app_db");

    private static final HikariConfig config = new HikariConfig();
    private static  HikariDataSource dataSource;

    static {
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.setDriverClassName(org.postgresql.Driver.class.getName());
        config.setAutoCommit(false);
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(10);
        config.setPoolName("deliveryAppDbPool");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }
    public static void updateDataSourceProperties() {
        String user = System.getProperty("jdbc.username", "your_default_username");
        String password = System.getProperty("jdbc.password", "your_default_password");
        String url = System.getProperty("jdbc.url", "jdbc:postgresql://localhost:5332/delivery_app_db");

        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        if (dataSource != null) {
            dataSource.close();
        }

        dataSource = new HikariDataSource(config);
    }


    private CustomDataSourceConfig() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static DataSource getHikariDataSource() {
        return dataSource;
    }

}