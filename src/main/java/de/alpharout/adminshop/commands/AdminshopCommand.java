package de.alpharout.adminshop.commands;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Subcommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdminshopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Subcommand subcommand;

        if (args.length == 0) {
            subcommand = AdminShop.getSubcommandManager().getSubcommandMap().get("help");
        } else {
            subcommand = AdminShop.getSubcommandManager().getSubcommandMap().get(args[0]);
        }

        if (subcommand == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes(
                    '&',
                    AdminShop.getConfigManager().getMessagesConf().getString("wrong-subcommand-output")
            ));
            return true;
        }
        return subcommand.handleCommand(sender, command, label, args);
    }
}
