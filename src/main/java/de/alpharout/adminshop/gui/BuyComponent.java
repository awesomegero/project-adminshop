package de.alpharout.adminshop.gui;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.gui.ItemComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BuyComponent extends ItemComponent {
    @Override
    public ItemStack getItemStack() {
        String materialName = ChatColor.translateAlternateColorCodes(
                '&',
                AdminShop.getConfigManager().getMessagesConf().getString("buy-component-material")
        );
        String displayName = ChatColor.translateAlternateColorCodes(
                '&',
                AdminShop.getConfigManager().getMessagesConf().getString("buy-component-name")
        );
        Material material = Material.getMaterial(materialName);

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public void handleClick() {
        super.handleClick();
    }
}
