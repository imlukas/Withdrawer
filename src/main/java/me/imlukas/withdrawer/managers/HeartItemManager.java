package me.imlukas.withdrawer.managers;

import lombok.With;
import me.imlukas.withdrawer.Withdrawer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HeartItemManager extends Managers {
    private final Withdrawer main;

    public HeartItemManager(Withdrawer main) {
        super(main, "heart");
        this.main = main;
    }
  /*
    public void give(Player player, int exp, int amount) {
        int total = exp * amount;
        if (checkHealth(player, total)) {
            playWithdrawSound(player);
            sendMessages(player, total, true);
            callEvent(player, exp, amount, null);
            return;
        }
        sendMessages(player, total, false);
    }

    private void setItemProperties(Player player, double health) {
    }

    private boolean checkHealth(Player player, int total) {
    } */
}
