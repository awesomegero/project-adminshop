package de.alpharout.adminshop.commands.sub;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Subcommand;
import de.alpharout.adminshop.api.Trader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ListSubcommand implements Subcommand {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("adminshop.command.list")) {
            sender.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            AdminShop.getConfigManager().getMessagesConf().getString("no-perm-output")
                    )
            );
            return true;
        }
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes(
                        '&',
                        AdminShop.getConfigManager().getMessagesConf().getString("list-trader-output")
                )
        );

        for (Trader trader : Trader.getTraderList()) {
            sender.sendMessage(ChatColor.GRAY + " • " + ChatColor.RED + trader.getInternalName());
        }

        return true;
    }
}
