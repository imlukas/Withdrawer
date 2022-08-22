package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WithdrawerCommand implements CommandExecutor {

    private final MessagesFile messages;

    public WithdrawerCommand(Withdrawer main) {
        this.messages = main.getMessages();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            return true;
        }
        if (!player.hasPermission("withdrawer.admin")) {
            this.messages.sendStringMessage(player, "&7||---- &dWithdrawer &7----||");
            this.messages.sendStringMessage(player, "&7/withdrawer help - see all the commands");
            return true;
        }
        if (player.hasPermission("withdrawer.admin") && args.length != 1) {
            this.messages.sendStringMessage(player, "&7||---- &dWithdrawer &7----||");
            this.messages.sendStringMessage(player, "&7Version: V1.0");
            this.messages.sendStringMessage(player, "&7Author: imlukas");
            this.messages.sendStringMessage(player, "&cThanks for using this plugin <3");
            return true;
        }
        if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
            this.messages.sendStringMessage(player, "&7||---- &dWithdrawer Help &7----||");
            this.messages.sendStringMessage(player, "- &7/withdrawmoney <money>");
            this.messages.sendStringMessage(player, "  - &7/withdrawmoney <money> <amount>");
            this.messages.sendStringMessage(player, "- &7/withdrawxp <money>");
            this.messages.sendStringMessage(player, "  - &7/withdrawxp <money> <amount>");
            return true;
        }
        return true;
    }
}
