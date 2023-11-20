package me.imlukas.withdrawer.v3.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Withdrawable {

    /**
     * Returns the associated {@link ItemStack} of this {@link Withdrawable}.
     * This ItemStack is not a clone.
     */
    ItemStack getAssociatedItem();

    /**
     * The base value associated with this {@link Withdrawable}.
     * @return the base value, always >1
     */
    int getBaseValue();

    /**
     * @return the name used on config files
     */
    String getConfigName();

    /**
     * Checks if a player can withdraw the {@link Withdrawable}.
     * @param player the player to check
     * @return true if the player can withdraw, false otherwise
     */
    boolean canWithdraw(Player player);

    /**
     * Handles withdrawing of the {@link Withdrawable}.
     * @param player the player to withdraw from
     */
    void withdraw(Player player);

    /**
     * Handles redeeming of the {@link Withdrawable}.
     */
    void redeem(Player player, boolean isShift);

    void give(Player player);

    void gift(Player gifter, Player target, int amount);
}
