package de.alpharout.adminshop.utils;

import de.alpharout.adminshop.AdminShop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Log {
    public static void info(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.WHITE + "Info | " + msg);
    }

    public static void error(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error | " + msg);
    }

    public static void critical(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Critical | " + msg);
    }

    public static void debug(String msg) {
        if (AdminShop.isDebugMode()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "Debug | " + msg);
        }
    }
}
