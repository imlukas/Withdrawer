package me.imlukas.withdrawer.item;

import me.imlukas.withdrawer.item.preparator.WithdrawablePreparator;
import me.imlukas.withdrawer.item.item.ItemWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface Withdrawable {

    UUID getUUID();

    WithdrawablePreparator getPreparator();
    ItemWrapper getItemWrapper();

    ItemStack getDisplayItem(boolean clone);

    int getAmount();

    int getValue();

    String getConfigName();

    void setAmount(int amount);

    void setAsGifted(boolean isGifted);

    boolean isGifted();

    boolean canWithdraw(Player player);

    void withdraw(Player player);

    void gift(Player gifter, Player target);

    void redeem(Player player, boolean isShift);

    void give(Player player, int amount);
}
