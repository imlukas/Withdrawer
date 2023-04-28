package me.imlukas.withdrawer.item;

import me.imlukas.withdrawer.item.wrapper.ItemStackWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface Withdrawable {

    UUID getUuid();

    ItemStackWrapper getWrappedItem();
    ItemStack getItemStack();
    int getValue();

    int getAmount();

    String getConfigName();

    boolean isGifted();

    public void withdraw(Player player);

    public void gift(Player gifter, Player target);

    public void redeem(Player player, boolean isShift);
}
