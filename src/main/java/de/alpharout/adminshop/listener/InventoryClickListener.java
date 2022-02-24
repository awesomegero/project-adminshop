package de.alpharout.adminshop.listener;

import de.alpharout.adminshop.api.Product;
import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.api.gui.ItemComponent;
import de.alpharout.adminshop.api.gui.ViewComponent;
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
        int page;
        if (clickEvent.getView().getTitle().contains(" - ")) {
            traderName = clickEvent.getView().getTitle().split(" - ")[0];
            page = Integer.parseInt(clickEvent.getView().getTitle().split(" - ")[1]) - 1;
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

        Predicate<Product> byName = product ->
                ChatColor.translateAlternateColorCodes('&', product.getDisplayName()).equals(clickEvent.getCurrentItem().getItemMeta().getDisplayName());
        Product product;
        try {
            product = Product.getProductList().stream().filter(byName).findFirst().get();
            // TODO: Check if clicker is player
            product.processTransaction((Player) clickEvent.getWhoClicked());
        } catch (NoSuchElementException nsee) {
            Log.debug("No correct click handling could be executed!");
        }
    }
}
