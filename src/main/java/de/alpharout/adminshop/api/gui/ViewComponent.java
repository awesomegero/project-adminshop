package de.alpharout.adminshop.api.gui;

import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.gui.OverviewViewComponent;
import de.alpharout.adminshop.utils.Log;
import org.bukkit.inventory.Inventory;

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

    public String getInventoryName() {
        return inventoryName;
    }
}
