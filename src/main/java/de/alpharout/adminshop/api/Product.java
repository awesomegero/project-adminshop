package de.alpharout.adminshop.api;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.utils.Log;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Product {
    private static ArrayList<Product> productList = new ArrayList<>();

    public static void loadProductList() {
        try {
            PreparedStatement preparedStatement = AdminShop.getDatabaseManager().getConnection().prepareStatement(
                    "SELECT InternalName, DisplayName, MaterialName, MaterialAmount, Price, TraderName, BuyLimit " +
                            "FROM adminshop_products;"
            );

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String internalName = resultSet.getString("InternalName");
                String displayName = resultSet.getString("DisplayName");
                String materialName = resultSet.getString("MaterialName");
                int materialAmount = resultSet.getInt("MaterialAmount");
                double price = resultSet.getDouble("Price");
                String traderName = resultSet.getString("TraderName");
                int buyLimit = resultSet.getInt("BuyLimit");

                Predicate<Trader> byInternalName = trader -> trader.getInternalName().equals(traderName);
                Trader trader;
                try {
                    trader = Trader.getTraderList().stream().filter(byInternalName).findFirst().get();
                } catch (NoSuchElementException nsee) {
                    Log.error("The product " + internalName + " is connected to a trader (" + traderName + ") that can't be found!");
                    continue;
                }

                Material material = Material.getMaterial(materialName);
                ItemStack itemStack = new ItemStack(material);
                itemStack.setAmount(materialAmount);

                trader.addProduct(new Product(internalName, displayName, itemStack, price, buyLimit));
                Log.debug("Added product " + internalName + " to " + traderName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String internalName;
    private String displayName;
    private ItemStack itemStack;
    private double price;
    private int buyLimit;

    public Product(String internalName, String displayName, ItemStack itemStack, double price, int buyLimit) {
        this.internalName = internalName;
        this.displayName = displayName;
        this.itemStack = itemStack;
        this.price = price;
        this.buyLimit = buyLimit;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public double getPrice() {
        return price;
    }
}
