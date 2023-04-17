package me.imlukas.withdrawer.v2.item.impl;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.constant.ItemType;
import me.imlukas.withdrawer.v2.item.ItemStackWrapper;
import me.imlukas.withdrawer.v2.item.WithdrawableItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class HealthItem implements WithdrawableItem {
    private final Withdrawer plugin;
    private final int value;
    private final int amount;
    private final UUID uuid;
    private final boolean isGifted;

    public HealthItem(Withdrawer plugin, int value, int amount) {
        this.plugin = plugin;
        this.uuid = UUID.randomUUID();
        this.value = value;
        this.amount = amount;
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
        return plugin.getDefaultItemsHandler().getWrapper(ItemType.HEALTH);
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
        return ItemType.HEALTH;
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
