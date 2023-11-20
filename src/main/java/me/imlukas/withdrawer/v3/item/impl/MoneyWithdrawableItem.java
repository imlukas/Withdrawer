package me.imlukas.withdrawer.v3.item.impl;

import me.imlukas.withdrawer.WithdrawerPlugin;
import me.imlukas.withdrawer.economy.EconomyManager;
import me.imlukas.withdrawer.economy.Economy;
import me.imlukas.withdrawer.v3.item.BaseWithdrawableItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MoneyWithdrawableItem extends BaseWithdrawableItem {

    private final Economy economy;

    protected MoneyWithdrawableItem(WithdrawerPlugin plugin, ItemStack itemStack) {
        super(plugin, itemStack);
        EconomyManager economyManager = plugin.getEconomyManager();
        String economyIdentifier = getItemPDC().getString("withdrawer-economy");

        if (economyIdentifier == null) {
            throw new IllegalArgumentException("Item " + itemStack.getType() + " has no economy");
        }

        this.economy = economyManager.getEconomy(economyIdentifier);
    }

    @Override
    public String getConfigName() {
        return "money";
    }

    @Override
    public boolean canWithdraw(Player player) {
        return economy.hasMoney(player, getTotalValue()) && checkPermission(player, "withdraw");
    }

    @Override
    public void withdraw(Player player) {
        String configName = getConfigName();

        if (!canWithdraw(player)) {
            messages.sendMessage(player, configName + ".no-" + configName);
            return;
        }

        economy.withdrawFrom(player, getTotalValue());

        messages.getAutomatedMessages().sendWithdrawMessage(player, configName, getTotalValue(), economy.getCurrencySymbol());
        sounds.playSound(player, configName + ".withdraw");

        player.getInventory().addItem(getAssociatedItem());
    }


    @Override
    public void redeem(Player player, boolean isShift) {
        String configName = getConfigName();

        int totalValue = setupRedeem(player, isShift);

        if (totalValue == 0) {
            return;
        }

        economy.giveTo(player, totalValue);

        messages.getAutomatedMessages().sendRedeemMessage(player, configName, totalValue, economy.getCurrencySymbol());
        sounds.playSound(player, configName + ".redeem");
    }

    @Override
    public void give(Player target) {
        super.give(target);

        messages.getAutomatedMessages().sendGiveMessage(target, getConfigName(), getTotalValue(), economy.getCurrencySymbol());
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

        economy.withdrawFrom(gifter, getTotalValue());

        messages.getAutomatedMessages().sendGiftedMessage(target, gifter, configName, getTotalValue(), economy.getCurrencySymbol());
        sounds.playSound(target, configName + ".gifted");

        target.getInventory().addItem(getAssociatedItem());
    }
}
