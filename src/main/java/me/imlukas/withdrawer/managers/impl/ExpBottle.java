package me.imlukas.withdrawer.managers.impl;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.constant.ItemType;
import me.imlukas.withdrawer.managers.Manager;
import org.bukkit.entity.Player;

public class ExpBottle extends Manager {

    public ExpBottle(Withdrawer main) {
        super(main, "expbottle");
    }

    public void give(Player player, int exp, int amount, boolean console) {
        int total = exp * amount;
        boolean success = false;
        if (callEvent(player, total, amount, ItemType.EXP)) {
            return;
        }
        if (expUtil.hasExp(player, total)) {
            expUtil.changeExp(player, -total);

            giveItem(player, exp, amount);

            playWithdrawSound(player);
            success = true;
        }
        if (console && !success) {
            System.out.println("The player does not have enough EXP!");
            return;
        }

        sendMessages(player, total, success, console);

        if (console) {
            System.out.println("You withdrew" + total + " EXP from " + player.getName());
        }
    }

    public void gift(Player target, int value, int quantity) {

        giveItem(target, value, quantity);
        sendGiftMessage(target, value, quantity);
    }
}
