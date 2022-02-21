package de.alpharout.adminshop.gui;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.api.gui.ViewComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

public class OverviewViewComponent extends ViewComponent {
    public OverviewViewComponent() {
        super.inventoryName = ChatColor.translateAlternateColorCodes(
                '&',
                AdminShop.getConfigManager().getMessagesConf().getString("overview-view-component-name")
        );
    }

    @Override
    public Inventory getInventory(Trader trader) {
        Inventory inventory = Bukkit.createInventory(null, 3*9, inventoryName);

        inventory.setItem(12, new BuyComponent().getItemStack());

        return inventory;
    }
}
