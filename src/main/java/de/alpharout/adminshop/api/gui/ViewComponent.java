package de.alpharout.adminshop.api.gui;

import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.gui.OverviewViewComponent;
import de.alpharout.adminshop.utils.Log;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ViewComponent {
    private static ArrayList<ViewComponent> viewComponentList = new ArrayList<>();

    public static ArrayList<ViewComponent> getViewComponents() {
        return viewComponentList;
    }

    public static void addView(ViewComponent viewComponent) {
        viewComponentList.add(viewComponent);
    }

    public static void addStandardViewComponents() {
        OverviewViewComponent overviewViewComponent = new OverviewViewComponent();
        Log.debug("Overview View Component Inventory Name: " + overviewViewComponent.inventoryName);
        viewComponentList.add(overviewViewComponent);
    }

    protected String inventoryName;

    public Inventory getInventory(Trader trader) {
        return null;
    }

    public Inventory getFilledInventory(Trader trader) {
        Inventory inventory = getInventory(trader);

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

    public String getInventoryName() {
        return inventoryName;
    }
}
