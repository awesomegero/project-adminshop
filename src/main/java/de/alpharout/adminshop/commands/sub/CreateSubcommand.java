package de.alpharout.adminshop.commands.sub;

import de.alpharout.adminshop.AdminShop;
import de.alpharout.adminshop.api.Subcommand;
import de.alpharout.adminshop.utils.CreateTraderOptions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

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

        // TODO: Add real logic
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
                createTraderOptions.setSkinUrl(input);
                return skinLinkPrompt;
            }
        };


        ConversationFactory conversationFactory = new ConversationFactory(AdminShop.getInstance())
                .withLocalEcho(false)
                .withFirstPrompt(displayNamePrompt);
        conversationFactory.buildConversation(player).begin();
        return true;
    }
}
