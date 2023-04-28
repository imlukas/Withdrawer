package me.imlukas.withdrawer.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.economy.EconomyManager;
import me.imlukas.withdrawer.economy.IEconomy;
import me.imlukas.withdrawer.events.WithdrawEvent;
import me.imlukas.withdrawer.item.impl.MoneyItem;
import me.imlukas.withdrawer.item.registry.WithdrawableItemsStorage;
import me.imlukas.withdrawer.utils.command.SimpleCommand;
import me.imlukas.withdrawer.utils.messages.MessagesFile;
import me.imlukas.withdrawer.utils.text.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WithdrawMoneyCommand implements SimpleCommand {

    private final Withdrawer plugin;
    private final MessagesFile messages;
    private final EconomyManager economyManager;
    private final WithdrawableItemsStorage itemsRegistry;

    public WithdrawMoneyCommand(Withdrawer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.economyManager = plugin.getEconomyManager();
        this.itemsRegistry = plugin.getWithdrawableItemsStorage();
    }
    @Override
    public String getIdentifier() {
        return "withdraw.money.*.*.*";
    }

    @Override
    public void execute(CommandSender sender, String... args) {
        Player player = (Player) sender;
        int value = 0;
        if (args[0].isEmpty()) {
            messages.sendMessage(sender, "command.invalid-args");
        }

        value = TextUtils.parseInt(args[0], (integer -> integer > 0));

        int amount = 1;
        if (!args[1].isEmpty()) {
            amount = TextUtils.parseInt(args[1], (integer -> integer > 0));
        }

        IEconomy economy = economyManager.getFirstEconomy();
        if (!args[2].isEmpty()) {
            economy = getEconomy(args[0]);
        }

        MoneyItem moneyItem = new MoneyItem(plugin, UUID.randomUUID(), value, amount, economy);

        WithdrawEvent withdrawEvent = new WithdrawEvent(player, moneyItem);
        Bukkit.getPluginManager().callEvent(withdrawEvent);

        if (withdrawEvent.isCancelled()) {
            return;
        }

        itemsRegistry.addItem(moneyItem);
        moneyItem.withdraw(player);
    }

    private IEconomy getEconomy(String economy) {
        return economyManager.getEconomy(economy);
    }



}
