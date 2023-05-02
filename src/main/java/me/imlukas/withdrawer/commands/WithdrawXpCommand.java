package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.config.DefaultItemsHandler;
import me.imlukas.withdrawer.events.WithdrawEvent;
import me.imlukas.withdrawer.item.impl.ExpItem;
import me.imlukas.withdrawer.utils.ExpUtil;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import me.imlukas.withdrawer.utils.text.Placeholder;
import me.imlukas.withdrawer.utils.text.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WithdrawXpCommand implements SimpleCommand {

    private final Withdrawer plugin;
    private final MessagesFile messages;
    private final DefaultItemsHandler itemsHandler;

    public WithdrawXpCommand(Withdrawer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.itemsHandler = plugin.getDefaultItemsHandler();
    }

    @Override
    public String getIdentifier() {
        return "withdraw.xp.*.*.*";
    }

    @Override
    public Map<Integer, List<String>> tabCompleteWildcards() {
        return Map.of(2, List.of("xp", "level"));
    }

    @Override
    public void execute(CommandSender sender, String... args) {
        Player player = (Player) sender;
        int value = 1;
        if (!args[0].isEmpty()) {
            value = TextUtils.parseInt(args[0], (integer -> integer > 0));
        }

        int amount = 1;
        if (!args[1].isEmpty()) {
            amount = TextUtils.parseInt(args[1], (integer -> integer > 0));
        }

        String type = "xp";
        if (!args[2].isEmpty()) {
            type = args[2];
        }

        if (type.equals("level")) {
            value = ExpUtil.getExpFromLevel(value);
        }

        ExpItem expItem = new ExpItem(plugin, UUID.randomUUID(), value, amount);

        if (!checkValues(player, value * amount, expItem.getConfigName())) {
            return;
        }

        WithdrawEvent withdrawEvent = new WithdrawEvent(player, expItem);
        Bukkit.getPluginManager().callEvent(withdrawEvent);

        if (withdrawEvent.isCancelled()) {
            return;
        }

        expItem.withdraw(player);
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
            messages.sendMessage(player, "command.invalid-value", placeholders);
            return false;
        }

        return true;
    }
}
