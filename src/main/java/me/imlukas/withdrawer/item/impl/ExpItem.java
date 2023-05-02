package me.imlukas.withdrawer.item.impl;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.utils.ExpUtil;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ExpItem extends WithdrawableItem {

    private final int value = getValue();

    public ExpItem(Withdrawer plugin, NBTItem nbtItem) {
        super(plugin, nbtItem);
        setWithdrawPredicate(player -> ExpUtil.hasExp(player, value));
    }

    public ExpItem(Withdrawer plugin, UUID uuid, int value, int amount) {
        super(plugin, uuid, value, amount);
        setWithdrawPredicate(player -> ExpUtil.hasExp(player, value));
    }

    @Override
    public String getConfigName() {
        return "exp";
    }

    @Override
    public void withdraw(Player player) {
        int totalValue = value * amount;
        if (!setupWithdraw(player)) {
            return;
        }

        ExpUtil.changeExp(player, -totalValue);
        sendWithdrawInteractions(player, totalValue);
    }

    @Override
    public void gift(Player gifter, Player target) {
        int totalValue = value * amount;
        if (!setupGift(gifter, target)) {
            return;
        }

        ExpUtil.changeExp(gifter, -totalValue);
        messages.getAutomatedMessages().sendGiftedMessage(target, gifter, getConfigName(), totalValue);
    }

    @Override
    public void redeem(Player player, boolean isShift) {
        int totalValue = setupRedeem(player, isShift);

        if (totalValue == 0) {
            return;
        }

        ExpUtil.changeExp(player, totalValue);
        sendRedeemInteractions(player, totalValue);
    }

}
