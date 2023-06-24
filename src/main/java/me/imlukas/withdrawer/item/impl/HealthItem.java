package me.imlukas.withdrawer.item.impl;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.utils.HealthUtil;
import me.imlukas.withdrawer.utils.pdc.PDCWrapper;
import org.bukkit.entity.Player;

public class HealthItem extends WithdrawableItem {

    public HealthItem(Withdrawer plugin, PDCWrapper pdcWrapper) {
        super(plugin, pdcWrapper);
    }

    public HealthItem(Withdrawer plugin, int value, int amount) {
        super(plugin, value, amount);
    }

    @Override
    public String getConfigName() {
        return "hp";
    }

    @Override
    public boolean canWithdraw(Player player) {
        return HealthUtil.checkHealth(player, amount * getValue());
    }

    @Override
    public void withdraw(Player player) {
        if (!preparator.setupWithdraw(player)) {
            return;
        }

        HealthUtil.removeHealth(player, amount * getValue());
        interactions.sendWithdrawInteractions(player, amount * getValue());
    }

    @Override
    public void gift(Player gifter, Player target) {
        if (!preparator.setupGift(gifter, target)) {
            return;
        }

        HealthUtil.addHealth(target, getValue());
        interactions.sendGiftedInteractions(gifter, target, getValue());
    }

    @Override
    public void redeem(Player player, boolean isShift) {
        int totalValue = preparator.setupRedeem(player, isShift);

        if (totalValue == 0) {
            return;
        }

        HealthUtil.addHealth(player, totalValue);
        interactions.sendRedeemInteractions(player, totalValue);
    }

    @Override
    public void give(Player player, int amount) {
        addItem(player);
        interactions.sendGiveInteractions(player, amount);
    }
}
