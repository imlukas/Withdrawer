package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.managers.ExpBottle;
import me.imlukas.withdrawer.managers.HealthItem;
import me.imlukas.withdrawer.managers.Note;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GiftCommand implements CommandExecutor, TabCompleter {

    private final MessagesFile messages;
    private final Note noteManager;
    private final ExpBottle expBottleManager;
    private final HealthItem healthItemManager;

    public GiftCommand(Withdrawer main) {
        this.messages = main.getMessages();
        this.noteManager = main.getNoteManager();
        this.expBottleManager = main.getExpBottleManager();
        this.healthItemManager = main.getHealthItemManager();

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3){
            messages.sendStringMessage(sender, "&c&l[Error]7 Usage: /wdgift (money, hp, exp) (player) (amount) <quantity>");
            return true;
        }
        if (sender instanceof Player player){
            if (!(player.hasPermission("withdrawer.gift"))){
                messages.sendMessage(sender, "global.no-permission");
                return true;
            }
        }
        String type = args[0]; // money, hp, exp
        Player target = Bukkit.getPlayer(args[1]); // player to gift

        if (target == null) {
            messages.sendMessage(sender, "global.player-not-found");
            return true;
        }

        int value = parse(args[2]); // value of the gift

        int quantity = 1;
        if (args.length == 4){
            if (parse(args[3]) > 0) {
                quantity = parse(args[3]);
            }
        }

        if (value <= 0) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 amount must be positive and bigger than zero");
            return true;
        }

        switch (type) {
            case ("money") -> noteManager.gift(target, value, quantity);
            case ("exp") -> expBottleManager.gift(target, value, quantity);
            case ("hp") -> healthItemManager.gift(target, value);
        }


        return true;
    }

    private int parse(String amount) {
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
            StringUtil.copyPartialMatches(args[0], List.of("money", "hp", "exp"), completions);
            return completions;
        }
        if (args.length == 2) {
            return null;
        }

        if (args.length == 3 || args.length == 4) {
            completions.add("10");
            completions.add("100");
            completions.add("1000");
            return completions;
        }

        return Collections.emptyList();
    }
}
