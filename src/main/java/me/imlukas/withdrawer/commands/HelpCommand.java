package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import me.imlukas.withdrawer.utils.text.TextUtils;
import org.bukkit.command.CommandSender;

public class HelpCommand implements SimpleCommand {

    private final Withdrawer plugin;
    private final MessagesFile messages;

    public HelpCommand(Withdrawer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
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
