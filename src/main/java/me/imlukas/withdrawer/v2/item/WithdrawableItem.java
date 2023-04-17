package me.imlukas.withdrawer.v2.item;

import me.imlukas.withdrawer.constant.ItemType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface WithdrawableItem {

    UUID getUuid();

    ItemStackWrapper getWrappedItem();
    ItemStack getItemStack();
    int getValue();

    int getAmount();
    ItemType getType();

    boolean isGifted();

    public void withdraw(Player player);

    public void redeem(Player player, boolean isShift);
}
