package me.imlukas.withdrawer.item.impl;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.item.wrapper.ItemStackWrapper;
import me.imlukas.withdrawer.item.Withdrawable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ExpItem extends WithdrawableItem {
    private final Withdrawer plugin;

    public ExpItem(Withdrawer plugin, NBTItem nbtItem) {
        super(plugin, nbtItem);
        this.plugin = plugin;
    }

    public ExpItem(Withdrawer plugin, UUID uuid, int value, int amount) {
        super(plugin, uuid, value, amount);
        this.plugin = plugin;
    }

    @Override
    public String getConfigName() {
        return "exp";
    }

    @Override
    public void withdraw(Player player) {

    }

    @Override
    public void gift(Player gifter, Player target) {

    }

    @Override
    public void redeem(Player player, boolean isShift) {

    }
}
