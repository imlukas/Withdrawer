package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.managers.ExpBottle;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpBottleCommand implements CommandExecutor, TabCompleter {

    private final FileConfiguration config;
    private final MessagesFile messages;
    private final ExpBottle expBottleManager;

    private final int minExp, maxExp;
    public ExpBottleCommand(Withdrawer main) {
        this.config = main.getConfig();
        this.messages = main.getMessages();
        this.expBottleManager = main.getExpBottleManager();
        minExp = config.getInt("expbottle.min");
        maxExp = config.getInt("expbottle.max");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            messages.sendMessage(sender, "global.not-player");
            return true;
        }
        if (!(player.hasPermission("withdrawer.withdraw.expbottle"))) {
            messages.sendMessage(sender, "global.no-permission");
            return true;
        }
        if (args.length >= 3 || args.length == 0) {
            messages.sendStringMessage(sender, "Usage: /withdrawxp <exp> (quantity)");
            return true;
        }

        int expAmount;
        try {
            expAmount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 Exp must be an integer number.");
            return true;
        }
        if (expAmount <= 0) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 Exp must be positive and bigger than zero");
            return true;
        }

        if (!(player.hasPermission("withdrawer.bypass.minmax.expbottle"))) {
            if (expAmount < minExp) {
                messages.sendStringMessage(sender, "&c&l[Error]&7 EXP amount must be bigger than " + minExp);
                return true;
            }
            if (expAmount >maxExp) {
                messages.sendStringMessage(sender, "&c&l[Error]&7 EXP amount must be smaller than " + maxExp);
                return true;
            }
        }


        if (args.length == 2) {
            int quantity;
            try {
                quantity = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                messages.sendStringMessage(sender, "Amount must be a number");
                return true;
            }
            if (expAmount * quantity > maxExp) {
                messages.sendStringMessage(sender, "&c&l[Error]&7 EXP amount must be smaller than " + maxExp);
                return true;
            }
            if (quantity < 1) {
                messages.sendStringMessage(sender, "Usage: /withdrawxp <money> (quantity)");
                return true;
            }
            expBottleManager.give(player, expAmount, quantity); // gives player x amount of expbottles
            return true;
        }
        expBottleManager.give(player, expAmount, 1); // gives player 1 exp bottle
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("10");
            completions.add("100");
            completions.add("1000");
            completions.add("" + maxExp);
            return completions;
        }
        if (args.length == 2) {
            completions.add("10");
            completions.add("100");
            return completions;
        }
        return Collections.emptyList();
    }
}
