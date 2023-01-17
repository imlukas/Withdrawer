package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.managers.impl.HealthItem;
import me.imlukas.withdrawer.utils.storage.MessagesFile;
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

public class HealthWithdrawCommand implements CommandExecutor, TabCompleter {
    private final MessagesFile messages;
    private final HealthItem healthItemManager;
    private final FileConfiguration config;

    private final int minHealth;

    public HealthWithdrawCommand(Withdrawer main) {
        this.healthItemManager = main.getHealthItemManager();
        this.messages = main.getMessages();
        this.config = main.getConfig();
        minHealth = config.getInt("health.min");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            giveConsole(sender, args);
            return true;
        }

        if (!(player.hasPermission("withdrawer.withdraw.health"))) {
            messages.sendMessage(sender, "global.no-permission");
            return true;
        }
        if (args.length != 1) {
            messages.sendMessage(player, "health-withdraw.usage");
            return true;
        }

        int amount = parseAmount(args[0]);

        if (amount < minHealth) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 HP Amount must be bigger than " + minHealth);
            return true;
        }

        healthItemManager.give(player, amount, false);
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

        int hpAmount = parseAmount(args[1]);

        if (hpAmount <= 0) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 amount must be positive and bigger than zero");
            return;
        }
        if (hpAmount < minHealth) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 HP Amount must be bigger than " + minHealth);
        }
        healthItemManager.give(target, hpAmount, true);
    }

    private int parseAmount(String amount) {
        int amountParsed;

        try {
            amountParsed = Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            return 0;
        }

        return amountParsed;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add(minHealth + "");
            completions.add("5");
            completions.add("10");
            completions.add("15");
            completions.add("20");
            return completions;
        }
        return Collections.emptyList();
    }
}
