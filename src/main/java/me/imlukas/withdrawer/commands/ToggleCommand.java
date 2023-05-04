package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import org.bukkit.command.CommandSender;

public class ToggleCommand implements SimpleCommand {
    private final MessagesFile messages;

    public ToggleCommand(Withdrawer plugin) {
        this.messages = plugin.getMessages();
    }

    @Override
    public String getIdentifier() {
        return "withdrawer.toggle.*";
    }

    @Override
    public void execute(CommandSender sender, String... args) {
        if (args[0].isEmpty()) {
            messages.sendMessage(sender, "command.invalid-args");
            return;
        }
        String type = args[0];
        boolean isToggled = false;
        if (type.equals("actionbar")) {
            isToggled = messages.toggleActionBar();
        }

        if (type.equals("less-intrusive")) {
            isToggled = messages.toggleLessIntrusive();
        }

        if (type.equals("prefix")) {
            isToggled = messages.togglePrefix();
        }

        if (isToggled) {
            messages.sendMessage(sender, "global.actionbar-on", (message) -> message.replace("%feature%", type));
        } else {
            messages.sendMessage(sender, "global.actionbar-off", (message) -> message.replace("%feature%", type));
        }
    }
}
