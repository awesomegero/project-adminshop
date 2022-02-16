package de.alpharout.adminshop.commands.sub;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Subcommand;
import de.alpharout.adminshop.utils.ConfigHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class HelpSubcommand implements Subcommand {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String s, String[] args) {
        /*
        Simple help command displaying help message from messages.yml
         */

        if (!sender.hasPermission("adminshop.command.help")) {
            sender.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            AdminShop.getConfigManager().getMessagesConf().getString("no-perm-output")
                    )
            );
            return true;
        }

        for (String message : ConfigHelper.getColoredList(AdminShop.getConfigManager().getMessagesConf(), "help-command-output")) {
            sender.sendMessage(message);
        }

        return true;
    }
}
