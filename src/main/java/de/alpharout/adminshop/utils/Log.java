package de.alpharout.adminshop.utils;

import de.alpharout.adminshop.AdminShop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Log {
    // Normal information messages from the plugin
    public static void info(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.WHITE + "Info | " + msg);
    }

    // Unexpected behavior that doesn't impact the plugin (yet)
    public static void warn(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Warning | " + msg);
    }

    // Recoverable error, plugin is still (partially) working.
    public static void error(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error | " + msg);
    }

    // Unrecoverable error, normally disables the plugin completely.
    public static void critical(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Critical | " + msg);
    }

    // Used for development information and deeper analysis
    public static void debug(String msg) {
        if (AdminShop.isDebugMode()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "Debug | " + msg);
        }
    }
}
