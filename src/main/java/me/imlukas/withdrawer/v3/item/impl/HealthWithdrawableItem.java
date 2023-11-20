package me.imlukas.withdrawer.v3.item.impl;

import me.imlukas.withdrawer.WithdrawerPlugin;
import me.imlukas.withdrawer.utils.HealthUtil;
import me.imlukas.withdrawer.v3.item.BaseWithdrawableItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HealthWithdrawableItem extends BaseWithdrawableItem {

    protected HealthWithdrawableItem(WithdrawerPlugin plugin, ItemStack itemStack) {
        super(plugin, itemStack);
    }

    @Override
    public String getConfigName() {
        return null;
    }

    @Override
    public boolean canWithdraw(Player player) {
        return HealthUtil.checkHealth(player, getTotalValue()) && checkPermission(player, "withdraw");
    }

    @Override
    public void withdraw(Player player) {
        String configName = getConfigName();

        if (!canWithdraw(player)) {
            messages.sendMessage(player, configName + ".no-" + configName);
            return;
        }

        HealthUtil.removeHealth(player, getTotalValue());

        messages.getAutomatedMessages().sendWithdrawMessage(player, configName, getTotalValue());
        sounds.playSound(player, configName + ".withdraw");
    }


    @Override
    public void redeem(Player player, boolean isShift) {
        String configName = getConfigName();

        int totalValue = setupRedeem(player, isShift);

        if (totalValue == 0) {
            return;
        }

        HealthUtil.addHealth(player, totalValue);

        messages.getAutomatedMessages().sendRedeemMessage(player, configName, totalValue);
        sounds.playSound(player, configName + ".redeem");
    }

    @Override
    public void give(Player target) {
        super.give(target);
        messages.getAutomatedMessages().sendGiveMessage(target, getConfigName(), getTotalValue());
    }

    @Override
    public void gift(Player gifter, Player target, int amount) {
        String configName = getConfigName();

        if (!checkPermission(gifter, "gift")) {
            return;
        }

        if (!canWithdraw(gifter)) {
            messages.sendMessage(gifter, configName + ".no-" + configName);
            return;
        }

        HealthUtil.removeHealth(gifter, getTotalValue());

        messages.getAutomatedMessages().sendGiftedMessage(target, gifter, configName, getTotalValue());
        sounds.playSound(target, configName + ".gifted");

        target.getInventory().addItem(getAssociatedItem());
    }
}
