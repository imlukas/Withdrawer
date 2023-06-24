package me.imlukas.withdrawer.item.impl;


import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.economy.IEconomy;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.utils.pdc.PDCWrapper;
import org.bukkit.entity.Player;

public class MoneyItem extends WithdrawableItem {

    private final IEconomy economy;
    private final int value = getValue();

    public MoneyItem(Withdrawer plugin, PDCWrapper pdcWrapper) {
        super(plugin, pdcWrapper);
        this.economy = plugin.getEconomyManager().getEconomy(pdcWrapper.getString("withdrawer-economy"));
        getItemPlaceholders().addPlaceholder("currency", economy.getCurrencySymbol());
    }

    public MoneyItem(Withdrawer plugin, int value, int amount, IEconomy economy) {
        super(plugin, value, amount);
        this.economy = economy;
        getPDCWrapper().setString("withdrawer-economy", economy.getIdentifier());
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
    public boolean canWithdraw(Player player) {
        return economy.hasMoney(player, amount * value);
    }

    @Override
    public void withdraw(Player player) {
        int totalValue = value * getAmount();
        if (!preparator.setupWithdraw(player)) {
            return;
        }

        economy.withdrawFrom(player, totalValue);
        interactions.sendWithdrawInteractions(player, totalValue, economy.getCurrencySymbol());
    }

    @Override
    public void gift(Player gifter, Player target) {
        int totalValue = value * getAmount();
        if (!preparator.setupGift(gifter, target)) {
            return;
        }

        economy.withdrawFrom(gifter, totalValue);
        interactions.sendGiftedInteractions(gifter, target, totalValue, economy.getCurrencySymbol());
    }

    @Override
    public void redeem(Player player, boolean isShift) {
        int totalValue = preparator.setupRedeem(player, isShift);

        if (totalValue == 0) {
            return;
        }

        economy.giveTo(player, totalValue);
        interactions.sendRedeemInteractions(player, totalValue, getEconomy().getCurrencySymbol());
    }

    @Override
    public void give(Player player, int amount) {
        addItem(player);
        interactions.sendGiveInteractions(player, amount, getEconomy().getCurrencySymbol());
    }
}
