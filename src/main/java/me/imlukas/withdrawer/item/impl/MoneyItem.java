package me.imlukas.withdrawer.item.impl;


import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.economy.IEconomy;
import me.imlukas.withdrawer.item.WithdrawableItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MoneyItem extends WithdrawableItem {

    private final IEconomy economy;
    private final int value = getValue();

    public MoneyItem(Withdrawer plugin, NBTItem item) {
        super(plugin, item);
        this.economy = plugin.getEconomyManager().getEconomy(item.getString("withdrawer-economy"));
        setWithdrawPredicate(player -> economy.hasMoney(player, value));
        getItemPlaceholders().addPlaceholder("currency", economy.getCurrencySymbol());
    }

    public MoneyItem(Withdrawer plugin, UUID uuid, int value, int amount, IEconomy economy) {
        super(plugin, uuid, value, amount);
        this.economy = economy;
        getNBTItem().setString("withdrawer-economy", economy.getIdentifier());
        applyNBT();
        setWithdrawPredicate(player -> economy.hasMoney(player, value));
        getItemPlaceholders().addPlaceholder("currency", economy.getCurrencySymbol());
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
        int totalValue = value * getAmount();
        if (!setupWithdraw(player)) {
            return;
        }

        economy.withdrawFrom(player, totalValue);
        sendWithdrawInteractions(player, totalValue, economy.getCurrencySymbol());
    }

    @Override
    public void gift(Player gifter, Player target) {
        int totalValue = value * getAmount();
        if (!setupGift(gifter, target)) {
            return;
        }

        economy.withdrawFrom(gifter, totalValue);
        sendGiftedInteractions(gifter, target, totalValue, economy.getCurrencySymbol());
    }

    @Override
    public void redeem(Player player, boolean isShift) {
        int totalValue = setupRedeem(player, isShift);

        if (totalValue == 0) {
            return;
        }

        economy.giveTo(player, totalValue);
        sendRedeemInteractions(player, totalValue, getEconomy().getCurrencySymbol());
    }
}
