package me.imlukas.withdrawer.economy.impl;

import me.imlukas.withdrawer.economy.Economy;
import org.bukkit.entity.Player;

public class VaultAdapter implements Economy {

    private final net.milkbowl.vault.economy.Economy vaultEconomy;

    public VaultAdapter(net.milkbowl.vault.economy.Economy economy) {
        this.vaultEconomy = economy;
    }

    @Override
    public String getIdentifier() {
        return "vault";
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
