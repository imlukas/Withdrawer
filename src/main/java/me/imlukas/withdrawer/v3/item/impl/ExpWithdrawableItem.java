package me.imlukas.withdrawer.v3.item.impl;

import me.imlukas.withdrawer.WithdrawerPlugin;
import me.imlukas.withdrawer.utils.ExpUtil;
import me.imlukas.withdrawer.utils.HealthUtil;
import me.imlukas.withdrawer.v3.item.BaseWithdrawableItem;
import me.imlukas.withdrawer.v3.item.interaction.InteractionType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ExpWithdrawableItem extends BaseWithdrawableItem {

    protected ExpWithdrawableItem(WithdrawerPlugin plugin, ItemStack itemStack) {
        super(plugin, itemStack);
    }

    @Override
    public String getConfigName() {
        return "exp";
    }

    @Override
    public boolean canWithdraw(Player player) {
        return ExpUtil.hasExp(player, getTotalValue()) && checkPermission(player, "withdraw");
    }

    @Override
    public void withdraw(Player player) {
        String configName = getConfigName();

        if (!canWithdraw(player)) {
            messages.sendMessage(player, configName + ".no-" + configName);
            return;
        }

        ExpUtil.changeExp(player, -getTotalValue());

        messages.sendInteractionMessage(player, InteractionType.WITHDRAW, getConfigName(), Map.of("%value%", String.valueOf(getTotalValue())));
        sounds.playSound(player, configName + ".withdraw");
    }

    @Override
    public void redeem(Player player, boolean isShift) {
        String configName = getConfigName();

        int totalValue = setupRedeem(player, isShift);

        if (totalValue == 0) {
            return;
        }

        if (plugin.getPluginSettings().isXpDropable()) {
            ExpUtil.dropExp(player, totalValue);
        } else {
            ExpUtil.changeExp(player, totalValue);
        }

        messages.sendInteractionMessage(player, InteractionType.REDEEM, getConfigName(), Map.of("%value%", String.valueOf(totalValue)));
        sounds.playSound(player, configName + ".redeem");
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

        ExpUtil.changeExp(gifter, -getTotalValue());

        messages.sendInteractionMessage(target, InteractionType.GIFT, getConfigName(), Map.of("%value%", String.valueOf(getTotalValue())));
        sounds.playSound(target, configName + ".gifted");

        target.getInventory().addItem(getAssociatedItem());
    }
}
