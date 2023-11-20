package me.imlukas.withdrawer.economy.impl;

import me.imlukas.withdrawer.economy.Economy;
import me.realized.tokenmanager.api.TokenManager;
import org.bukkit.entity.Player;

public class TokensAdapter implements Economy {
    private final TokenManager tokenManager;

    public TokensAdapter(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public String getIdentifier() {
        return "tokens";
    }

    @Override
    public String getCurrencySymbol() {
        return "Tokens";
    }

    @Override
    public double getBalance(Player player) {
        return tokenManager.getTokens(player).getAsLong();
    }

    @Override
    public void withdrawFrom(Player player, double amount) {
        tokenManager.removeTokens(player, (int) amount);
    }

    @Override
    public void giveTo(Player player, double amount) {
        tokenManager.addTokens(player, (int) amount);
    }
}
