package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.WithdrawerPlugin;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.interactions.Messages;
import me.imlukas.withdrawer.utils.text.TextUtils;
import org.bukkit.command.CommandSender;

public class HelpCommand implements SimpleCommand {

    private final WithdrawerPlugin plugin;
    private final Messages messages;

    public HelpCommand(WithdrawerPlugin plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
    }

    @Override
    public String getPermission() {
        return "withdrawer.help";
    }

    @Override
    public String getIdentifier() {
        return "withdrawer.help";
    }

    @Override
    public void execute(CommandSender sender, String... args) {
        sender.sendMessage(TextUtils.color("&7&m------------------------"));
        sender.sendMessage(TextUtils.color("&d&l[Withdrawer] &7- &fVersion " + plugin.getDescription().getVersion()));
        sender.sendMessage(TextUtils.color("&7Created by &fimlukas"));
        sender.sendMessage(TextUtils.color("&7&m------------------------"));
        messages.sendMessage(sender, "global.help");

    }
}
