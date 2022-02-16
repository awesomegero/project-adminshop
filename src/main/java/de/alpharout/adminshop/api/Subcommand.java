package de.alpharout.adminshop.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface Subcommand {
    boolean handleCommand(CommandSender sender, Command command, String label, String[] args);
}
