package me.imlukas.withdrawer.v2.listener;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.v2.events.RedeemEvent;
import me.imlukas.withdrawer.v2.item.WithdrawableItem;
import me.imlukas.withdrawer.v2.item.registry.WithdrawableItemsRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class RedeemListener implements Listener {

    private final WithdrawableItemsRegistry itemsRegistry;

    public RedeemListener(Withdrawer plugin) {
        this.itemsRegistry = plugin.getWithdrawableItemsRegistry();
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

        WithdrawableItem withdrawableItem = itemsRegistry.getItem(withdrawbleItemUUID);

        RedeemEvent redeemEvent = new RedeemEvent(player, withdrawableItem);
        Bukkit.getPluginManager().callEvent(event);

        if (redeemEvent.isCancelled()) {
            return;
        }

        withdrawableItem.redeem(player, isShift);
    }
}
