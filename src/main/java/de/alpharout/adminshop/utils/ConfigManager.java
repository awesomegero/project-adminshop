package de.alpharout.adminshop.utils;

import de.alpharout.adminshop.AdminShop;

import java.io.File;

public class ConfigManager {
    private File databaseFile;
    private File messagesFile;

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
    }
}
