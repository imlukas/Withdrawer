package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.manager.ExpBottleManager;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ExpBottleCommand implements CommandExecutor, TabCompleter {

    private final Withdrawer main;
    private final MessagesFile messages;
    private final ExpBottleManager expBottleManager;

    int expAmount;
    int quantity;

    public ExpBottleCommand(Withdrawer main) {
        this.main = main;
        this.messages = main.getMessages();
        this.expBottleManager = main.getExpBottleManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            messages.sendMessage(sender, "global.not-player");
            return true;
        }
        if (args.length >= 3) {
            messages.sendStringMessage(sender, "Usage: /withdrawxp <exp> (quantity)");
            return true;
        }

        expAmount = Integer.parseInt(args[0]);
        if (expAmount <= 0) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 Money must be positive and bigger than zero");
            return true;
        }
        if (expAmount < main.getConfig().getInt("expbottle.min")) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 EXP amount must be bigger than " + main.getConfig().getInt("expbottle.min"));
            return true;
        }
        if (expAmount > main.getConfig().getInt("expbottle.max")) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 EXP amount must be smaller than " + main.getConfig().getInt("expbottle.max"));
            return true;

        }

        if (args.length == 2) {
            try {
                quantity = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                messages.sendStringMessage(sender, "Amount must be a number");
                return true;
            }
            if (expAmount * quantity > main.getConfig().getInt("expbottle.max")) {
                messages.sendStringMessage(sender, "&c&l[Error]&7 EXP amount must be smaller than " + main.getConfig().getInt("expbottle.max"));
                return true;
            }
            if (quantity < 1) {
                messages.sendStringMessage(sender, "Usage: /withdrawxp <money> (quantity)");
                return true;
            }
            expBottleManager.give(player, expAmount, quantity);
            return true;
        }
        expBottleManager.give(player, expAmount);
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
