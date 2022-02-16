package de.alpharout.adminshop;

import de.alpharout.adminshop.utils.ConfigManager;
import de.alpharout.adminshop.utils.Log;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminShop extends JavaPlugin {
    private static AdminShop PLUGIN;
    private static boolean debugMode;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        PLUGIN = this;
        debugMode = getConfig().getBoolean("debug-mode");

        ConfigManager configManager = new ConfigManager();
        configManager.loadFiles();

        Log.debug("Enabled Adminshop.");
    }

    public static AdminShop getInstance() {
        return PLUGIN;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }
}
