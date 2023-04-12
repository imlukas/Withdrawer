package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.managers.impl.ExpBottle;
import me.imlukas.withdrawer.utils.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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
            giveConsole(sender, args);
            return true;
        }

        if (!(player.hasPermission("withdrawer.withdraw.expbottle"))) {
            messages.sendMessage(sender, "global.no-permission");
            return true;
        }
        if (args.length >= 3 || args.length == 0) {
            messages.sendMessage(player, "expbottle-withdraw.usage");
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
            if (expAmount > maxExp) {
                messages.sendStringMessage(sender, "&c&l[Error]&7 EXP amount must be smaller than " + maxExp);
                return true;
            }
        }
        int amount = 1;
        if (args.length == 2){
            amount = NumberUtil.parse(args[1]);
            if (expAmount * amount > maxExp) {
                messages.sendStringMessage(player, "&c&l[Error]&7 Total EXP amount must be smaller than " + maxExp);
                return true;
            }
            if (amount < 1) {
                messages.sendMessage(player, "expbottle-withdraw.usage");
                return true;
            }
        }


        expBottleManager.give(player, expAmount, amount, false);
        return true;
    }


    private void giveConsole(CommandSender sender, String[] args) {
        if (args.length != 3) {
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            messages.sendMessage(sender, "global.player-not-found");
            return;
        }

        int expAmount = NumberUtil.parse(args[1]);

        if (expAmount <= 0) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 Exp must be positive and bigger than zero");
            return;
        }

        int amount = NumberUtil.parse(args[2]);

        if (amount <= 0) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 amount must be positive and bigger than zero");
            return;
        }
        if (expAmount * amount > maxExp) {
            messages.sendStringMessage(target, "&c&l[Error]&7 EXP amount must be smaller than " + maxExp);
        }

        expBottleManager.give(target, expAmount, amount, true);
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
