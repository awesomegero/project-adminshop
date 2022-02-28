package de.alpharout.adminshop.gui;

import de.alpharout.adminshop.api.Product;
import de.alpharout.adminshop.api.ProductType;
import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.api.gui.ViewComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

import java.util.NoSuchElementException;
import java.util.function.Predicate;

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

        Predicate<Product> byBuyProductLeft = product -> (product.getBuyLimit() - product.getCurrentlySold()) > 0
                && product.getProductType() == ProductType.BUY_PRODUCT;
        Predicate<Product> bySellProductLeft = product -> (product.getBuyLimit() - product.getCurrentlySold()) > 0
                && product.getProductType() == ProductType.SELL_PRODUCT;
        Product product;
        try {
            product = getTrader().getProductList().stream().filter(byBuyProductLeft).findFirst().get();
            inventory.setItem(12, new BuyComponent().getItemStack());
        } catch (NoSuchElementException nsee) {
            inventory.setItem(12, new NoProductsComponent().getItemStack());
        }

        try {
            product = getTrader().getProductList().stream().filter(bySellProductLeft).findFirst().get();
            inventory.setItem(14, new SellComponent().getItemStack());
        } catch (NoSuchElementException nsee) {
            inventory.setItem(14, new NoProductsComponent().getItemStack());
        }

        return inventory;
    }
}
