package me.imlukas.withdrawer.economy;

import org.bukkit.entity.Player;

public interface Economy {

    String getIdentifier();

    String getCurrencySymbol();

    default boolean hasMoney(Player player, double amount) {
        return getBalance(player) >= amount;
    }

    double getBalance(Player player);

    void withdrawFrom(Player player, double amount);

    void giveTo(Player player, double amount);
}
