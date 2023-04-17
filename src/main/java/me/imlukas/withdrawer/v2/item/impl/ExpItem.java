package me.imlukas.withdrawer.v2.item.impl;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.constant.ItemType;
import me.imlukas.withdrawer.v2.item.ItemStackWrapper;
import me.imlukas.withdrawer.v2.item.WithdrawableItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ExpItem implements WithdrawableItem {
    private final Withdrawer plugin;
    private final int value, amount;
    private final UUID uuid;
    private final boolean isGifted;

    public ExpItem(Withdrawer plugin, int value, int amount) {
        this.plugin = plugin;
        this.value = value;
        this.amount = amount;
        this.uuid = UUID.randomUUID();
        this.isGifted = false;

        getWrappedItem().setValue(value);
        getWrappedItem().setUUID(uuid);
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public ItemStackWrapper getWrappedItem() {
        return plugin.getDefaultItemsHandler().getWrapper(ItemType.EXP);
    }

    @Override
    public ItemStack getItemStack() {
        return getWrappedItem().getItemStack();
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public ItemType getType() {
        return ItemType.EXP;
    }

    @Override
    public boolean isGifted() {
        return isGifted;
    }

    @Override
    public void withdraw(Player player) {

    }

    @Override
    public void redeem(Player player, boolean isShift) {

    }
}
