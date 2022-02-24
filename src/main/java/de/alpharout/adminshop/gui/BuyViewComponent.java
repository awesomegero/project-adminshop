package de.alpharout.adminshop.gui;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Product;
import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.api.gui.ViewComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tsp.headdb.api.HeadAPI;

import java.util.ArrayList;

public class BuyViewComponent extends ViewComponent {
    public BuyViewComponent(Trader trader) {
        super(trader);
    }

    public Inventory getInventory(int page) {
        String inventoryName = ChatColor.translateAlternateColorCodes(
                '&',
                getTrader().getDisplayName()
        ) + " - " + (page + 1);
        Inventory inventory = Bukkit.createInventory(null, 6*9, inventoryName);

        int currentPosition = 11;
        if (getTrader().getBuyProductPages().size() != 0) {
            for (Product product : getTrader().getBuyProductPages().get(page)) {
                inventory.setItem(currentPosition, product.getItemStack());
                currentPosition++;

                if (currentPosition == 16) currentPosition = 20;
                if (currentPosition == 25) currentPosition = 29;
            }
        }
        if (page > 0) {
            int previousHeadID = AdminShop.getConfigManager().getMessagesConf().getInt("previous-page-head-id");
            String displayName = ChatColor.translateAlternateColorCodes(
                    '&',
                    AdminShop.getConfigManager().getMessagesConf().getString("previous-page-display-name")
            );
            ItemStack previousItemStack = HeadAPI.getHeadByID(previousHeadID).getMenuItem();
            ItemMeta itemMeta = previousItemStack.getItemMeta();
            itemMeta.setDisplayName(displayName);
            itemMeta.setLore(new ArrayList<>());
            inventory.setItem(47, previousItemStack);
        }


        inventory.setItem(49, new ResetDisplayComponent().getItemStack());

        if (getTrader().getBuyProductPages().size() > 1) {
            int nextHeadID = AdminShop.getConfigManager().getMessagesConf().getInt("next-page-head-id");
            String displayName = ChatColor.translateAlternateColorCodes(
                    '&',
                    AdminShop.getConfigManager().getMessagesConf().getString("next-page-display-name")
            );
            ItemStack nextItemStack = HeadAPI.getHeadByID(nextHeadID).getMenuItem();
            ItemMeta itemMeta = nextItemStack.getItemMeta();
            itemMeta.setDisplayName(displayName);
            itemMeta.setLore(new ArrayList<>());
            inventory.setItem(51, nextItemStack);
        }

        return inventory;
    }
}
