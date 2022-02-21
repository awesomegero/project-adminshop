package de.alpharout.adminshop.api.gui;

import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.gui.BuyComponent;
import de.alpharout.adminshop.gui.SellComponent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ItemComponent {
    private static ArrayList<ItemComponent> itemComponentList = new ArrayList<>();

    public static ArrayList<ItemComponent> getItemComponentList() {
        return itemComponentList;
    }

    public static void addStandardComponents() {
        itemComponentList.add(new BuyComponent());
        itemComponentList.add(new SellComponent());
    }

    public static void addItemComponent(ItemComponent itemComponent) {
        itemComponentList.add(itemComponent);
    }

    public ItemStack getItemStack() {
        return null;
    }

    public void handleClick(InventoryClickEvent clickEvent, Trader trader) {

    }
}
