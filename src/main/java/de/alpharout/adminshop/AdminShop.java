package de.alpharout.adminshop;

import de.alpharout.adminshop.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class AdminShop extends JavaPlugin {
    private static AdminShop plugin;
    private static boolean debugMode;

    private static File messagesFile;
    private static File databaseFile;
    private static YamlConfiguration messagesConf;
    private static YamlConfiguration databaseConf;

    PluginManager pluginManager;

    @Override
    public void onEnable() {
        // Load config.yml at first because plugin has to know if debug mode is enabled.
        saveDefaultConfig();
        debugMode = getConfig().getBoolean("debug-mode");

        plugin = this;
        pluginManager = Bukkit.getServer().getPluginManager();

        // Check if dependencies are found and enabled
        checkDependencies();
        Log.debug("Checked dependencies!");
        // Initialize config files
        initFiles();
        Log.debug("Initialized config files!");
    }

    @Override
    public void onDisable() {

    }

    private void initFiles() {
        messagesFile = new File(getDataFolder(), "messages.yml");
        databaseFile = new File(getDataFolder(), "database.yml");

        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
        if (!databaseFile.exists()) {
            saveResource("database.yml", false);
        }

        messagesConf = new YamlConfiguration();
        databaseConf = new YamlConfiguration();
        String currentFileName = "unspecified file";
        try {
            currentFileName = messagesFile.getName();
            messagesConf.load(messagesFile);

            currentFileName = databaseFile.getName();
            databaseConf.load(databaseFile);
        } catch (IOException e) {
            Log.critical("Couldn't load " + currentFileName + "!");
            pluginManager.disablePlugin(this);
        } catch (InvalidConfigurationException e) {
            Log.critical("Invalid YAML code in " + currentFileName + "!");
            pluginManager.disablePlugin(this);
        }
    }
    private void checkDependencies() {
        if (!pluginManager.isPluginEnabled("Citizens")) {
            Log.critical("Citizens is not found or disabled! Adminshop requires Citizens.");
            pluginManager.disablePlugin(this);
        }
    }

    public static AdminShop getInstance() {
        return plugin;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }
}
