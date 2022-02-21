package de.alpharout.adminshop.gui;

import de.alpharout.adminshop.api.Product;
import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.api.gui.ViewComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

public class BuyViewComponent extends ViewComponent {
    public BuyViewComponent(Trader trader) {
        super(trader);
    }

    public Inventory getInventory(int page) {
        String inventoryName = ChatColor.translateAlternateColorCodes(
                '&',
                getTrader().getDisplayName()
        );
        Inventory inventory = Bukkit.createInventory(null, 6*9, inventoryName);

        int currentPosition = 11;
        for (Product product : getTrader().getProductPages().get(page)) {
            inventory.setItem(currentPosition, product.getItemStack());
            currentPosition++;

            if (currentPosition == 16) currentPosition = 20;
            if (currentPosition == 25) currentPosition = 29;
        }

        return inventory;
    }
}
