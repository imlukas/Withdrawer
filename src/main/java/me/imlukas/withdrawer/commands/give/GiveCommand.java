package me.imlukas.withdrawer.commands.give;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import me.imlukas.withdrawer.utils.text.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class GiveCommand implements SimpleCommand {

    private final MessagesFile messages;
    private final String identifier;
    private final BiFunction<Integer, Integer, WithdrawableItem> itemFunction;

    public GiveCommand(Withdrawer plugin, String identifier, BiFunction<Integer, Integer, WithdrawableItem> itemFunction) {
        this.messages = plugin.getMessages();
        this.identifier = identifier;
        this.itemFunction = itemFunction;
    }

    @Override
    public Map<Integer, List<String>> tabCompleteWildcards() {
        return Map.of(1, List.of("10", "100", "1000"));
    }

    @Override
    public String getIdentifier() {
        return "wd.give." + identifier + ".*.*.*";
    }

    @Override
    public void execute(CommandSender sender, String... args) {
        if (args[0].isEmpty()) {
            messages.sendMessage(sender, "command.invalid-arguments");
            return;
        }

        if (sender instanceof Player player) {
            if (!player.hasPermission("withdrawer.give.*")) {
                if (!player.hasPermission("withdrawer.give." + identifier)) {
                    messages.sendMessage(player, "command.no-permission");
                    return;
                }
            }
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            messages.sendMessage(sender, "command.player-not-found");
            return;
        }

        int value = 1;
        if (!args[1].isEmpty()) {
            value = TextUtils.parseInt(args[1], (integer -> integer > 0));
        }

        int amount = 1;
        if (!args[2].isEmpty()) {
            amount = TextUtils.parseInt(args[2], (integer -> integer > 0));
    }

        while (amount > 64) {
            amount -= 64;
            giveItem(target, value, 64);
        }

        giveItem(target, value, amount);
    }

    private void giveItem(Player target, int value, int amount) {
        WithdrawableItem withdrawableItem = itemFunction.apply(value, amount);
        withdrawableItem.give(target, value * amount);


    }
}
