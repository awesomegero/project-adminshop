package de.alpharout.adminshop.api;

import de.alpharout.adminshop.AdminShop;
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

    private static ArrayList<Trader> traderList = new ArrayList<>();

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

    public static void setTraderList(ArrayList<Trader> traderList) {
        Trader.traderList = traderList;
    }

    public static void addTraderToList(Trader trader) {
        traderList.add(trader);
    }

    private UUID npcUUID;
    private final String internalName;
    private final String displayName;
    private final SkinInformation skinInformation;
    private ArrayList<Product> productList;
    private ArrayList<Product[]> buyProductPages;
    private ArrayList<Product[]> sellProductPages;

    public Trader(UUID npcUUID, String internalName, String displayName, SkinInformation skinInformation) {
        this.npcUUID = npcUUID;
        this.internalName = internalName;
        this.displayName = displayName;
        this.skinInformation = skinInformation;
        this.productList = new ArrayList<>();
        this.buyProductPages = new ArrayList<>();
        this.sellProductPages = new ArrayList<>();
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
        ArrayList<Product> currentBuyPage = new ArrayList<>();
        ArrayList<Product> currentSellPage = new ArrayList<>();
        buyProductPages.clear();
        sellProductPages.clear();
        Log.debug("Product list size: " + productList.size());
        for (Product product : productList) {
            if (product.getProductType() == ProductType.BUY_PRODUCT) {
                if (currentBuyPage.size() > 14) {
                    Log.debug("Creating new page");
                    buyProductPages.add(currentBuyPage.toArray(new Product[0]));
                    currentBuyPage = new ArrayList<>();
                }

                currentBuyPage.add(product);
            } else {
                if (currentSellPage.size() > 15) {
                    Log.debug("Creating new page");
                    sellProductPages.add(currentSellPage.toArray(new Product[0]));
                    currentSellPage = new ArrayList<>();
                }

                currentSellPage.add(product);
            }
        }
        sellProductPages.add(currentSellPage.toArray(new Product[0]));
        buyProductPages.add(currentBuyPage.toArray(new Product[0]));
    }

    public void remove() {
        NPC npc = CitizensAPI.getNPCRegistry().getByUniqueIdGlobal(npcUUID);
        Bukkit.getScheduler().runTask(AdminShop.getInstance(), new Runnable() {
            @Override
            public void run() {
                npc.destroy();
            }
        });


        try {
            PreparedStatement preparedStatement = AdminShop.getDatabaseManager().getConnection().prepareStatement(
                    "DELETE FROM adminshop_traders WHERE InternalName=?;"
            );
            preparedStatement.setString(1, internalName);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public ArrayList<Product[]> getBuyProductPages() {
        return buyProductPages;
    }

    public ArrayList<Product[]> getSellProductPages() {
        return sellProductPages;
    }

    public NPC getNpc() {
        return CitizensAPI.getNPCRegistry().getByUniqueId(npcUUID);
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }
}
