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
    private YamlConfiguration databaseConf;
    private YamlConfiguration messagesConf;

    public ConfigManager() {

    }

    public void loadFiles() {
        databaseFile = new File(AdminShop.getInstance().getDataFolder(), "database.yml");
        messagesFile = new File(AdminShop.getInstance().getDataFolder(), "messages.yml");

        if (!databaseFile.exists()) {
            AdminShop.getInstance().saveResource("database.yml", false);
            Log.debug("Created database.yml file.");
        }
        if (!messagesFile.exists()) {
            AdminShop.getInstance().saveResource("messages.yml", false);
            Log.debug("Created messages.yml file.");
        }

        String currentLoadingConfig = "unknown";
        try {
            databaseConf = new YamlConfiguration();
            messagesConf = new YamlConfiguration();

            currentLoadingConfig = databaseFile.getName();
            databaseConf.load(databaseFile);

            currentLoadingConfig = messagesFile.getName();
            messagesConf.load(messagesFile);
        } catch (FileNotFoundException e) {
            Log.critical("Configuration file for " + currentLoadingConfig + " hasn't been found!");
            return;
        } catch (IOException e) {
            Log.critical("Error while reading " + currentLoadingConfig + "!");
        } catch (InvalidConfigurationException e) {
            Log.critical("Invalid YAML code in " + currentLoadingConfig + "!");
        }
    }
}
