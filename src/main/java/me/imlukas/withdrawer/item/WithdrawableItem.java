package me.imlukas.withdrawer.item;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.item.wrapper.ItemStackWrapper;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public abstract class WithdrawableItem implements Withdrawable {

    private final Withdrawer plugin;
    private final ItemStackWrapper itemStackWrapper;
    private final UUID uuid;
    private final int value;

    protected int amount;
    private boolean isGifted = false;

    protected WithdrawableItem(Withdrawer withdrawer, NBTItem nbtItem) {
        this.plugin = withdrawer;
        this.uuid = nbtItem.getUUID("withdrawer-uuid");
        this.value = nbtItem.getInteger("withdrawer-value");
        this.amount = nbtItem.getItem().getAmount();

        this.itemStackWrapper = new ItemStackWrapper(nbtItem);
    }

    protected WithdrawableItem(Withdrawer withdrawer, UUID uuid, int value, int amount) {
        this.plugin = withdrawer;
        this.uuid = uuid;
        this.value = value;
        this.amount = amount;

        this.itemStackWrapper = plugin.getDefaultItemsHandler().createWrapper(getConfigName(), value, amount, uuid);
    }

    @Override
    public ItemStackWrapper getWrappedItem() {
        return itemStackWrapper;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStackWrapper.getItemStack();
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public boolean isGifted() {
        return isGifted;
    }

    public void setAsGifted(boolean isGifted) {
        this.isGifted = isGifted;
        getWrappedItem().setBoolean("withdrawer-gifted", isGifted);
    }
}
