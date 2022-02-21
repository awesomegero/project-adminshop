package de.alpharout.adminshop.api.gui;

import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.gui.OverviewViewComponent;
import de.alpharout.adminshop.utils.Log;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewComponent {
    private static ArrayList<ViewComponent> viewComponentList = new ArrayList<>();

    public static ArrayList<ViewComponent> getViewComponents() {
        return viewComponentList;
    }

    public static void addView(ViewComponent viewComponent) {
        viewComponentList.add(viewComponent);
    }

    public static void addStandardViewComponents() {
        OverviewViewComponent overviewViewComponent = new OverviewViewComponent(null);
        Log.debug("Overview View Component Inventory Name: " + overviewViewComponent.inventoryName);
        viewComponentList.add(overviewViewComponent);
    }

    protected String inventoryName;
    private Trader trader;

    public ViewComponent(Trader trader) {
        this.trader = trader;
    }

    public Inventory getInventory() {
        return null;
    }

    public Inventory getFilledInventory() {
        Inventory inventory = getInventory();

        for (int i = 0; i < inventory.getContents().length; i++) {
            if (inventory.getItem(i) == null) {
                ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(" ");
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(i, itemStack);
            }
        }

        return inventory;
    }

    public Trader getTrader() {
        return trader;
    }

    public String getInventoryName() {
        return inventoryName;
    }
}
