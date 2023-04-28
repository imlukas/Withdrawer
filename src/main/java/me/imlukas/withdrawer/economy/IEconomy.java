package me.imlukas.withdrawer.economy;

import org.bukkit.entity.Player;

public interface IEconomy {


    public String getIdentifier();

    public String getCurrencySymbol();

    public default boolean hasMoney(Player player, double amount) {
        return getBalance(player) >= amount;
    }

    public double getBalance(Player player);

    public void withdrawFrom(Player player, double amount);

    public void giveTo(Player player, double amount);
}
