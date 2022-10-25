package me.imlukas.withdrawer.managers;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.events.WithdrawEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ExpBottle extends Manager {

    public ExpBottle(Withdrawer main) {
        super(main, "expbottle");
    }

    public void give(Player player, int exp, int amount) {
        int total = exp * amount;
        boolean success = false;
        if (callEvent(player, total, amount, WithdrawEvent.WithdrawType.EXPBOTTLE)) {
            return;
        }
        if (!(expUtil.getExp(player) < total)) {
            expUtil.changeExp(player, -total);
            ItemStack expItem = setItemProperties(player, exp);
            if (amount > 1) {
                for (int i = 0; i < amount; i++) {
                    player.getInventory().addItem(expItem);
                }
            } else {
                player.getInventory().addItem(expItem);
            }
            playWithdrawSound(player);
            success = true;
        }
        if (messages.isUseActionBar()) {
            sendActionBar(player, total, success);
            return;
        }
        sendMessages(player, total, success);
    }

}
