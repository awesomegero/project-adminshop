package de.alpharout.adminshop.api;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.gui.OverviewViewComponent;
import de.alpharout.adminshop.utils.Log;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class Trader {
    /**
     * Global class for accessing anything belonged to traders and their items.
     */

    private static final ArrayList<Trader> traderList = new ArrayList<>();

    public static void loadTraderList() {
        try {
            PreparedStatement preparedStatement = AdminShop.getDatabaseManager().getConnection().prepareStatement(
                    "SELECT NpcUUID, InternalName, DisplayName, SkinName, SkinSignature, SkinTexture " +
                            "FROM adminshop_traders;"
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                String uuid = resultSet.getString("NpcUUID");
                String internalName = resultSet.getString("InternalName");
                String displayName = resultSet.getString("DisplayName");
                String skinName = resultSet.getString("SkinName");
                String skinSignature = resultSet.getString("SkinSignature");
                String skinTexture = resultSet.getString("SkinTexture");

                traderList.add(new Trader(UUID.fromString(uuid), internalName, displayName, new SkinInformation(skinName, skinSignature, skinTexture)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Trader> getTraderList() {
        return traderList;
    }

    public static void addTraderToList(Trader trader) {
        traderList.add(trader);
    }

    private UUID npcUUID;
    private final String internalName;
    private final String displayName;
    private final SkinInformation skinInformation;
    private ArrayList<Product> productList;
    private ArrayList<Product[]> productPages;

    public Trader(UUID npcUUID, String internalName, String displayName, SkinInformation skinInformation) {
        this.npcUUID = npcUUID;
        this.internalName = internalName;
        this.displayName = displayName;
        this.skinInformation = skinInformation;
        this.productList = new ArrayList<>();
        this.productPages = new ArrayList<>();
    }

    @Deprecated
    public void saveToConfig() {
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

    public void saveToDatabase() {
        Bukkit.getScheduler().runTaskAsynchronously(AdminShop.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement preparedStatement = AdminShop.getDatabaseManager().getConnection().prepareStatement(
                            "INSERT INTO adminshop_traders (NpcUUID, InternalName, DisplayName, SkinName, SkinSignature, SkinTexture)" +
                                    "VALUES (?, ?, ?, ?, ?, ?);"
                    );
                    preparedStatement.setString(1, npcUUID.toString());
                    preparedStatement.setString(2, internalName);
                    preparedStatement.setString(3, displayName);
                    preparedStatement.setString(4, skinInformation.getSkinName());
                    preparedStatement.setString(5, skinInformation.getTextureSignature());
                    preparedStatement.setString(6, skinInformation.getTextureValue());

                    preparedStatement.executeUpdate();
                } catch (SQLException sqlException) {
                    // TODO: Add correct error handling
                    sqlException.printStackTrace();
                }
            }
        });
    }

    public void addProduct(Product product) {
        productList.add(product);
    }

    public void loadPages() {
        ArrayList<Product> currentPage = new ArrayList<>();
        Log.debug("Product list size: " + productList.size());
        for (Product product : productList) {
            if (currentPage.size() > 15) {
                Log.debug("Creating new page");
                productPages.add(currentPage.toArray(new Product[0]));
                currentPage = new ArrayList<>();
            }

            currentPage.add(product);
        }

        productPages.add(currentPage.toArray(new Product[0]));
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getInternalName() {
        return internalName;
    }

    public SkinInformation getSkinInformation() {
        return skinInformation;
    }

    public UUID getNpcUUID() {
        return npcUUID;
    }

    public ArrayList<Product[]> getProductPages() {
        return productPages;
    }

    public NPC getNpc() {
        return CitizensAPI.getNPCRegistry().getByUniqueId(npcUUID);
    }
}
