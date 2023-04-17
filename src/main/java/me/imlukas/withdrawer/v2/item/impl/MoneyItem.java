package me.imlukas.withdrawer.v2.item.impl;


import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.constant.ItemType;
import me.imlukas.withdrawer.v2.economy.EconomyManager;
import me.imlukas.withdrawer.v2.economy.IEconomy;
import me.imlukas.withdrawer.v2.item.ItemStackWrapper;
import me.imlukas.withdrawer.v2.item.WithdrawableItem;
import me.imlukas.withdrawer.v2.utils.storage.MessagesFile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MoneyItem implements WithdrawableItem {

    private final Withdrawer plugin;
    private final MessagesFile messages;
    private final UUID uuid;
    private final int value;
    private final IEconomy economy;
    private final boolean isGifted;

    private int amount;

    public MoneyItem(Withdrawer plugin, int value, int amount, IEconomy economy) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.uuid = UUID.randomUUID();
        this.value = value;
        this.amount = amount;
        this.economy = economy;
        this.isGifted = false;

        getWrappedItem().setValue(value);
        getWrappedItem().setUUID(uuid);
    }

    public IEconomy getEconomy() {
        return economy;
    }

    @Override
    public UUID getUuid() { return uuid; }

    @Override
    public ItemStackWrapper getWrappedItem() {
        return plugin.getDefaultItemsHandler().getWrapper(getType());
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
        return ItemType.BANKNOTE;
    }

    @Override
    public boolean isGifted() {
        return isGifted;
    }

    @Override
    public void withdraw(Player player) {
        if (!economy.hasMoney(player, value)) {
            messages.sendMessage(player, "banknote.no-money");
            return;
        }

        economy.withdrawFrom(player, value);

        player.getInventory().addItem(getItemStack());

        if (isGifted) {
            messages.sendMessage(player, "banknote.gifted");
        }
    }

    @Override
    public void redeem(Player player, boolean isShift) {
        economy.giveTo(player, value);

        if (isShift) {
            plugin.getWithdrawableItemsRegistry().removeItem(this);
        }

        if (amount == 0) {
            plugin.getWithdrawableItemsRegistry().removeItem(this);
        }

        amount--;
    }
}
