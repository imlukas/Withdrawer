package me.imlukas.withdrawer.economy.impl;

import me.imlukas.withdrawer.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.entity.Player;

public class PlayerPointsAdapter implements Economy {

    private final PlayerPointsAPI playerPointsAPI;

    public PlayerPointsAdapter(PlayerPointsAPI playerPointsAPI) {
        this.playerPointsAPI = playerPointsAPI;
    }

    @Override
    public String getIdentifier() {
        return "playerpoints";
    }

    @Override
    public String getCurrencySymbol() {
        return "Points";
    }

    @Override
    public double getBalance(Player player) {
        return playerPointsAPI.look(player.getUniqueId());
    }

    @Override
    public void withdrawFrom(Player player, double amount) {
        playerPointsAPI.take(player.getUniqueId(), (int) amount);
    }

    @Override
    public void giveTo(Player player, double amount) {
        playerPointsAPI.give(player.getUniqueId(), (int) amount);
    }
}
