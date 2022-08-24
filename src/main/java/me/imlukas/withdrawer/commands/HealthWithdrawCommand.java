package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.managers.HealthItem;
import me.imlukas.withdrawer.utils.HealthUtil;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealthWithdrawCommand implements CommandExecutor {
    private final MessagesFile messages;
    private final HealthItem healthItemManager;
    private final HealthUtil healthUtil;

    public HealthWithdrawCommand(Withdrawer main) {
        this.healthItemManager = main.getHealthItemManager();
        this.healthUtil = main.getHealthUtil();
        this.messages = main.getMessages();
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
        if (args.length >= 2) {
            messages.sendStringMessage(sender, "Usage: /withdrawmoney <amount>");
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 Amount must be an integer number and be bigger than 0.5");
            return true;
        }

        healthItemManager.give(player, amount);
        return true;
    }
}
