package de.alpharout.adminshop;

import de.alpharout.adminshop.api.SubcommandManager;
import de.alpharout.adminshop.commands.AdminshopCommand;
import de.alpharout.adminshop.commands.sub.HelpSubcommand;
import de.alpharout.adminshop.utils.ConfigManager;
import de.alpharout.adminshop.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminShop extends JavaPlugin {
    private static AdminShop PLUGIN;
    private static boolean debugMode;

    private static ConfigManager configManager;
    private static SubcommandManager subcommandManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        PLUGIN = this;
        debugMode = getConfig().getBoolean("debug-mode");

        configManager = new ConfigManager();
        configManager.loadFiles();

        this.getCommand("adminshop").setExecutor(new AdminshopCommand());

        subcommandManager = new SubcommandManager();
        subcommandManager.registerSubcommand("help", new HelpSubcommand());

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        if (!pluginManager.isPluginEnabled("Citizens")) {
            Log.critical("Citizens is not enabled on this server!");
            pluginManager.disablePlugin(this);
            return;
        }

        Log.debug("Enabled Adminshop.");
    }

    public static AdminShop getInstance() {
        return PLUGIN;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static SubcommandManager getSubcommandManager() {
        return subcommandManager;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }
}
