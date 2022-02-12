package de.alpharout.adminshop;

import de.alpharout.adminshop.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class AdminShop extends JavaPlugin {
    private static AdminShop plugin;
    private static boolean debugMode;

    private static File messagesFile;
    private static YamlConfiguration messagesConf;

    @Override
    public void onEnable() {
        plugin = this;

        // Initialize config files
        initFiles();
    }

    @Override
    public void onDisable() {

    }

    private void initFiles() {
        messagesFile = new File(getDataFolder(), "messages.yml");

        saveDefaultConfig();
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }

        messagesConf = new YamlConfiguration();
        String currentFileName = "unspecified file";
        try {
            currentFileName = messagesFile.getName();
            messagesConf.load(messagesFile);
        } catch (IOException e) {
            Log.critical("Couldn't load " + currentFileName + "!");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        } catch (InvalidConfigurationException e) {
            Log.critical("Invalid YAML code in " + currentFileName + "!");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        }
    }

    public static AdminShop getInstance() {
        return plugin;
    }
}
