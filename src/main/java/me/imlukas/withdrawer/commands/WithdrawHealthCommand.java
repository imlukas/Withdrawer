package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.config.DefaultItemsHandler;
import me.imlukas.withdrawer.economy.EconomyManager;
import me.imlukas.withdrawer.economy.IEconomy;
import me.imlukas.withdrawer.events.WithdrawEvent;
import me.imlukas.withdrawer.item.impl.HealthItem;
import me.imlukas.withdrawer.item.registry.WithdrawableItemsStorage;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import me.imlukas.withdrawer.utils.text.Placeholder;
import me.imlukas.withdrawer.utils.text.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class WithdrawHealthCommand implements SimpleCommand {

    private final Withdrawer plugin;
    private final MessagesFile messages;
    private final DefaultItemsHandler itemsHandler;

    public WithdrawHealthCommand(Withdrawer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.itemsHandler = plugin.getDefaultItemsHandler();
    }

    @Override
    public String getIdentifier() {
        return "withdraw.hp.*.*";
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

        HealthItem hpItem = new HealthItem(plugin, UUID.randomUUID(), value, amount);

        if (!checkValues(player, value * amount, hpItem.getConfigName())) {
            return;
        }

        WithdrawEvent withdrawEvent = new WithdrawEvent(player, hpItem);
        Bukkit.getPluginManager().callEvent(withdrawEvent);

        if (withdrawEvent.isCancelled()) {
            return;
        }

        hpItem.withdraw(player);
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
