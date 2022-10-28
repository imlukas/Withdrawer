package me.imlukas.withdrawer.managers;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.events.WithdrawType;
import org.bukkit.entity.Player;

public class Note extends Manager {

    public Note(Withdrawer main) {
        super(main, "banknote");
    }

    public void give(Player player, double money, int amount, boolean console) {
        double total = money * amount;
        boolean success = false;
        if (callEvent(player, total, amount, WithdrawType.BANKNOTE)) {
            return;
        }
        if (economyUtil.hasMoney(player, total)) {
            economyUtil.remove(player, total);

            giveItem(player, money, amount);

            playWithdrawSound(player);
            success = true;
        }
        if (console && !success) {
            System.out.println("The player does not have enough money!");
            return;
        }
        if (messages.isUseActionBar()) {
            sendActionBar(player, total, success, console);
            return;
        }
        sendMessages(player, total, success, console);

        if (console) {
            System.out.println("You have withdrawn " + total + " from " + player.getName() + "'s account");
        }
    }


    public void gift(Player target, int value, int quantity) {

        giveItem(target, value, quantity);
        sendGiftMessage(target, value, quantity);

    }
}
