package de.alpharout.adminshop.commands;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Subcommand;
import org.bukkit.ChatColor;
import org.bukkit.command.*;

import java.util.List;
import java.util.Locale;

public class AdminshopCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /*
        Main command redirecting to subcommands.
        When no argument is given, the help subcommand gets executed.
         */

        Subcommand subcommand;

        if (args.length == 0) {
            subcommand = AdminShop.getSubcommandManager().getSubcommandMap().get("help");
        } else {
            subcommand = AdminShop.getSubcommandManager().getSubcommandMap().get(args[0].toLowerCase(Locale.ROOT));
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender.hasPermission(command.getPermission())) {
            return AdminShop.getSubcommandManager().getSubcommandMap().keySet().stream().toList();
        } else {
            return null;
        }
    }
}
