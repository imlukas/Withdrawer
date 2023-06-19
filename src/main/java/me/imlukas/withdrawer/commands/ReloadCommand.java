package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements SimpleCommand {

    private final Withdrawer plugin;
    private final MessagesFile messages;

    public ReloadCommand(Withdrawer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
    }

    @Override
    public String getPermission() {
        return "withdrawer.reload";
    }

    @Override
    public String getIdentifier() {
        return "withdrawer.reload";
    }

    @Override
    public void execute(CommandSender sender, String... args) {
        plugin.onDisable();
        plugin.onEnable();
        messages.sendMessage(sender, "global.reload");
    }
}
