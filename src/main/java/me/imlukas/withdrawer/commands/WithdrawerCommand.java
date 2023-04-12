package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
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

public class WithdrawerCommand implements CommandExecutor, TabCompleter {

    private final MessagesFile messages;
    private final Withdrawer main;
    private final String[] SUB_COMMANDS = {"toggleprefix", "toggleintrusive", "toggleactionbar", "reload", "help", "?"};

    public WithdrawerCommand(Withdrawer main) {
        this.messages = main.getMessages();
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length == 0) {
            if (!player.hasPermission("withdrawer.admin")) {
                messages.sendStringMessage(player, "&7||---- &dWithdrawer &7----||");
                messages.sendStringMessage(player, "&7/withdrawer help - see all the commands");
                messages.sendStringMessage(player, "&7/withdrawer toggleactionbar");
                messages.sendStringMessage(player, "&7/withdrawer toggleintrusive");
                messages.sendStringMessage(player, "&7/withdrawer toggleprefix");
                return true;
            }
            if (player.hasPermission("withdrawer.admin")) {
                messages.sendStringMessage(player, "&7||---- &dWithdrawer &7----||");
                messages.sendStringMessage(player, "&7Version: V1.6");
                messages.sendStringMessage(player, "&7Author: imlukas");
                messages.sendStringMessage(player, "&cThanks for using withdrawer <3");
                messages.sendStringMessage(player, "&7||-----------------||");
                messages.sendStringMessage(player, "- &7/withdrawer help - see all the commands");
                messages.sendStringMessage(player, "- &7/withdrawer toggleactionbar - toggle the actionbar messages");
                messages.sendStringMessage(player, "- &7/withdrawer toggleintrusive - toggle between less intrusive messages");
                messages.sendStringMessage(player, "- &7/withdrawer toggleprefix - deactivate/activate prefix on messages");
                return true;
            }
        }
        if (player.hasPermission("withdrawer.admin")) {
            String action = args[0];
            switch (action) {
                case ("toggleactionbar") -> {
                    if (messages.toggleActionBar()) {
                        messages.sendMessage(player, "global.feature-on", (message) -> message.replace("%feature%", "ActionBar"));
                    } else {
                        messages.sendMessage(player, "global.feature-off", (message) -> message.replace("%feature%", "ActionBar"));
                    }
                }
                case ("toggleintrusive") -> {
                    if (messages.toggleLessIntrusive()) {
                        messages.sendMessage(player, "global.feature-on", (message) -> message.replace("%feature%", "Less Intrusive mode"));
                    } else {
                        messages.sendMessage(player, "global.feature-off", (message) -> message.replace("%feature%", "Less Intrusive mode"));
                    }
                }
                case ("toggleprefix") -> {
                    if (messages.togglePrefix()) {
                        messages.sendMessage(player, "global.feature-on", (message) -> message.replace("%feature%", "Prefix"));
                    } else {
                        messages.sendMessage(player, "global.feature-off", (message) -> message.replace("%feature%", "Prefix"));
                    }
                }
                case ("reload") -> {
                    main.onDisable();
                    main.onEnable();
                    messages.sendMessage(player, "global.reload");
                }
                case ("help") -> {
                    messages.sendStringMessage(player, "&7||---- &dWithdrawer Help &7----||");
                    messages.sendStringMessage(player, "- &7/withdrawmoney <money>");
                    messages.sendStringMessage(player, "  - &7/withdrawmoney <money> <amount>");
                    messages.sendStringMessage(player, "- &7/withdrawxp <money>");
                    messages.sendStringMessage(player, "  - &7/withdrawxp <money> <amount>");
                    messages.sendStringMessage(player, "- &7/withdrawhp <amount>");
                    messages.sendStringMessage(player, "- &7/withdrawer toggleactionbar - toggle the actionbar messages");
                    messages.sendStringMessage(player, "- &7/withdrawer toggleintrusive - toggle between less intrusive messages");
                    messages.sendStringMessage(player, "- &7/withdrawer toggleprefix - deactivate/activate prefix on messages");
                }
            }
            return true;
        }

        messages.sendMessage(player, "global.no-permission");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            final List<String> completions = new ArrayList<>();
            //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
            StringUtil.copyPartialMatches(args[0], Arrays.asList(SUB_COMMANDS), completions);
            return completions;
        }

        return Collections.emptyList();
    }
}
