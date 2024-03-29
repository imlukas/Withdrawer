package me.imlukas.withdrawer.commands.gift;

import me.imlukas.withdrawer.WithdrawerPlugin;
import me.imlukas.withdrawer.api.events.GiftEvent;
import me.imlukas.withdrawer.config.ItemHandler;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.interactions.Messages;
import me.imlukas.withdrawer.utils.text.Placeholder;
import me.imlukas.withdrawer.utils.text.TextUtils;
import me.imlukas.withdrawer.v3.item.Withdrawable;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class GiftCommand implements SimpleCommand {
    private final Messages messages;
    private final ItemHandler itemsHandler;
    private final String identifier;
    private final BiFunction<Integer, Integer, Withdrawable> itemFunction;

    public GiftCommand(WithdrawerPlugin plugin, String identifier, BiFunction<Integer, Integer, Withdrawable> itemFunction) {
        this.messages = plugin.getMessages();
        this.itemsHandler = plugin.getItemHandler();
        this.identifier = identifier;
        this.itemFunction = itemFunction;
    }

    @Override
    public Map<Integer, List<String>> tabCompleteWildcards() {
        return Map.of(1, List.of("10", "100", "1000"));
    }

    @Override
    public String getPermission() {
        return "withdrawer.gift." + identifier;
    }

    @Override
    public String getIdentifier() {
        return "wd.gift." + identifier + ".*.*.*";
    }

    @Override
    public void execute(CommandSender sender, String... args) {
        Player gifter = (Player) sender;

        if (args[0].isEmpty()) {
            messages.sendMessage(gifter, "command.invalid-arguments");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            messages.sendMessage(gifter, "command.player-not-found");
            return;
        }

        int value = 1;
        if (!args[1].isEmpty()) {
            value = TextUtils.parseInt(args[1], (integer -> integer > 0)).get();
        }

        int amount = 1;
        if (!args[2].isEmpty()) {
            amount = TextUtils.parseInt(args[2], (integer -> integer > 0)).get();
        }

        while (amount > 64) {
            amount -= 64;
            giveItem(gifter, target, value, 64);
        }

        giveItem(gifter, target, value, amount);
    }

    private void giveItem(Player gifter, Player target, int value, int amount) {
        Withdrawable withdrawableItem = itemFunction.apply(value, amount);

        if (!checkValues(gifter, value * amount, withdrawableItem.getConfigName())) {
            return;
        }

        GiftEvent giftEvent = new GiftEvent(gifter, target, withdrawableItem);
        Bukkit.getPluginManager().callEvent(giftEvent);

        if (giftEvent.isCancelled()) {
            return;
        }

        withdrawableItem.gift(gifter, target);
    }

    public boolean checkValues(Player player, int totalValue, String identifier) {
        if (player.hasPermission("withdrawer.bypass.minmax." + identifier)) {
            return true;
        }

        int min = itemsHandler.getMinValue(identifier);
        int max = itemsHandler.getMaxValue(identifier);

        List<Placeholder<Player>> placeholders = List.of(new Placeholder<>("min", String.valueOf(min)),
                new Placeholder<>("max", String.valueOf(max)));

        if (totalValue < min || totalValue > max) {
            messages.sendMessage(player, "command.invalid-values", placeholders);
            return false;
        }

        return true;
    }
}
