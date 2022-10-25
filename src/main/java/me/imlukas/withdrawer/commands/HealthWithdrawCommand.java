package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.managers.HealthItem;
import me.imlukas.withdrawer.utils.HealthUtil;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.attribute.Attribute;
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
            messages.sendMessage(sender, "global.not-player");
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
        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 Amount must be an integer number");
            return true;
        }

        if (amount < minHealth) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 Amount must be bigger than " + minHealth);
            return true;
        }

        healthItemManager.give(player, amount);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("5");
            completions.add("10");
            completions.add("15");
            completions.add("20");
            return completions;
        }
        return Collections.emptyList();
    }
}
