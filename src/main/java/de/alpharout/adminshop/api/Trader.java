package de.alpharout.adminshop.api;

import de.alpharout.adminshop.AdminShop;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class Trader {
    private static final ArrayList<Trader> traderList = new ArrayList<>();

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

    public static void addTraderToList(Trader trader) {
        traderList.add(trader);
    }

    private final int npcId;
    private final String internalName;
    private final String displayName;
    private final SkinInformation skinInformation;

    public Trader(int npcId, String internalName, String displayName, SkinInformation skinInformation) {
        this.npcId = npcId;
        this.internalName = internalName;
        this.displayName = displayName;
        this.skinInformation = skinInformation;
    }

    public void saveToConfig() {
        AdminShop.getConfigManager().getTraderConf().set(internalName + ".npcId", npcId);
        AdminShop.getConfigManager().getTraderConf().set(internalName + ".displayName", displayName);
        AdminShop.getConfigManager().getTraderConf().set(internalName + ".skin.name", skinInformation.getSkinName());
        AdminShop.getConfigManager().getTraderConf().set(internalName + ".skin.signature", skinInformation.getTextureSignature());
        AdminShop.getConfigManager().getTraderConf().set(internalName + ".skin.value", skinInformation.getTextureValue());

        try {
            AdminShop.getConfigManager().getTraderConf().save(AdminShop.getConfigManager().getTraderFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public NPC getNpc() {
        return CitizensAPI.getNPCRegistry().getById(npcId);
    }
}
