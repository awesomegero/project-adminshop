package de.alpharout.adminshop.listener;

import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.utils.Log;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class NPCRightClickListener implements Listener {
    @EventHandler
    public void onClick(NPCRightClickEvent clickEvent) {
        Predicate<Trader> byUUID = trader -> trader.getNpcUUID().equals(clickEvent.getNPC().getUniqueId());

        try {
            Trader trader = Trader.getTraderList().stream().filter(byUUID).findFirst().get();
            Log.debug("Clicked on registered NPC " + trader.getInternalName());
        } catch (NoSuchElementException nsee) {
            Log.debug("Wrong UUID (" + clickEvent.getNPC().getUniqueId() + ")");
        }
    }
}
