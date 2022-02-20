package de.alpharout.adminshop.commands.sub;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.SkinInformation;
import de.alpharout.adminshop.api.Subcommand;
import de.alpharout.adminshop.api.Trader;
import de.alpharout.adminshop.utils.CreateTraderOptions;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CreateSubcommand implements Subcommand {
    @Override
    public boolean handleCommand(CommandSender sender, Command command, String label, String[] args) {
        /*
        Command for creating Traders.
        The plan is to use the Bukkit Conversation API to get the input from the player.
         */

        if (!sender.hasPermission("adminshop.command.create")) {
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
        CreateTraderOptions createTraderOptions = new CreateTraderOptions();

        if (args.length != 2) {
            player.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            AdminShop.getConfigManager().getMessagesConf().getString("missing-trader-name-output")
                    )
            );
            return true;
        }

        String traderInternalName = args[1];
        createTraderOptions.setInternalName(traderInternalName);

        // TODO: Add text input validation
        Prompt skinLinkPrompt = new StringPrompt() {
            @Override
            public String getPromptText(ConversationContext context) {
                return ChatColor.translateAlternateColorCodes(
                        '&',
                        AdminShop.getConfigManager().getMessagesConf().getString("enter-skin-output")
                );
            }

            @Override
            public Prompt acceptInput(ConversationContext context, String input) {
                createTraderOptions.setSkinUrl(input);
                player.sendMessage(
                        ChatColor.translateAlternateColorCodes(
                                '&',
                                AdminShop.getConfigManager().getMessagesConf().getString("creating-trader-output")
                        )
                );
                processInputs(player, createTraderOptions);

                return null;
            }
        };

        Prompt displayNamePrompt = new StringPrompt() {
            @Override
            public String getPromptText(ConversationContext context) {
                return ChatColor.translateAlternateColorCodes(
                        '&',
                        AdminShop.getConfigManager().getMessagesConf().getString("enter-display-name-output")
                );
            }

            @Override
            public Prompt acceptInput(ConversationContext context, String input) {
                createTraderOptions.setDisplayName(input);
                return skinLinkPrompt;
            }
        };


        ConversationFactory conversationFactory = new ConversationFactory(AdminShop.getInstance())
                .withLocalEcho(false)
                .withFirstPrompt(displayNamePrompt);
        conversationFactory.buildConversation(player).begin();
        return true;
    }

    void processInputs(Player player, CreateTraderOptions createTraderOptions) {
        Bukkit.getScheduler().runTaskAsynchronously(AdminShop.getInstance(), new Runnable() {
            @Override
            public void run() {
                String invalidSkinUrl = ChatColor.translateAlternateColorCodes(
                        '&',
                        AdminShop.getConfigManager().getMessagesConf().getString("invalid-skin-url-output")
                );

                if (createTraderOptions.getSkinUrl() == null) {
                    player.sendMessage(invalidSkinUrl);
                    return;
                }
                SkinInformation skinInformation = SkinInformation.loadFromURL(createTraderOptions.getSkinUrl());
                if (skinInformation == null) {
                    player.sendMessage(invalidSkinUrl);
                    return;
                }

                Bukkit.getScheduler().runTask(AdminShop.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, createTraderOptions.getDisplayName());
                        int npcId = npc.getId();

                        Trader trader = new Trader(UUID.randomUUID(), createTraderOptions.getInternalName(), createTraderOptions.getDisplayName(), skinInformation);
                        Trader.addTraderToList(trader);
                        trader.saveToConfig();
                        trader.saveToDatabase();

                        npc.getOrAddTrait(SkinTrait.class).setSkinPersistent(
                                skinInformation.getSkinName(),
                                skinInformation.getTextureSignature(),
                                skinInformation.getTextureValue()
                        );
                        npc.spawn(player.getLocation());
                    }
                });

                player.sendMessage(
                        ChatColor.translateAlternateColorCodes(
                                '&',
                                AdminShop.getConfigManager().getMessagesConf().getString("created-trader-output")
                        )
                );
            }
        });
    }
}
