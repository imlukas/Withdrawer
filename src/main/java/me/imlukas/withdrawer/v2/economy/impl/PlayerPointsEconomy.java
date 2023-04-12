package me.imlukas.withdrawer.v2.economy.impl;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.v2.economy.IEconomy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.entity.Player;

public class PlayerPointsEconomy implements IEconomy {

    private final PlayerPointsAPI playerPointsAPI;

    public PlayerPointsEconomy(PlayerPointsAPI playerPointsAPI) {
        this.playerPointsAPI = playerPointsAPI;
    }

    @Override
    public String getIdentifier() {
        return "PlayerPoints";
    }

    @Override
    public String getCurrencySymbol() {
        return "PP";
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
