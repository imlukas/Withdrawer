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
        setWithdrawPredicate(player -> HealthUtil.checkHealth(player, getValue()));
    }

    public HealthItem(Withdrawer plugin, UUID uuid, int value, int amount) {
        super(plugin, uuid, value, amount);
        setWithdrawPredicate(player -> HealthUtil.checkHealth(player, getValue()));
    }

    @Override
    public String getConfigName() {
        return "health";
    }

    @Override
    public void withdraw(Player player) {
        if (!setupWithdraw(player)) {
            return;
        }
    }

    @Override
    public void gift(Player gifter, Player target) {
        if (!setupGift(gifter, target)) {
            return;
        }
    }

    @Override
    public void redeem(Player player, boolean isShift) {
        int totalValue = setupRedeem(player, isShift);

        if (totalValue == 0) {
            return;
        }
    }
}
