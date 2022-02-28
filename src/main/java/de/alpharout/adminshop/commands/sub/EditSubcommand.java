package de.alpharout.adminshop.commands.sub;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Product;
import de.alpharout.adminshop.api.Subcommand;
import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.utils.EditProductOptions;
import de.alpharout.adminshop.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class EditSubcommand implements Subcommand {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("adminshop.command.edit")) {
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

        if (args.length != 2) {
            player.sendMessage("§cPlease use /adminshop edit <Trader>");
            return true;
        }

        Predicate<Trader> byInternalName = trader -> trader.getInternalName().equalsIgnoreCase(args[1]);
        Trader trader;
        try {
            trader = Trader.getTraderList().stream().filter(byInternalName).findFirst().get();
        } catch (NoSuchElementException nsee) {
            player.sendMessage("§cThis trader does not exist!");
            return true;
        }
        EditProductOptions editProductOptions = new EditProductOptions();
        Prompt buyLimitPrompt = new NumericPrompt() {
            @Override
            protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
                editProductOptions.setBuyLimit(input.intValue());
                processInput(editProductOptions, trader, player);
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
                editProductOptions.setPrice(input.doubleValue());
                return buyLimitPrompt;
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
                editProductOptions.setAmount(input.intValue());
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
        Prompt productNamePrompt = new StringPrompt() {
            @Override
            public String getPromptText(ConversationContext context) {
                return ChatColor.translateAlternateColorCodes(
                        '&',
                        AdminShop.getConfigManager().getMessagesConf().getString("enter-product-internal-name-output")
                );
            }

            @Override
            public Prompt acceptInput(ConversationContext context, String input) {
                editProductOptions.setProductName(input);
                return amountPrompt;
            }
        };

        ConversationFactory conversationFactory = new ConversationFactory(AdminShop.getInstance())
                .withLocalEcho(false)
                .withEscapeSequence("/quit")
                .withFirstPrompt(productNamePrompt);
        conversationFactory.buildConversation(player).begin();

        return true;
    }

    public void processInput(EditProductOptions editProductOptions, Trader trader, Player player) {
        Log.debug("Process Input called!");
        Predicate<Product> byInternalName = product -> product.getInternalName().equalsIgnoreCase(editProductOptions.getProductName());
        Product product;
        try {
            product = trader.getProductList().stream().filter(byInternalName).findFirst().get();
            ItemStack itemStack = product.getItemStack();
            itemStack.setAmount(editProductOptions.getAmount());
            product.setItemStack(itemStack);
            product.setBuyLimit(editProductOptions.getBuyLimit());
            product.setPrice(editProductOptions.getPrice());

            ArrayList<Product> productList = trader.getProductList();
            productList.set(productList.indexOf(product), product);
            trader.setProductList(productList);

            Bukkit.getScheduler().runTaskAsynchronously(AdminShop.getInstance(), new Runnable() {
                @Override
                public void run() {
                    product.updateDatabase();
                }
            });
        } catch (NoSuchElementException nsee) {
            player.sendMessage("§cThis trader does not exist!");
            Log.debug("Trader does not exist!");
            return;
        }
    }
}
