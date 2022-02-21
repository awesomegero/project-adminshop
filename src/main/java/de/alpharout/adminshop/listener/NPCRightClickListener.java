package de.alpharout.adminshop.listener;

import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.api.gui.ViewComponent;
import de.alpharout.adminshop.gui.BuyComponent;
import de.alpharout.adminshop.gui.OverviewViewComponent;
import de.alpharout.adminshop.utils.Log;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class NPCRightClickListener implements Listener {
    @EventHandler
    public void onClick(NPCRightClickEvent clickEvent) {
        Predicate<Trader> byUUID = trader -> trader.getNpcUUID().equals(clickEvent.getNPC().getUniqueId());

        try {
            Trader trader = Trader.getTraderList().stream().filter(byUUID).findFirst().get();
            Log.debug("Clicked on registered NPC " + trader.getInternalName());

            // TODO: Add real logic
            clickEvent.getClicker().openInventory(new OverviewViewComponent().getInventory(trader));
        } catch (NoSuchElementException nsee) {
            Log.debug("Wrong UUID (" + clickEvent.getNPC().getUniqueId() + ")");
        }
    }
}
