package me.imlukas.withdrawer.v2.item.impl;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.constant.ItemType;
import me.imlukas.withdrawer.v2.item.WithdrawableItem;
import org.bukkit.inventory.ItemStack;

public class ExpItem implements WithdrawableItem {
    private final Withdrawer plugin;
    private final int value;

    public ExpItem(Withdrawer plugin, int value) {
        this.plugin = plugin;
        this.value = value;
    }

    @Override
    public ItemStack getItem() {
        return plugin.getDefaultItemsHandler().getExpItem();
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public ItemType getType() {
        return ItemType.EXPBOTTLE;
    }

    @Override
    public void withdraw() {

    }

    @Override
    public void redeem() {

    }
}
