package me.imlukas.withdrawer.item.impl;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.utils.ExpUtil;
import me.imlukas.withdrawer.utils.pdc.PDCWrapper;
import org.bukkit.entity.Player;

public class ExpItem extends WithdrawableItem {

    private final int value = getValue();

    public ExpItem(Withdrawer plugin, PDCWrapper pdcWrapper) {
        super(plugin, pdcWrapper);
    }

    public ExpItem(Withdrawer plugin, int value, int amount) {
        super(plugin, value, amount);
    }

    @Override
    public String getConfigName() {
        return "exp";
    }

    @Override
    public boolean canWithdraw(Player player) {
        return ExpUtil.hasExp(player, amount * value);
    }

    @Override
    public void withdraw(Player player) {
        int totalValue = value * amount;
        if (!preparator.setupWithdraw(player)) {
            return;
        }

        ExpUtil.changeExp(player, -totalValue);
        interactions.sendWithdrawInteractions(player, totalValue);
    }

    @Override
    public void gift(Player gifter, Player target) {
        int totalValue = value * amount;
        if (!preparator.setupGift(gifter, target)) {
            return;
        }

        ExpUtil.changeExp(gifter, -totalValue);
        interactions.sendGiftedInteractions(gifter, target, totalValue);
    }

    @Override
    public void redeem(Player player, boolean isShift) {
        int totalValue = preparator.setupRedeem(player, isShift);

        if (totalValue == 0) {
            return;
        }

        if (plugin.getPluginSettings().isXpDropable()) {
            ExpUtil.dropExp(player, totalValue);
        } else {
            ExpUtil.changeExp(player, totalValue);
        }
        interactions.sendRedeemInteractions(player, totalValue);
    }

    @Override
    public void give(Player player, int amount) {
        addItem(player);
        interactions.sendGiveInteractions(player, amount);
    }

}
