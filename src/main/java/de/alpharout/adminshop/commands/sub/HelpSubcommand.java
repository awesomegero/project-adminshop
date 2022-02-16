package de.alpharout.adminshop.commands.sub;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Subcommand;
import de.alpharout.adminshop.utils.ConfigHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class HelpSubcommand implements Subcommand {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String s, String[] args) {
        for (String message : ConfigHelper.getColoredList(AdminShop.getConfigManager().getMessagesConf(), "help-command-output")) {
            sender.sendMessage(message);
        }

        return true;
    }
}
