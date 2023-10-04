package ru.adel.deliveryapp.datasourse;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceHikariPostgreSQL {
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource dataSource;


    static {
        try (InputStream input = DataSourceHikariPostgreSQL.class.getClassLoader().getResourceAsStream("database.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.user"));
            config.setPassword(properties.getProperty("db.password"));
            config.setDriverClassName(org.postgresql.Driver.class.getName());
            config.setAutoCommit(false);
            config.setMinimumIdle(5);
            config.setMaximumPoolSize(10);
            config.setPoolName("deliveryAppDbPool");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataSource = new HikariDataSource(config);
    }


    private DataSourceHikariPostgreSQL() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static DataSource getHikariDataSource() {
        return dataSource;
    }

}