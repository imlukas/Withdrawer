package me.imlukas.withdrawer.commands.give;

import me.imlukas.withdrawer.WithdrawerPlugin;
import me.imlukas.withdrawer.api.events.GiveEvent;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.interactions.Messages;
import me.imlukas.withdrawer.utils.text.TextUtils;
import me.imlukas.withdrawer.v3.item.BaseWithdrawableItem;
import me.imlukas.withdrawer.v3.item.Withdrawable;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class GiveCommand implements SimpleCommand {

    private final Messages messages;
    private final String identifier;
    private final BiFunction<Integer, Integer, BaseWithdrawableItem> itemFunction;

    public GiveCommand(WithdrawerPlugin plugin, String identifier, BiFunction<Integer, Integer, BaseWithdrawableItem> itemFunction) {
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
            giveItem(sender, target, value, 64);
        }

        giveItem(sender, target, value, amount);
    }

    private void giveItem(CommandSender sender, Player target, int value, int amount) {
        Withdrawable withdrawableItem = itemFunction.apply(value, amount);

        GiveEvent giveEvent = new GiveEvent(sender, target, withdrawableItem);

        Bukkit.getPluginManager().callEvent(giveEvent);

        if (giveEvent.isCancelled()) {
            return;
        }

        withdrawableItem.give(target, value * amount);
    }
}
