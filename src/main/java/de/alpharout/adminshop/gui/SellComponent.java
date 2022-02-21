package de.alpharout.adminshop.gui;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.api.gui.ItemComponent;
import de.alpharout.adminshop.utils.Log;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SellComponent extends ItemComponent {
    @Override
    public ItemStack getItemStack() {
        String materialName = AdminShop.getConfigManager().getMessagesConf().getString("sell-component-material");
        ItemStack itemStack = new ItemStack(Material.getMaterial(materialName));
        ItemMeta itemMeta = itemStack.getItemMeta();
        String displayName = ChatColor.translateAlternateColorCodes(
                '&',
                AdminShop.getConfigManager().getMessagesConf().getString("sell-component-name")
        );
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public void handleClick(InventoryClickEvent clickEvent, Trader trader) {
        clickEvent.setCancelled(true);
    }
}
