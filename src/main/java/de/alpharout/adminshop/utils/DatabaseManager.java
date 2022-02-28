package de.alpharout.adminshop.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Product;
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
        try {
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
            hikariConfig.addDataSourceProperty("initializationFailTimeout", 0);

            // TODO: Improve error handling
            try {
                hikariDataSource = new HikariDataSource(hikariConfig);
                setupDatabase();
            } catch (HikariPool.PoolInitializationException exception) {
                Log.error("Error connecting to database!");
            } catch (Exception e) {
                Log.error("Error connecting to database!");
            }
        } catch (Exception e) {

        }
    }

    public void setupDatabase() {
        PreparedStatement traderTableStatement;
        PreparedStatement productTableStatement;
        try {
            traderTableStatement = getConnection().prepareStatement(
                    "CREATE TABLE IF NOT EXISTS adminshop_traders (" +
                            "NpcUUID varchar(255)," +
                            "InternalName varchar(255)," +
                            "DisplayName varchar(255)," +
                            "SkinName varchar(255)," +
                            "SkinSignature varchar(1024)," +
                            "SkinTexture varchar(1024)" +
                            ");"
            );
            productTableStatement = getConnection().prepareStatement(
                    "CREATE TABLE IF NOT EXISTS adminshop_products (" +
                            "InternalName varchar(255)," +
                            "DisplayName varchar(255)," +
                            "MaterialName varchar(255)," +
                            "MaterialAmount int(255)," +
                            "Price double(64, 10)," +
                            "TraderName varchar(255)," +
                            "BuyLimit int(255)," +
                            "ProductType varchar(255)" +
                            ");"
            );
            traderTableStatement.executeUpdate();
            productTableStatement.executeUpdate();
        } catch (SQLException e) {
            Log.critical("Error executing create table statement!");

            e.printStackTrace();
        }

        Trader.loadTraderList();
        Product.loadProductList();
    }

    public Connection getConnection()  {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            return null;
        }
    }
}
