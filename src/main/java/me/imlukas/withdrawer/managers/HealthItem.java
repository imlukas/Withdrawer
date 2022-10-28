package me.imlukas.withdrawer.managers;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.events.WithdrawType;
import org.bukkit.entity.Player;

public class HealthItem extends Manager {
    public HealthItem(Withdrawer main) {
        super(main, "health");
    }

    public void give(Player player, int hp, boolean console) {
        boolean success = false;
        if (callEvent(player, hp, 1, WithdrawType.HEALTH)) {
            return;
        }
        if (healthUtil.checkHealth(player, hp)) {
            healthUtil.removeHealth(player, hp);

            giveItem(player, hp, 1);

            playWithdrawSound(player);
            success = true;
        }
        if (console && !success) {
            System.out.println("The player does not have enough EXP!");
            return;
        }
        if (messages.isUseActionBar()) {
            sendActionBar(player, hp, success, console);
            return;
        }
        sendMessages(player, hp, success, false);
        if (console) {
            System.out.println("You withdrew" + hp + " HP from " + player.getName());
        }
    }


    public void gift(Player target, int value) {
        giveItem(target, value, 1);
        sendGiftMessage(target, value, 1);
    }
}
