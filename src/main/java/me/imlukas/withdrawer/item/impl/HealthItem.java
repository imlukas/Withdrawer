package me.imlukas.withdrawer.item.impl;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.utils.HealthUtil;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HealthItem extends WithdrawableItem {

    public HealthItem(Withdrawer plugin, NBTItem nbtItem) {
        super(plugin, nbtItem);
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
        if (!setupWithdraw(player)) {
            return;
        }

        HealthUtil.removeHealth(player, amount * getValue());
        sendWithdrawInteractions(player, amount * getValue());
    }

    @Override
    public void gift(Player gifter, Player target) {
        if (!setupGift(gifter, target)) {
            return;
        }

        HealthUtil.addHealth(target, getValue());
        sendGiftedInteractions(gifter, target, getValue());
    }

    @Override
    public void redeem(Player player, boolean isShift) {
        int totalValue = setupRedeem(player, isShift);

        if (totalValue == 0) {
            return;
        }

        HealthUtil.addHealth(player, totalValue);
        sendRedeemInteractions(player, totalValue);
    }
}
