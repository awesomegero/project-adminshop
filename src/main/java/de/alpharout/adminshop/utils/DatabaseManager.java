package de.alpharout.adminshop.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Trader;

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

        // TODO: Improve error handling
        try {
            hikariDataSource = new HikariDataSource(hikariConfig);
            setupDatabase();
        } catch (HikariPool.PoolInitializationException exception ) {
            Log.error("Error connecting to database!");
        }
    }

    public void setupDatabase() {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = getConnection().prepareStatement(
                    "CREATE TABLE IF NOT EXISTS adminshop_traders (" +
                            "NpcUUID varchar(255)," +
                            "InternalName varchar(255)," +
                            "DisplayName varchar(255)," +
                            "SkinName varchar(255)," +
                            "SkinSignature varchar(1024)," +
                            "SkinTexture varchar(1024)" +
                            ");"
            );
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Log.critical("Error executing create table statement!");

            e.printStackTrace();
        }

        loadTrader();
    }

    public void loadTrader() {
        Log.debug("Loading Trader List.");
        Trader.loadTraderList();
    }

    public Connection getConnection()  {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            return null;
        }
    }
}
