package de.alpharout.adminshop.gui;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.gui.ItemComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tsp.headdb.api.HeadAPI;

import java.util.ArrayList;

public class ResetDisplayComponent extends ItemComponent {
    @Override
    public ItemStack getItemStack() {
        int clockHeadID = AdminShop.getConfigManager().getMessagesConf().getInt("clock-head-id");
        ItemStack clockItemStack = HeadAPI.getHeadByID(clockHeadID).getMenuItem();
        ItemMeta clockItemMeta = clockItemStack.getItemMeta();
        String clockDisplayName = ChatColor.translateAlternateColorCodes(
                '&',
                AdminShop.getConfigManager().getMessagesConf().getString("clock-display-name")
        );
        String encodedDisplayName = StringUtils.replace(clockDisplayName, "%reset_time%", String.valueOf(AdminShop.getResetManager().getNextReset()));
        clockItemMeta.setDisplayName(encodedDisplayName);
        clockItemMeta.setLore(new ArrayList<>());
        clockItemStack.setItemMeta(clockItemMeta);

        return clockItemStack;
    }
}
