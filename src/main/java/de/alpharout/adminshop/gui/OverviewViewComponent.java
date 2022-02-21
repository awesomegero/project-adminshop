package de.alpharout.adminshop.gui;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.api.gui.ViewComponent;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

public class OverviewViewComponent extends ViewComponent {
    public OverviewViewComponent(Trader trader) {
        super(trader);
    }

    @Override
    public Inventory getInventory() {
        String invName = ChatColor.translateAlternateColorCodes(
                '&',
                getTrader().getDisplayName()
        );
        Inventory inventory = Bukkit.createInventory(null, 3*9, invName);

        inventory.setItem(12, new BuyComponent().getItemStack());
        inventory.setItem(14, new SellComponent().getItemStack());

        return inventory;
    }
}
