package de.alpharout.adminshop.listener;

import de.alpharout.adminshop.api.gui.ItemComponent;
import de.alpharout.adminshop.api.gui.ViewComponent;
import de.alpharout.adminshop.utils.Log;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent clickEvent) {
        if (clickEvent.getCurrentItem() == null) return;

        // Check if inventory name matches one of the ViewComponents
        Predicate<ViewComponent> byInvName = viewComponent -> viewComponent.getInventoryName().equals(clickEvent.getView().getTitle());
        ViewComponent viewComponent;
        try {
            viewComponent = ViewComponent.getViewComponents().stream().filter(byInvName).findFirst().get();
            Log.debug("Clicked on gui inventory.");
        } catch (NoSuchElementException nsee) {
            Log.debug("Clicked on not-gui inventory.");
            return;
        }

        // Check if clicked item is an ItemComponent
        // TODO: Filter only ItemComponents of the specific ViewComponent (?)
        Predicate<ItemComponent> byDisplayName = itemComponent ->
                itemComponent.getItemStack().getItemMeta().getDisplayName().equals(clickEvent.getCurrentItem().getItemMeta().getDisplayName());

        ItemComponent itemComponent;
        try {
            itemComponent = ItemComponent.getItemComponentList().stream().filter(byDisplayName).findFirst().get();
        } catch (NoSuchElementException nsee) {
            return;
        }

        itemComponent.handleClick(clickEvent);
    }
}
