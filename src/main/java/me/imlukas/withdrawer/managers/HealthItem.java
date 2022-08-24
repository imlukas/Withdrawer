package me.imlukas.withdrawer.managers;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.events.WithdrawEvent;
import me.imlukas.withdrawer.utils.HealthUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HealthItem extends Manager {
    private final Withdrawer main;
    private final HealthUtil healthUtil;


    public HealthItem(Withdrawer main) {
        super(main, "health");
        this.main = main;
        this.healthUtil = main.getHealthUtil();
    }

    public void give(Player player, int hp) {
        if (callEvent(player, hp, 1, WithdrawEvent.WithdrawType.EXPBOTTLE)) {
            return;
        }
        if (healthUtil.checkHealth(player, hp)) {
            healthUtil.removeHealth(player, hp);
            ItemStack HealtItem = setItemProperties(player, hp);
            player.getInventory().addItem(HealtItem);
            playWithdrawSound(player);
            sendMessages(player, hp, true);
            callEvent(player, hp, 1, WithdrawEvent.WithdrawType.HEALTH);
            return;
        }
        sendMessages(player, 1, false);
    }


}
