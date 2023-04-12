package me.imlukas.withdrawer.v2.item.impl;


import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.constant.ItemType;
import me.imlukas.withdrawer.v2.economy.IEconomy;
import me.imlukas.withdrawer.v2.item.WithdrawableItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BankNoteItem implements WithdrawableItem {

    private final Withdrawer plugin;
    private final UUID uuid;
    private final int value;
    private final IEconomy economy;

    public BankNoteItem(Withdrawer plugin, int value, IEconomy economy) {
        this.plugin = plugin;
        this.uuid = UUID.randomUUID();
        this.value = value;
        this.economy = economy;
    }

    public IEconomy getEconomy() {
        return economy;
    }

    @Override
    public UUID getUuid() { return uuid; }

    @Override
    public ItemStack getItem() {
        return plugin.getDefaultItemsHandler().getItem(getType().getLowercase());
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
    public void withdraw(Player player) {

    }

    @Override
    public void redeem(Player player) {

    }
}
