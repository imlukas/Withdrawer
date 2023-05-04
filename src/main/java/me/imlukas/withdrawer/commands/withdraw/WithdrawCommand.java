package me.imlukas.withdrawer.commands.withdraw;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.config.ItemHandler;
import me.imlukas.withdrawer.api.events.WithdrawEvent;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import me.imlukas.withdrawer.utils.text.Placeholder;
import me.imlukas.withdrawer.utils.text.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiFunction;

public class WithdrawCommand implements SimpleCommand {

    private final MessagesFile messages;
    private final ItemHandler itemsHandler;
    private final String identifier;
    private final BiFunction<Integer, Integer, WithdrawableItem> itemFunction;

    public WithdrawCommand(Withdrawer plugin, String identifier, BiFunction<Integer, Integer, WithdrawableItem> itemFunction) {
        this.messages = plugin.getMessages();
        this.itemsHandler = plugin.getItemHandler();
        this.identifier = identifier;
        this.itemFunction = itemFunction;
    }
    @Override
    public String getIdentifier() {
        return "withdraw." + identifier + ".*.*";
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

        while (amount > 64) {
            amount -= 64;
            giveItem(player, value, 64);
        }

        giveItem(player, value, amount);
    }

    private void giveItem(Player player, int value, int amount) {
        WithdrawableItem withdrawableItem = itemFunction.apply(amount, value);

        if (!checkValues(player, value * amount, withdrawableItem.getConfigName())) {
            return;
        }

        WithdrawEvent withdrawEvent = new WithdrawEvent(player, withdrawableItem);
        Bukkit.getPluginManager().callEvent(withdrawEvent);

        if (withdrawEvent.isCancelled()) {
            return;
        }

        withdrawableItem.withdraw(player);
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
