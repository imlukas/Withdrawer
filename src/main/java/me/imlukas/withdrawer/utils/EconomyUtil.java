package me.imlukas.withdrawer.utils;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class EconomyUtil {

    private final Economy econ;
    private final String economySystem;
    private final PlayerPointsAPI playerPointsAPI;

    public EconomyUtil(Withdrawer main) {
        this.econ = main.getEconomy();
        this.playerPointsAPI = main.getPlayerPointsAPI();
        this.economySystem = main.getConfig().getString("economy-plugin");
    }

    // TODO: Add PlayerPoints support + other requested economy plugins.

    public boolean hasMoney(Player player, double amount) {
        return !(getMoney(player) < amount);
    }

    public double getMoney(Player player) {
        return getEconomySystem().equalsIgnoreCase("vault") && econ != null
                ? econ.getBalance(player)
                : playerPointsAPI.look(player.getUniqueId());
    }

    public void removeMoney(Player player, double amount) {
        if (getEconomySystem().equalsIgnoreCase("vault") && econ != null) {
            econ.withdrawPlayer(player, amount);
            return;
        }
        playerPointsAPI.take(player.getUniqueId(), (int) amount);


    }

    public void giveMoney(Player player, double amount) {
        if (getEconomySystem().equalsIgnoreCase("vault") && econ != null) {
            econ.depositPlayer(player, amount);
            return;
        }
        playerPointsAPI.give(player.getUniqueId(), (int) amount);
    }

    private String getEconomySystem() {
        return economySystem;
    }

    public String getCurrencySign() {
        return getEconomySystem().equalsIgnoreCase("vault") && econ != null
                ? "$"
                : " Points";
    }
}
