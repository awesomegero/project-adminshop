package de.alpharout.adminshop.api;

import de.alpharout.adminshop.AdminShop;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Set;

public class Trader {
    private static ArrayList<Trader> traderList = new ArrayList<>();

    // Loading all traders from the trader.yml into memory (Deserialisation)
    public static void loadTraderList() {
        Set<String> traderNames = AdminShop.getConfigManager().getTraderConf().getConfigurationSection("").getKeys(false);

        for (String internalName : traderNames) {
            int npcId = AdminShop.getConfigManager().getTraderConf().getInt(internalName + ".npcId");
            String displayName = ChatColor.translateAlternateColorCodes(
                    '&',
                    AdminShop.getConfigManager().getTraderConf().getString(internalName + ".displayName")
            );
            String skinName = AdminShop.getConfigManager().getTraderConf().getString(internalName + ".skin.name");
            String skinSignature = AdminShop.getConfigManager().getTraderConf().getString(internalName + ".skin.signature");
            String skinValue = AdminShop.getConfigManager().getTraderConf().getString(internalName + ".skin.value");

            SkinInformation skinInformation = new SkinInformation(skinName, skinSignature, skinValue);

            traderList.add(new Trader(npcId, internalName, displayName, skinInformation));
        }
    }

    public static ArrayList<Trader> getTraderList() {
        return traderList;
    }

    private int npcId;
    private String internalName;
    private String displayName;
    private SkinInformation skinInformation;

    public Trader(int npcId, String internalName, String displayName, SkinInformation skinInformation) {
        this.npcId = npcId;
        this.internalName = internalName;
        this.displayName = displayName;
        this.skinInformation = skinInformation;
    }

    public String getDisplayName() {
        return displayName;
    }

    public NPC getNpc() {
        return CitizensAPI.getNPCRegistry().getById(npcId);
    }
}
