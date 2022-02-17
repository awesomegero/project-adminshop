package de.alpharout.adminshop.api;

import java.util.HashMap;

public class SubcommandManager {
    // All subcommands with the argument they belong to (e.g. /adminshop test, where test is the argument)
    private final HashMap<String, Subcommand> subcommandMap;

    public SubcommandManager() {
        subcommandMap = new HashMap<>();
    }

    // Registers subcommand by putting it into the hash map
    public boolean registerSubcommand(String argument, Subcommand subcommand) {
        if (subcommandMap.get(argument) != null) return false;

        subcommandMap.put(argument, subcommand);
        return true;
    }

    public HashMap<String, Subcommand> getSubcommandMap() {
        return subcommandMap;
    }
}
