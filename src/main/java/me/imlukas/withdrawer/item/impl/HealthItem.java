package me.imlukas.withdrawer.item.impl;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.item.WithdrawableItem;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HealthItem extends WithdrawableItem {
    private final Withdrawer plugin;

    public HealthItem(Withdrawer plugin, NBTItem nbtItem) {
        super(plugin, nbtItem);
        this.plugin = plugin;
    }

    public HealthItem(Withdrawer plugin, UUID uuid, int value, int amount) {
        super(plugin, uuid, value, amount);
        this.plugin = plugin;
    }

    @Override
    public String getConfigName() {
        return "health";
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
