package me.imlukas.withdrawer.v2.item.impl;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.constant.ItemType;
import me.imlukas.withdrawer.v2.item.WithdrawableItem;
import org.bukkit.inventory.ItemStack;

public class HealthItem implements WithdrawableItem {
    private final Withdrawer plugin;
    private final int value;

    public HealthItem(Withdrawer plugin, int value) {
        this.plugin = plugin;
        this.value = value;
    }

    @Override
    public ItemStack getItem() {
        return plugin.getDefaultItemsHandler().getHealthItem();
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public ItemType getType() {
        return ItemType.HEALTH;
    }

    @Override
    public void withdraw() {

    }

    @Override
    public void redeem() {

    }
}
