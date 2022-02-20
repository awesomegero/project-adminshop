package de.alpharout.adminshop;

import de.alpharout.adminshop.api.SubcommandManager;
import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.commands.AdminshopCommand;
import de.alpharout.adminshop.commands.sub.CreateSubcommand;
import de.alpharout.adminshop.commands.sub.HelpSubcommand;
import de.alpharout.adminshop.commands.sub.ListSubcommand;
import de.alpharout.adminshop.utils.ConfigManager;
import de.alpharout.adminshop.utils.DatabaseManager;
import de.alpharout.adminshop.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminShop extends JavaPlugin {
    private static AdminShop PLUGIN;
    private static boolean debugMode;

    private static ConfigManager configManager;
    private static SubcommandManager subcommandManager;
    private static DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        PLUGIN = this;

        saveDefaultConfig();
        debugMode = getConfig().getBoolean("debug-mode");

        // Load all files and configurations
        configManager = new ConfigManager();
        configManager.loadFiles();

        // Connect to database
        databaseManager = new DatabaseManager();
        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                databaseManager.loadDatabase();
            }
        });

        // Register main command and tab completer
        this.getCommand("adminshop").setExecutor(new AdminshopCommand());
        this.getCommand("adminshop").setTabCompleter(new AdminshopCommand());

        // Subcommand Manager for /adminshop
        subcommandManager = new SubcommandManager();
        subcommandManager.registerSubcommand("help", new HelpSubcommand());
        subcommandManager.registerSubcommand("create", new CreateSubcommand());
        subcommandManager.registerSubcommand("list", new ListSubcommand());

        // Check for Citizens
        // TODO: Relocate code to other file
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

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public static SubcommandManager getSubcommandManager() {
        return subcommandManager;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }
}
