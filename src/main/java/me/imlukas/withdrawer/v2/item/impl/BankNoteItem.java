package me.imlukas.withdrawer.v2.item.impl;


import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.constant.ItemType;
import me.imlukas.withdrawer.v2.item.WithdrawableItem;
import org.bukkit.inventory.ItemStack;

public class BankNoteItem implements WithdrawableItem {

    private final Withdrawer plugin;
    private final int value;

    public BankNoteItem(Withdrawer plugin, int value) {
        this.plugin = plugin;
        this.value = value;
    }

    @Override
    public ItemStack getItem() {
        return plugin.getConfigHandler().getBankNoteItem();
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public ItemType getType() {
        return ItemType.BANKNOTE;
    }

    @Override
    public void withdraw() {

    }

    @Override
    public void redeem() {

    }
}
