package me.imlukas.withdrawer.economy.impl;

import me.imlukas.withdrawer.economy.IEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class Vault implements IEconomy {

    private final Economy vaultEconomy;

    public Vault(Economy economy) {
        this.vaultEconomy = economy;
    }

    @Override
    public String getIdentifier() {
        return "Vault";
    }

    @Override
    public String getCurrencySymbol() {
        return "$";
    }

    @Override
    public double getBalance(Player player) {
        return vaultEconomy.getBalance(player);
    }

    @Override
    public void withdrawFrom(Player player, double amount) {
        vaultEconomy.withdrawPlayer(player, amount);
    }

    @Override
    public void giveTo(Player player, double amount) {
        vaultEconomy.depositPlayer(player, amount);
    }
}
