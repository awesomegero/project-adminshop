package de.alpharout.adminshop.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.alpharout.adminshop.AdminShop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private HikariConfig hikariConfig;
    private HikariDataSource hikariDataSource;

    public DatabaseManager() {
        hikariConfig = new HikariConfig();
    }

    public void loadDatabase() {
        String jdbcUrl =
                "jdbc:mysql://" +
                AdminShop.getConfigManager().getDatabaseConf().getString("db.hostname") +
                ":" + AdminShop.getConfigManager().getDatabaseConf().getInt("db.port") +
                "/" + AdminShop.getConfigManager().getDatabaseConf().getString("db.database");
        String username = AdminShop.getConfigManager().getDatabaseConf().getString("db.username");
        String password = AdminShop.getConfigManager().getDatabaseConf().getString("db.password");

        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // TODO: Inform about most efficient properties
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}
