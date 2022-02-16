package de.alpharout.adminshop.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;

public class ConfigHelper {
    // Replaces '&' with colors in a list
    public static ArrayList<String> getColoredList(YamlConfiguration config, String key) {
        ArrayList<String> rawList = (ArrayList<String>) config.getStringList(key);

        for (int i = 0; i < rawList.size(); i++) {
            rawList.set(i, ChatColor.translateAlternateColorCodes(
                    '&',
                    rawList.get(i)
            ));
        }

        return rawList;
    }
}
