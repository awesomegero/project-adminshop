package de.alpharout.adminshop.commands.sub;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Subcommand;
import de.alpharout.adminshop.api.Trader;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class RemovenpcSubcommand implements Subcommand {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("adminshop.command.removenpc")) {
            sender.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            AdminShop.getConfigManager().getMessagesConf().getString("no-perm-output")
                    )
            );
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            AdminShop.getConfigManager().getMessagesConf().getString("no-player-output")
                    )
            );
            return true;
        }
        Player player = (Player) sender;

        if (args.length != 2) {
            player.sendMessage("§cPlease use /adminshop removenpc <Trader>");
        }

        Predicate<Trader> byInternalName = trader -> trader.getInternalName().equalsIgnoreCase(args[1]);
        Trader trader;
        try {
            trader = Trader.getTraderList().stream().filter(byInternalName).findFirst().get();
        } catch (NoSuchElementException nsee) {
            player.sendMessage("§cThis trader does not exist!");
            return true;
        }

        NPC npc = CitizensAPI.getNPCRegistry().getByUniqueIdGlobal(trader.getNpcUUID());

        if (npc == null) {
            player.sendMessage("§cCouldn't find the NPC using the UUID. Have you removed the NPC?");
            return true;
        }

        npc.despawn();
        player.sendMessage("§cRemoved §7NPC.");

        return true;
    }
}
