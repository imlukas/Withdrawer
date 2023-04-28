package me.imlukas.withdrawer.listener;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.events.RedeemEvent;
import me.imlukas.withdrawer.item.Withdrawable;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.item.registry.WithdrawableItemsStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class RedeemListener implements Listener {

    private final WithdrawableItemsStorage itemsRegistry;

    public RedeemListener(Withdrawer plugin) {
        this.itemsRegistry = plugin.getWithdrawableItemsStorage();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack interactedItem = event.getItem();
        boolean isShift = player.isSneaking();

        if (interactedItem == null) {
            return;
        }

        NBTItem nbtItem = new NBTItem(interactedItem);
        UUID withdrawbleItemUUID = nbtItem.getUUID("withdrawer-uuid");

        if (withdrawbleItemUUID == null) {
            return;
        }

        WithdrawableItem withdrawable = itemsRegistry.getItem(withdrawbleItemUUID);

        if (withdrawable == null) {
            return;
        }

        RedeemEvent redeemEvent = new RedeemEvent(player, withdrawable);
        Bukkit.getPluginManager().callEvent(redeemEvent);

        if (redeemEvent.isCancelled()) {
            return;
        }

        withdrawable.redeem(player, isShift);
    }
}
