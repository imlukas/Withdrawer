package me.imlukas.withdrawer.managers;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.events.WithdrawEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Note extends Manager {

    public Note(Withdrawer main) {
        super(main, "banknote");
    }

    public void give(Player player, double money, int amount) {
        double total = money * amount;
        boolean success = false;
        if (callEvent(player, total, amount, WithdrawEvent.WithdrawType.BANKNOTE)) {
            return;
        }
        if (economyUtil.hasMoney(player, total)) {
            economyUtil.removeMoney(player, total);
            ItemStack noteItem = setItemProperties(player, money);
            if (amount > 1) {
                for (int i = 0; i < amount; i++) {
                    player.getInventory().addItem(noteItem);
                }
            } else {
                player.getInventory().addItem(noteItem);
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
