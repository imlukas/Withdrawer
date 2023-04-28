package me.imlukas.withdrawer.item.impl;


import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.economy.IEconomy;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.utils.messages.MessagesFile;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MoneyItem extends WithdrawableItem {

    private final Withdrawer plugin;
    private final MessagesFile messages;
    private final IEconomy economy;
    private final int value = getValue();


    public MoneyItem(Withdrawer plugin, NBTItem item) {
        super(plugin, item);

        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.economy = plugin.getEconomyManager().getFirstEconomy();
    }

    public MoneyItem(Withdrawer plugin, UUID uuid, int value, int amount, IEconomy economy) {
        super(plugin, uuid, value, amount);
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.economy = economy;
    }

    public IEconomy getEconomy() {
        return economy;
    }

    @Override
    public String getConfigName() {
        return "money";
    }

    @Override
    public void withdraw(Player player) {
        if (!economy.hasMoney(player, value)) {
            messages.sendMessage(player, "banknote.no-money");
            return;
        }

        economy.withdrawFrom(player, value);

        player.getInventory().addItem(getItemStack());

        if (isGifted()) {
            messages.sendMessage(player, "banknote.gifted");
        }
    }

    @Override
    public void gift(Player gifter, Player target) {
        setAsGifted(true);
        if (!economy.hasMoney(gifter, value)) {
            messages.sendMessage(gifter, getConfigName() + ".no-money");
            return;
        }

        economy.withdrawFrom(gifter, value);

        target.getInventory().addItem(getItemStack());
        messages.sendMessage(target, getConfigName() + ".gifted");
    }

    @Override
    public void redeem(Player player, boolean isShift) {
        int totalAmount = value;
        String currencySign = getEconomy().getCurrencySymbol();

        if (isShift || amount == 1) {
            totalAmount = amount * value;
            plugin.getWithdrawableItemsStorage().removeItem(this);
            player.getInventory().removeItem(getItemStack());
        }

        if (amount > 1 && !isShift) {
            amount--;
            player.getInventory().getItemInMainHand().setAmount(amount);
        }

        System.out.println("Redeeming " + totalAmount + " " + currencySign + " to " + player.getName());
        System.out.println(getWrappedItem().getAmount());
        economy.giveTo(player, totalAmount);
        messages.getAutomatedMessages().sendRedeemMessage(player, getConfigName(), totalAmount, currencySign);
    }
}
