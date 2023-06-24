package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

public class ToggleCommand implements SimpleCommand {
    private final MessagesFile messages;

    public ToggleCommand(Withdrawer plugin) {
        this.messages = plugin.getMessages();
    }

    @Override
    public Map<Integer, List<String>> tabCompleteWildcards() {
        return Map.of(1, List.of("actionbar", "less-intrusive", "prefix"));
    }

    @Override
    public String getPermission() {
        return "withdrawer.toggle";
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

        switch (type) {
            case "actionbar" -> isToggled = messages.toggleActionBar();
            case "less-intrusive" -> isToggled = messages.toggleLessIntrusive();
            case "prefix" -> isToggled = messages.togglePrefix();
        }

        if (isToggled) {
            messages.sendMessage(sender, "global.feature-on", (message) -> message.replace("%feature%", type));
        } else {
            messages.sendMessage(sender, "global.feature-off", (message) -> message.replace("%feature%", type));
        }

    }
}
