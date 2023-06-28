package me.imlukas.withdrawer.item.preparator;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import me.imlukas.withdrawer.utils.pdc.PDCWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLOutput;
import java.util.UUID;

public class WithdrawablePreparator {
    /*
    These methods handle item setup for the three possible actions:
    - Redeem
    - Gift
    - Withdraw
    Basically, just value checking and other stuff that doesn't need to be made over and over again.
    Prevents code repetition on item implementations.
 */
    private final Withdrawer plugin;
    private final MessagesFile messages;
    private final WithdrawableItem item;

    public WithdrawablePreparator(Withdrawer withdrawer, WithdrawableItem item) {
        this.plugin = withdrawer;
        this.messages = withdrawer.getMessages();
        this.item = item;
    }

    public int setupRedeem(Player player, boolean isShift) {
        int value = item.getValue();
        int amount = item.getAmount();
        String configName = item.getConfigName();

        if (!player.hasPermission("withdrawer.redeem.*")) {
            if (!player.hasPermission("withdrawer.redeem." + configName)) {
                messages.sendMessage(player, "global.no-permission");
                return 0;
            }
        }
        int totalValue = value;
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        int amountInHand = itemInHand.getAmount();

        if (isShift || amount == 1) {

            if (amountInHand < amount) {
                totalValue *= amountInHand;
                item.setAmount(amount - amountInHand);
            }

            if (amountInHand >= amount) {
                totalValue *= amount;
                removeWithdrawable(player);
            }

            player.getInventory().setItemInMainHand(null);
            return totalValue;
        }

        int amountLeft = amountInHand - 1;
        itemInHand.setAmount(amountLeft);
        item.setAmount(amountLeft);
        return totalValue;
    }

    public boolean setupGift(Player gifter, Player target) {
        String configName = item.getConfigName();
        if (!item.canWithdraw(gifter)) {
            messages.sendMessage(gifter, configName + ".no-" + configName);
            return false;
        }
        if (!gifter.hasPermission("withdrawer.gift.*")) {
            if (!gifter.hasPermission("withdrawer.gift." + configName)) {
                messages.sendMessage(gifter, "global.no-permission");
                return false;
            }
        }

        item.setAsGifted(true);
        addItem(target);
        return true;
    }

    public boolean setupWithdraw(Player player) {
        String configName = item.getConfigName();
        if (!item.canWithdraw(player)) {
            messages.sendMessage(player, configName + ".no-" + configName);
            return false;
        }

        if (!player.hasPermission("withdrawer.withdraw.*")) {
            if (!player.hasPermission("withdrawer.withdraw." + configName)) {
                messages.sendMessage(player, "global.no-permission");
                return false;
            }
        }

        addItem(player);
        return true;
    }

    public void removeWithdrawable(Player player) {
        item.removeItem(player);
    }

    public void addItem(Player player) {
        item.addItem(player);
    }
}
