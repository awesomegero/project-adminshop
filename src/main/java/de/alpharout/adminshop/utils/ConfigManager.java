package de.alpharout.adminshop.utils;

import de.alpharout.adminshop.AdminShop;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConfigManager {
    private File databaseFile;
    private File messagesFile;
    private File traderFile;
    private YamlConfiguration databaseConf;
    private YamlConfiguration messagesConf;
    private YamlConfiguration traderConf;

    public void loadFiles() {
        databaseFile = new File(AdminShop.getInstance().getDataFolder(), "database.yml");
        messagesFile = new File(AdminShop.getInstance().getDataFolder(), "messages.yml");
        traderFile = new File(AdminShop.getInstance().getDataFolder(), "trader.yml");

        if (!databaseFile.exists()) {
            AdminShop.getInstance().saveResource("database.yml", false);
            Log.debug("Created database.yml file.");
        }
        if (!messagesFile.exists()) {
            AdminShop.getInstance().saveResource("messages.yml", false);
            Log.debug("Created messages.yml file.");
        }
        if (!traderFile.exists()) {
            AdminShop.getInstance().saveResource("trader.yml", false);
            Log.debug("Created trader.yml file.");
        }

        String currentLoadingConfig = "unknown";
        try {
            databaseConf = new YamlConfiguration();
            messagesConf = new YamlConfiguration();
            traderConf = new YamlConfiguration();

            currentLoadingConfig = databaseFile.getName();
            databaseConf.load(databaseFile);

            currentLoadingConfig = messagesFile.getName();
            messagesConf.load(messagesFile);

            currentLoadingConfig = traderFile.getName();
            traderConf.load(traderFile);
        } catch (FileNotFoundException e) {
            Log.critical("Configuration file for " + currentLoadingConfig + " hasn't been found!");
        } catch (IOException e) {
            Log.critical("Error while reading " + currentLoadingConfig + "!");
        } catch (InvalidConfigurationException e) {
            Log.critical("Invalid YAML code in " + currentLoadingConfig + "!");
        }
    }

    public File getDatabaseFile() {
        return databaseFile;
    }

    public File getMessagesFile() {
        return messagesFile;
    }

    public File getTraderFile() {
        return traderFile;
    }

    public YamlConfiguration getMessagesConf() {
        return messagesConf;
    }

    public YamlConfiguration getDatabaseConf() {
        return databaseConf;
    }

    public YamlConfiguration getTraderConf() {
        return traderConf;
    }
}
