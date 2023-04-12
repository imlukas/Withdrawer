package me.imlukas.withdrawer.v2.commands;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.v2.economy.EconomyManager;
import me.imlukas.withdrawer.v2.economy.IEconomy;
import me.imlukas.withdrawer.v2.item.impl.BankNoteItem;
import me.imlukas.withdrawer.v2.item.registry.WithdrawableItemsRegistry;
import me.imlukas.withdrawer.v2.utils.command.SimpleCommand;
import me.imlukas.withdrawer.v2.utils.storage.MessagesFile;
import me.imlukas.withdrawer.v2.utils.text.TextUtils;
import org.bukkit.command.CommandSender;

public class WithdrawMoneyCommand implements SimpleCommand {

    private final Withdrawer plugin;
    private final MessagesFile messages;
    private final EconomyManager economyManager;
    private final WithdrawableItemsRegistry itemsRegistry;

    public WithdrawMoneyCommand(Withdrawer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.economyManager = plugin.getEconomyManager();
        this.itemsRegistry = plugin.getWithdrawableItemsRegistry();
    }
    @Override
    public String getIdentifier() {
        return "withdraw.money.*";
    }

    @Override
    public void execute(CommandSender sender, String... args) {

        int value = 0;
        if (args[0].isEmpty()) {
            messages.sendMessage(sender, "command.invalid-args");
        }

        value = TextUtils.parseInt(args[0], (integer -> integer > 0));

        int amount = 0;
        if (args[1].isEmpty()) {
            messages.sendMessage(sender, "command.invalid-args");
        }
        amount = TextUtils.parseInt(args[1], (integer -> integer > 0));

        IEconomy economy = economyManager.getFirstEconomy();
        if (!args[2].isEmpty()) {
            economy = getEconomy(args[0]);
        }

        BankNoteItem bankNoteItem = new BankNoteItem(plugin, value, economy);
        bankNoteItem.getItem().setAmount(amount);
        itemsRegistry.addItem(bankNoteItem);
    }

    private IEconomy getEconomy(String economy) {
        return economyManager.getEconomy(economy);
    }



}
