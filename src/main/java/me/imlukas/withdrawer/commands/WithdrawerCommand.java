package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WithdrawerCommand implements CommandExecutor {
    private final Withdrawer main;
    private final MessagesFile messages;

    public WithdrawerCommand(Withdrawer main) {
        this.main = main;
        this.messages = main.getMessages();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)){
            return true;
        }
        if (!(player.hasPermission("withdrawer.admin"))){
            messages.sendStringMessage(player, "&7||---- &dWithdrawer &7----||");
            messages.sendStringMessage(player, "&7/withdrawer help - see all the commands");
            return true;
        }
        if (player.hasPermission("withdrawer.admin") && args.length != 1){
            messages.sendStringMessage(player, "&7||---- &dWithdrawer &7----||");
            messages.sendStringMessage(player, "&7Version: V1.0");
            messages.sendStringMessage(player, "&7Author: imlukas");
            messages.sendStringMessage(player, "&cThanks for using this plugin <3");
            return true;
        }

        if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")){
            messages.sendStringMessage(player, "&7||---- &dWithdrawer Help &7----||");
            messages.sendStringMessage(player, "- &7/withdrawmoney <money>");
            messages.sendStringMessage(player, "  - &7/withdrawmoney <money> <amount>");
            messages.sendStringMessage(player, "- &7/withdrawxp <money>");
            messages.sendStringMessage(player, "  - &7/withdrawxp <money> <amount>");
            return true;
        }



        return true;
    }
}
