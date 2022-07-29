package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.managers.NoteManager;
import me.imlukas.withdrawer.utils.EconomyUtil;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class BankNoteWithdrawCommand implements CommandExecutor, TabCompleter {

    private final Withdrawer main;
    private final MessagesFile messages;
    private final EconomyUtil economyUtil;

    private final NoteManager noteManager;

    private int amount;
    private double money;

    public BankNoteWithdrawCommand(Withdrawer main) {
        this.main = main;
        this.messages = main.getMessages();
        this.noteManager = new NoteManager(main);
        this.economyUtil = new EconomyUtil(main);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            messages.sendMessage(sender, "global.not-player");
            return true;
        }
        if (args.length >= 3) {
            messages.sendStringMessage(sender, "Usage: /withdrawmoney <money> <amount>");
            return true;
        }
        if (!(player.hasPermission("withdrawer.withdraw.banknote"))) {
            messages.sendMessage(sender, "global.no-permission");
            return true;
        }

        money = Double.parseDouble(args[0]);
        if (money <= 0) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 Money must be positive and bigger than zero");
            return true;
        }
        if (!(player.hasPermission("withdrawer.bypass.minmax.banknote"))) {
            if (money < main.getConfig().getInt("banknote.min")) {
                messages.sendStringMessage(sender, "&c&l[Error]&7 Money must be bigger than " + main.getConfig().getInt("banknote.min"));
                return true;
            }
            if (money > main.getConfig().getInt("banknote.max")) {
                messages.sendStringMessage(sender, "&c&l[Error]&7 Money must be smaller than " + main.getConfig().getInt("banknote.max"));
                return true;

            }
        }

        if (args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                messages.sendStringMessage(sender, "Amount must be a number");
                return true;
            }
            if (amount < 1) {
                messages.sendStringMessage(sender, "Usage: /withdrawmoney <money> <amount>");
                return true;
            }

            noteManager.give(player, money, amount); // gives player x amount of notes
            return true;
        }

        noteManager.give(player, money); // gives player 1 note.
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
