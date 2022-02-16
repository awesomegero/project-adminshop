package de.alpharout.adminshop.api;

import java.util.HashMap;

public class SubcommandManager {
    private HashMap<String, Subcommand> subcommandMap;

    public SubcommandManager() {
        subcommandMap = new HashMap<>();
    }

    public boolean registerSubcommand(String argument, Subcommand subcommand) {
        if (subcommandMap.get(argument) != null) return false;

        subcommandMap.put(argument, subcommand);
        return true;
    }

    public HashMap<String, Subcommand> getSubcommandMap() {
        return subcommandMap;
    }
}
