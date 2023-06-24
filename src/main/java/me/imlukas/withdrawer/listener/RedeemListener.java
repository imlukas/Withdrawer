package me.imlukas.withdrawer.listener;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.api.events.RedeemEvent;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.item.registry.WithdrawableItemsStorage;
import me.imlukas.withdrawer.utils.pdc.PDCWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class RedeemListener implements Listener {

    private final Withdrawer plugin;
    private final WithdrawableItemsStorage itemsRegistry;

    public RedeemListener(Withdrawer plugin) {
        this.plugin = plugin;
        this.itemsRegistry = plugin.getWithdrawableItemsStorage();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack interactedItem = event.getItem();
        Action action = event.getAction();
        boolean isShift = player.isSneaking();

        if (!(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if (interactedItem == null) {
            return;
        }

        PDCWrapper pdcWrapper = new PDCWrapper(plugin, interactedItem);
        UUID withdrawbleItemUUID = pdcWrapper.getUUID("withdrawer-uuid");

        if (withdrawbleItemUUID == null) {
            return;
        }

        event.setCancelled(true);

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
