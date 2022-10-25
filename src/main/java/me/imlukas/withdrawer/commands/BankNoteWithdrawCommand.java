package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.managers.Note;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankNoteWithdrawCommand implements CommandExecutor, TabCompleter {

    private final FileConfiguration config;
    private final MessagesFile messages;
    private final Note noteManager;

    private final int minMoney, maxMoney;

    public BankNoteWithdrawCommand(Withdrawer main) {
        this.config = main.getConfig();
        this.messages = main.getMessages();
        this.noteManager = new Note(main);
        minMoney = config.getInt("banknote.min");
        maxMoney = config.getInt("banknote.max");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            messages.sendMessage(sender, "global.not-player");
            return true;
        }
        if (!(player.hasPermission("withdrawer.withdraw.banknote"))) {
            messages.sendMessage(sender, "global.no-permission");
            return true;
        }
        if (args.length >= 3 || args.length == 0) {
            messages.sendMessage(player, "banknote-withdraw.usage");
            return true;
        }

        double money = Double.parseDouble(args[0]);
        if (money <= 0) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 Money must be positive and bigger than zero");
            return true;
        }

        if (!(player.hasPermission("withdrawer.bypass.minmax.banknote"))) {

            if (money < minMoney) {
                messages.sendStringMessage(sender, "&c&l[Error]&7 Money must be bigger than " + minMoney);
                return true;
            }
            if (money > maxMoney) {
                messages.sendStringMessage(sender, "&c&l[Error]&7 Money must be smaller than " + maxMoney);
                return true;

            }
        }

        int amount;
        if (args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                messages.sendStringMessage(sender, "Amount must be a number");
                return true;
            }
            if (amount < 1) {
                messages.sendMessage(player, "banknote-withdraw.usage");
                return true;
            }
        } else {
            amount = 1;
        }
        noteManager.give(player, money, amount); // gives the item to the player
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("10");
            completions.add("100");
            completions.add("1000");
            completions.add("" + maxMoney);
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
