package de.alpharout.adminshop.commands.sub;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Product;
import de.alpharout.adminshop.api.ProductType;
import de.alpharout.adminshop.api.Subcommand;
import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.utils.AddProductOptions;
import de.alpharout.adminshop.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class AddSubcommand implements Subcommand {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("adminshop.command.add")) {
            sender.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            AdminShop.getConfigManager().getMessagesConf().getString("no-perm-output")
                    )
            );
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            AdminShop.getConfigManager().getMessagesConf().getString("no-player-output")
                    )
            );
            return true;
        }
        Player player = (Player) sender;

        if (args.length != 4) {
            player.sendMessage("§cPlease use /adminshop add <Trader> <Buy/Sell> <Name>");
            return true;
        }

        String traderName = args[1];
        String productTypeName = args[2];
        String internalName = args[3];
        ProductType productType;

        if (productTypeName.equalsIgnoreCase("buy")) {
            productType = ProductType.BUY_PRODUCT;
        } else if (productTypeName.equalsIgnoreCase("sell")) {
            productType = ProductType.SELL_PRODUCT;
        } else {
            player.sendMessage("§cThe product type you specified is invalid. Use buy or sell.");
            return true;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() == Material.AIR) {
            player.sendMessage("§cPlease put the item you want to sell in your main hand.");
            return true;
        }

        Predicate<Trader> byName = trader -> trader.getInternalName().equalsIgnoreCase(traderName);
        Trader trader;
        try {
            trader = Trader.getTraderList().stream().filter(byName).findFirst().get();
        } catch (NoSuchElementException nsee) {
            player.sendMessage("§cThis trader does not exist!");
            return true;
        }

        AddProductOptions addProductOptions = new AddProductOptions();
        addProductOptions.setInternalName(internalName);
        addProductOptions.setProductType(productType);

        Prompt maxAmountPrompt = new NumericPrompt() {
            @Override
            protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
                addProductOptions.setProductLimit(input.intValue());
                processInput(addProductOptions, trader, itemInHand, player);
                return null;
            }

            @Override
            public String getPromptText(ConversationContext context) {
                return ChatColor.translateAlternateColorCodes(
                        '&',
                        AdminShop.getConfigManager().getMessagesConf().getString("enter-product-limit-output")
                );
            }
        };
        Prompt pricePrompt = new NumericPrompt() {
            @Override
            protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
                addProductOptions.setProductPrice(input.doubleValue());
                return maxAmountPrompt;
            }

            @Override
            public String getPromptText(ConversationContext context) {
                return ChatColor.translateAlternateColorCodes(
                        '&',
                        AdminShop.getConfigManager().getMessagesConf().getString("enter-product-price-output")
                );
            }
        };

        Prompt amountPrompt = new NumericPrompt() {
            @Override
            protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
                addProductOptions.setProductAmount(input.intValue());
                return pricePrompt;
            }

            @Override
            public String getPromptText(ConversationContext context) {
                return ChatColor.translateAlternateColorCodes(
                        '&',
                        AdminShop.getConfigManager().getMessagesConf().getString("enter-product-amount-output")
                );
            }
        };
        Prompt displayNamePrompt = new StringPrompt() {
            @Override
            public String getPromptText(ConversationContext context) {
                return ChatColor.translateAlternateColorCodes(
                        '&',
                        AdminShop.getConfigManager().getMessagesConf().getString("enter-product-display-name-output")
                );
            }

            @Override
            public Prompt acceptInput(ConversationContext context, String input) {
                addProductOptions.setDisplayName(input);
                return amountPrompt;
            }
        };

        ConversationFactory conversationFactory = new ConversationFactory(AdminShop.getInstance())
                .withLocalEcho(false)
                .withFirstPrompt(displayNamePrompt)
                .withEscapeSequence("/quit");
        conversationFactory.buildConversation(player).begin();

        return true;
    }

    public void processInput(AddProductOptions addProductOptions, Trader trader, ItemStack itemStack, Player player) {
        ItemStack productItem = itemStack;
        ItemMeta itemMeta = productItem.getItemMeta();
        productItem.setAmount(addProductOptions.getProductAmount());
        productItem.setItemMeta(itemMeta);

        Log.debug("Product limit: " + addProductOptions.getProductLimit());

        Product product = new Product(
                addProductOptions.getInternalName(),
                addProductOptions.getDisplayName(),
                productItem,
                addProductOptions.getProductPrice(),
                addProductOptions.getProductLimit(),
                addProductOptions.getProductType(),
                trader
        );
        Product.addProductToList(product);

        Bukkit.getScheduler().runTaskAsynchronously(AdminShop.getInstance(), new Runnable() {
            @Override
            public void run() {
                product.saveToDatabase();
                player.sendMessage("§aCreated §7product!");
            }
        });
    }
}
