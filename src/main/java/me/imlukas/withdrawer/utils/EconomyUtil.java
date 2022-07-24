package me.imlukas.withdrawer.utils;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.illusion.storage.MessagesFile;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class EconomyUtil {

    private final Withdrawer main;
    private final MessagesFile messages;
    private final Economy econ;
    private final DecimalFormat df;

    public EconomyUtil(Withdrawer main) {
        this.main = main;
        this.messages = main.getMessages();
        this.econ = main.getEconomy();
        this.df = new DecimalFormat("#");
    }
    public boolean hasMoney(Player player, double amount){
        return !(getMoney(player) < amount);
    }
    public double getMoney(Player player) {
        return econ.getBalance(player);
    }
    public void removeMoney(Player player, double amount){
        econ.withdrawPlayer(player, amount);
        messages.sendStringMessage(player, "&c-" + df.format(amount));
    }
    public void giveMoney(Player player, double amount){
        econ.depositPlayer(player, amount);
        messages.sendStringMessage(player, "&a+" + df.format(amount));
    }
}
