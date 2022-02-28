package de.alpharout.adminshop.api;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.utils.ConfigHelper;
import de.alpharout.adminshop.utils.Log;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class Product {
    private static ArrayList<Product> productList = new ArrayList<>();

    public static void loadProductList() {
        try {
            PreparedStatement preparedStatement = AdminShop.getDatabaseManager().getConnection().prepareStatement(
                    "SELECT InternalName, DisplayName, MaterialName, MaterialAmount, Price, TraderName, BuyLimit, ProductType " +
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
                String productType = resultSet.getString("ProductType");

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

                Product product = new Product(internalName, displayName, itemStack, price, buyLimit, ProductType.valueOf(productType), trader);
                trader.addProduct(product);
                productList.add(product);

                Log.debug("Added product " + internalName + " to " + traderName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Trader trader : Trader.getTraderList()) {
            trader.loadPages();
        }
    }

    public static void addProductToList(Product product) {
        productList.add(product);
    }

    public static ArrayList<Product> getProductList() {
        return productList;
    }

    private String internalName;
    private String displayName;
    private ItemStack itemStack;
    private double price;
    private int currentlySold;
    private int buyLimit;
    private ProductType productType;
    private boolean isItemSoldOut;
    private Trader trader;

    public Product(String internalName, String displayName, ItemStack itemStack, double price, int buyLimit, ProductType productType, Trader trader) {
        this.internalName = internalName;
        this.displayName = displayName;
        this.itemStack = itemStack;
        this.price = price;
        this.buyLimit = buyLimit;
        this.productType = productType;
        this.currentlySold = 0;
        this.isItemSoldOut = false;
        this.trader = trader;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Item displayed in the buy/sell menu
    public ItemStack getItemStack() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes(
                '&',
                displayName
        ));
        ArrayList<String> itemLore = ConfigHelper.getColoredList(AdminShop.getConfigManager().getMessagesConf(), "product-item-lore");

        for (int i = 0; i < itemLore.size(); i++) {
            DecimalFormat decimalFormat = new DecimalFormat("0.#");
            itemLore.set(i, StringUtils.replace(itemLore.get(i), "%price%", decimalFormat.format(price)));
            itemLore.set(i, StringUtils.replace(itemLore.get(i), "%item_name%",
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            displayName
                    )));
            itemLore.set(i, StringUtils.replace(itemLore.get(i), "%items_left%", String.valueOf(buyLimit - currentlySold)));
        }
        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    // Item added to the players inventory
    public ItemStack getRawStack() {
        ItemStack itemStack = new ItemStack(getItemStack().getType());
        itemStack.setAmount(getItemStack().getAmount());

        return itemStack;
    }

    public double getPrice() {
        return price;
    }

    public int getBuyLimit() {
        return buyLimit;
    }

    public int getCurrentlySold() {
        return currentlySold;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void addSell() {
        currentlySold++;

        if ((buyLimit - currentlySold) == 0) {
            isItemSoldOut = true;
        }
    }

    public void saveToDatabase() {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = AdminShop.getDatabaseManager().getConnection().prepareStatement(
                    "INSERT INTO adminshop_products (InternalName, DisplayName, MaterialName, MaterialAmount, Price, TraderName, BuyLimit, ProductType) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);"
            );

            preparedStatement.setString(1, internalName);
            preparedStatement.setString(2, displayName);
            preparedStatement.setString(3, itemStack.getType().name());
            preparedStatement.setInt(4, itemStack.getAmount());
            preparedStatement.setDouble(5, price);
            preparedStatement.setString(6, trader.getInternalName());
            preparedStatement.setInt(7, buyLimit);
            preparedStatement.setString(8, productType.name());

            preparedStatement.executeUpdate();
            Log.debug("Saved product " + internalName + " to database.");
        } catch (SQLException sqle) {
            Log.critical("Could not save product " + internalName + " to database!");
        }
    }

    public void updateDatabase() {
        try {
            PreparedStatement preparedStatement = AdminShop.getDatabaseManager().getConnection().prepareStatement(
                    "UPDATE adminshop_products SET " +
                            "Price = ?," +
                            "MaterialAmount = ?," +
                            "BuyLimit = ? " +
                            "WHERE InternalName = ?;"
            );

            preparedStatement.setDouble(1, price);
            preparedStatement.setInt(2, getItemStack().getAmount());
            preparedStatement.setInt(3, buyLimit);
            preparedStatement.setString(4, internalName);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentlySold(int currentlySold) {
        this.currentlySold = currentlySold;
    }

    public void processTransaction(Player player) {
        Log.debug("Processing " + internalName + " for " + player.getDisplayName());
        if (getBuyLimit() - currentlySold == 0) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            return;
        }

        if (getProductType() == ProductType.BUY_PRODUCT) {
            if (!AdminShop.getEconomy().has(player, price)) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                return;
            }
            AdminShop.getEconomy().withdrawPlayer(player, price);
            addSell();
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            player.closeInventory();

            addProductToInventory(player);
            return;
        }
        if (getProductType() == ProductType.SELL_PRODUCT) {
            if (player.getInventory().contains(getItemStack().getType(), getItemStack().getAmount())) {
                player.getInventory().removeItem(getRawStack());
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                player.closeInventory();
                AdminShop.getEconomy().depositPlayer(player, price);
                addSell();
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                return;
            }
        }
    }

    public boolean remove(Trader trader) {
        Predicate<Product> byInternalName = product -> product.getInternalName().equals(internalName);
        Product product;
        try {
            product = getProductList().stream().filter(byInternalName).findFirst().get();
        } catch (NoSuchElementException nsee) {
            return false;
        }

        productList.remove(product);
        trader.getProductList().remove(product);
        return true;
    }

    public void addProductToInventory(Player player) {
        player.getInventory().addItem(getRawStack());
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setBuyLimit(int buyLimit) {
        this.buyLimit = buyLimit;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public void setItemSoldOut(boolean itemSoldOut) {
        isItemSoldOut = itemSoldOut;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }
}
