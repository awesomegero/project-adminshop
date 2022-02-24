package de.alpharout.adminshop.gui;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.api.gui.ItemComponent;
import de.alpharout.adminshop.api.gui.ViewComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
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
    public void handleClick(InventoryClickEvent clickEvent, Trader trader) {
        clickEvent.setCancelled(true);

        clickEvent.getWhoClicked().closeInventory();
        clickEvent.getWhoClicked().openInventory(ViewComponent.getFilledInventory(new BuyViewComponent(trader).getInventory(0)));
    }
}
