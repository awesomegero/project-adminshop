package de.alpharout.adminshop.api;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.utils.Log;
import org.bukkit.Bukkit;

import java.util.Date;

public class ResetManager {
    private int nextReset;

    public ResetManager() {

    }

    public void initTimer() {
        // TODO: Implememt a more efficient way
        Date date = new Date();

        nextReset = date.getHours() + 1;
        int resetInterval = AdminShop.getInstance().getConfig().getInt("cooldown-timer");
        while ((nextReset % resetInterval) != 0) {
            nextReset++;
            if (nextReset > 24) nextReset = 0;
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(AdminShop.getInstance(), new Runnable() {
            @Override
            public void run() {
                Date date = new Date();
                if ((date.getHours() % 3) == 0 && date.getMinutes() == 0) {
                    Log.debug("Refilled products!");
                    refillProducts();
                }
            }
        }, 0, 20*60);
    }

    public void refillProducts() {
        for (Product product : Product.getProductList()) {
            product.setCurrentlySold(0);
        }
    }

    public int getNextReset() {
        return nextReset;
    }
}
