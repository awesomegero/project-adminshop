package de.alpharout.adminshop.gui;

import de.alpharout.adminshop.api.Product;
import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.api.gui.ViewComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

public class SellViewComponent extends ViewComponent {
    public SellViewComponent(Trader trader) {
        super(trader);
    }

    public Inventory getInventory(int page) {
        String inventoryName = ChatColor.translateAlternateColorCodes(
                '&',
                getTrader().getDisplayName()
        ) + " - " + (page + 1);
        Inventory inventory = Bukkit.createInventory(null, 6*9, inventoryName);

        int currentPosition = 11;
        if (getTrader().getSellProductPages().size() != 0) {
            for (Product product : getTrader().getSellProductPages().get(page)) {
                inventory.setItem(currentPosition, product.getItemStack());
                currentPosition++;

                if (currentPosition == 16) currentPosition = 20;
                if (currentPosition == 25) currentPosition = 29;
            }
        }

        inventory.setItem(49, new ResetDisplayComponent().getItemStack());

        return inventory;
    }
}
