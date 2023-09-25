package ru.adel.deliveryapp.datasourse;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceHikariPostgreSQL {
    private static final  String USER = "postgres";

    private static final  String PASSWORD = "mamamama";

    private static final  String URL = "jdbc:postgresql://localhost:5332/delivery_app_db";


    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource dataSource;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DataSourceHikariPostgreSQL.class);

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

    private DataSourceHikariPostgreSQL() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static DataSource getHikariDataSource() {
        return dataSource;
    }

}