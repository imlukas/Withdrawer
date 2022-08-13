package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.managers.HeartItemManager;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealthWithdrawCommand implements CommandExecutor {
    private final Withdrawer main;
    private final MessagesFile messages;
    private double amount;

    public HealthWithdrawCommand(Withdrawer main) {
        this.main = main;
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
        if (args.length >= 3) {
            messages.sendStringMessage(sender, "Usage: /withdrawmoney <amount> (quantity)");
            return true;
        }
        try {
            amount = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 Amount must be a number");
            return true;
        }
        player.setHealthScale(player.getHealth() - amount);
        return false;
    }
}
