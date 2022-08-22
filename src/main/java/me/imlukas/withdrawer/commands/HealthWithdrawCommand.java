package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.managers.HealthItem;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealthWithdrawCommand implements CommandExecutor {
    private final MessagesFile messages;
    private final HealthItem healthItemManager;

    public HealthWithdrawCommand(Withdrawer main) {
        this.healthItemManager = main.getHealthItemManager();
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
        double amount;
        try {
            amount = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            messages.sendStringMessage(sender, "&c&l[Error]&7 Amount must be a number");
            return true;
        }
        double newHealth = amount;
        // player.setHealthScale(newHealth);
        double oldHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(oldHealth - newHealth);
        healthItemManager.give(player, newHealth);
        return false;
    }
}
