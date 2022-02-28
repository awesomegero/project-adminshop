package de.alpharout.adminshop.listener;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Product;
import de.alpharout.adminshop.api.ProductType;
import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.api.gui.ItemComponent;
import de.alpharout.adminshop.api.gui.ViewComponent;
import de.alpharout.adminshop.gui.BuyViewComponent;
import de.alpharout.adminshop.gui.SellViewComponent;
import de.alpharout.adminshop.utils.Log;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent clickEvent) {
        if (clickEvent.getCurrentItem() == null) return;

        // Extract trader name and (if existing) page number from inventory name
        String traderName;
        String productTypeName;
        ProductType productType = null;
        int page = 0;
        if (clickEvent.getView().getTitle().contains(" | ")) {
            String[] args = clickEvent.getView().getTitle().split(" \\| ");
            traderName = args[0];
            productTypeName = args[1];

            String buyInvName = ChatColor.translateAlternateColorCodes(
                    '&',
                    AdminShop.getConfigManager().getMessagesConf().getString("buy-inv-name")
            );
            String sellInvName = ChatColor.translateAlternateColorCodes(
                    '&',
                    AdminShop.getConfigManager().getMessagesConf().getString("sell-inv-name")
            );
            if (productTypeName.equals(buyInvName)) {
                productType = ProductType.BUY_PRODUCT;
            } else if (productTypeName.equals(sellInvName)) {
                productType = ProductType.SELL_PRODUCT;
            }

            page = Integer.parseInt(args[2]) - 1;
        } else {
            traderName = clickEvent.getView().getTitle();
        }

        // Check if inventory name matches one of the ViewComponents
        Predicate<Trader> byInvName = trader -> ChatColor.translateAlternateColorCodes('&', trader.getDisplayName()).equals(
                traderName
        );
        Trader trader;

        try {
            trader = Trader.getTraderList().stream().filter(byInvName).findFirst().get();
        } catch (NoSuchElementException nsee) {
            return;
        }
        clickEvent.setCancelled(true);

        // Check if clicked item is an ItemComponent
        // TODO: Filter only ItemComponents of the specific ViewComponent (?)
        Predicate<ItemComponent> byDisplayName = itemComponent ->
                itemComponent.getItemStack().getItemMeta().getDisplayName().equals(clickEvent.getCurrentItem().getItemMeta().getDisplayName());

        ItemComponent itemComponent;
        try {
            itemComponent = ItemComponent.getItemComponentList().stream().filter(byDisplayName).findFirst().get();
            itemComponent.handleClick(clickEvent, trader);
            return;
        } catch (NoSuchElementException nsee) {

        }

        ProductType finalProductType = productType;
        Predicate<Product> byName = product ->
                ChatColor.translateAlternateColorCodes('&', product.getDisplayName()).equals(clickEvent.getCurrentItem().getItemMeta().getDisplayName())
                        && product.getProductType() == finalProductType;
        Product product;
        try {
            product = Product.getProductList().stream().filter(byName).findFirst().get();
            // TODO: Check if clicker is player
            product.processTransaction((Player) clickEvent.getWhoClicked());
        } catch (NoSuchElementException nsee) {
            Log.debug("No correct click handling could be executed!");
        }

        String nextPageDisplayName = ChatColor.translateAlternateColorCodes(
                '&',
                AdminShop.getConfigManager().getMessagesConf().getString("next-page-display-name")
        );
        String previousPageDisplayName = ChatColor.translateAlternateColorCodes(
                '&',
                AdminShop.getConfigManager().getMessagesConf().getString("previous-page-display-name")
        );
        if (clickEvent.getCurrentItem().getItemMeta().getDisplayName().equals(nextPageDisplayName)) {
            clickEvent.getWhoClicked().closeInventory();
            if (productType == ProductType.BUY_PRODUCT) {
                clickEvent.getWhoClicked().openInventory(ViewComponent.getFilledInventory(new BuyViewComponent(trader).getInventory(page + 1)));
            } else if (productType == ProductType.SELL_PRODUCT) {
                clickEvent.getWhoClicked().openInventory(ViewComponent.getFilledInventory(new SellViewComponent(trader).getInventory(page + 1)));
            }
            return;
        }
        if (clickEvent.getCurrentItem().getItemMeta().getDisplayName().equals(previousPageDisplayName)) {
            clickEvent.getWhoClicked().closeInventory();
            if (productType == ProductType.BUY_PRODUCT) {
                clickEvent.getWhoClicked().openInventory(ViewComponent.getFilledInventory(new BuyViewComponent(trader).getInventory(page - 1)));
            } else if (productType == ProductType.SELL_PRODUCT) {
                clickEvent.getWhoClicked().openInventory(ViewComponent.getFilledInventory(new SellViewComponent(trader).getInventory(page - 1)));
            }
            return;
        }
    }
}
