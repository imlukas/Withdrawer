package me.imlukas.withdrawer.utils;

import me.imlukas.withdrawer.Withdrawer;
import me.realized.tokenmanager.api.TokenManager;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.entity.Player;

public class EconomyUtil {

    private final Economy econ;
    private final String economySystem;
    private final PlayerPointsAPI playerPointsAPI;
    private final TokenManager tokenManagerAPI;

    public EconomyUtil(Withdrawer main) {
        this.econ = main.getEconomy();
        this.playerPointsAPI = main.getPlayerPointsAPI();
        this.economySystem = main.getConfig().getString("economy-plugin");
        this.tokenManagerAPI = main.getTokenManagerAPI();
    }

    public boolean hasMoney(Player player, double amount) {
        return !(getMoney(player) < amount);
    }

    public double getMoney(Player player) {
        if (economySystem.equalsIgnoreCase("vault")) {
            return econ.getBalance(player);
        }
        if (economySystem.equalsIgnoreCase("playerpoints")) {
            return playerPointsAPI.look(player.getUniqueId());
        }
        if (economySystem.equalsIgnoreCase("tokenmanager")) {
            return tokenManagerAPI.getTokens(player).getAsLong();
        }
        return 0;
    }

    public void remove(Player player, double amount) {
        if (economySystem.equalsIgnoreCase("vault") && econ != null) {
            econ.withdrawPlayer(player, amount);
            return;
        }
        if (economySystem.equalsIgnoreCase("playerpoints")) {
            playerPointsAPI.take(player.getUniqueId(), (int) amount);
            return;
        }
        tokenManagerAPI.removeTokens(player, (int) amount);


    }

    public void giveMoney(Player player, double amount) {
        if (getEconomySystem().equalsIgnoreCase("vault") && econ != null) {
            econ.depositPlayer(player, amount);
            return;
        }
        if (economySystem.equalsIgnoreCase("playerpoints")) {
            playerPointsAPI.give(player.getUniqueId(), (int) amount);
            return;
        }
        tokenManagerAPI.addTokens(player, (int) amount);
    }

    private String getEconomySystem() {
        return economySystem;
    }

    public String getCurrencySign() {
        if (economySystem.equalsIgnoreCase("vault")) {
            return "$";
        }
        if (economySystem.equalsIgnoreCase("playerpoints")) {
            return "Points";
        }
        if (economySystem.equalsIgnoreCase("tokenmanager")) {
            return "Tokens";
        }
        return "ERROR";
    }
}
