package me.imlukas.withdrawer.managers;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.events.WithdrawEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HealthItem extends Manager {
    public HealthItem(Withdrawer main) {
        super(main, "health");
    }

    public void give(Player player, int hp) {
        boolean success = false;
        if (callEvent(player, hp, 1, WithdrawEvent.WithdrawType.HEALTH)) {
            return;
        }
        if (healthUtil.checkHealth(player, hp)) {
            healthUtil.removeHealth(player, hp);
            ItemStack HealtItem = setItemProperties(player, hp);
            player.getInventory().addItem(HealtItem);
            playWithdrawSound(player);
            success = true;
        }
        if (messages.isUseActionBar()) {
            sendActionBar(player, hp, success);
            return;
        }
        sendMessages(player, hp, success);
    }


}
