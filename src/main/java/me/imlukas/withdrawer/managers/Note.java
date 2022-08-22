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
        if (callEvent(player, total, amount, WithdrawEvent.WithdrawType.BANKNOTE)) {
            return;
        }
        if (economyUtil.hasMoney(player, money * amount)) {
            economyUtil.removeMoney(player, total);
            ItemStack noteItem = setItemProperties(player, (int) money);
            if (amount > 1) {
                for (int i = 0; i < amount; i++) {
                    player.getInventory().addItem(noteItem);
                }
            } else {
                player.getInventory().addItem(noteItem);
            }
            playWithdrawSound(player);
            sendMessages(player, total, true);
            return;
        }
        sendMessages(player, total, false);
    }
}
