package de.alpharout.adminshop.api;

import net.citizensnpcs.api.npc.NPC;

public class Trader {
    private String displayName;
    private NPC npc;

    public Trader(String displayName, NPC npc) {
        this.displayName = displayName;
        this.npc = npc;
    }

    public String getDisplayName() {
        return displayName;
    }

    public NPC getNpc() {
        return npc;
    }
}
