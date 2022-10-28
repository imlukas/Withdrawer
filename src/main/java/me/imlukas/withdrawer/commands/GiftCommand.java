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
import org.bukkit.entity.Player;

public class GiftCommand implements CommandExecutor {

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

        String type = args[0]; // money, hp, exp
        Player target = Bukkit.getPlayer(args[1]); // player to gift

        if (target == null) {
            messages.sendMessage(sender, "global.player-not-found");
            return true;
        }

        int value = parse(args[2]); // value of the gift
        int quantity = parse(args[3]); // quantity to gift

        if (value <= 0) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 amount must be positive and bigger than zero");
            return true;
        }
        if (quantity <= 0) {
            quantity = 1;
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
}
