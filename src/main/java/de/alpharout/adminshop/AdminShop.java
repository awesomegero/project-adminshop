package de.alpharout.adminshop;

import de.alpharout.adminshop.api.ResetManager;
import de.alpharout.adminshop.api.SubcommandManager;
import de.alpharout.adminshop.api.gui.ItemComponent;
import de.alpharout.adminshop.api.gui.ViewComponent;
import de.alpharout.adminshop.commands.AdminshopCommand;
import de.alpharout.adminshop.commands.sub.*;
import de.alpharout.adminshop.listener.InventoryClickListener;
import de.alpharout.adminshop.listener.NPCRightClickListener;
import de.alpharout.adminshop.utils.ConfigManager;
import de.alpharout.adminshop.utils.DatabaseManager;
import de.alpharout.adminshop.utils.Log;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminShop extends JavaPlugin {
    private static AdminShop PLUGIN;
    private static boolean debugMode;

    private static ConfigManager configManager;
    private static SubcommandManager subcommandManager;
    private static DatabaseManager databaseManager;
    private static ResetManager resetManager;

    private static Economy economy;

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
        subcommandManager.registerSubcommand("add", new AddSubcommand());
        subcommandManager.registerSubcommand("setnpc", new SetnpcSubcommand());
        subcommandManager.registerSubcommand("removenpc", new RemovenpcSubcommand());
        subcommandManager.registerSubcommand("remove", new RemoveSubcommand());
        subcommandManager.registerSubcommand("edit", new EditSubcommand());

        // Check for Citizens
        // TODO: Relocate code to other file
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        if (!pluginManager.isPluginEnabled("Citizens")) {
            Log.critical("Citizens is not enabled on this server!");
            pluginManager.disablePlugin(this);
            return;
        }
        if (!pluginManager.isPluginEnabled("HeadDatabase")) {
            Log.critical("HeadDatabase is not enabled on this server!");
            pluginManager.disablePlugin(this);
            return;
        }
        if (!pluginManager.isPluginEnabled("Vault")) {
            Log.critical("Vault is not enabled on this server!");
            pluginManager.disablePlugin(this);
            return;
        }

        pluginManager.registerEvents(new InventoryClickListener(), this);
        pluginManager.registerEvents(new NPCRightClickListener(), this);

        ItemComponent.addStandardComponents();
        Log.debug("Added standard components.");

        ViewComponent.addStandardViewComponents();
        Log.debug("Added standard view components.");

        resetManager = new ResetManager();
        resetManager.initTimer();

        setupEconomy();

        Log.debug("Enabled Adminshop.");
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> registeredServiceProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (registeredServiceProvider == null) {
            Log.critical("Economy could not be initialized!");
        }
        economy = registeredServiceProvider.getProvider();
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

    public static ResetManager getResetManager() {
        return resetManager;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    public static Economy getEconomy() {
        return economy;
    }
}
