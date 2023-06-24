package me.imlukas.withdrawer.listener;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.config.PluginSettings;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.utils.pdc.PDCWrapper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ItemPickupAndDropListener implements Listener {

    private final Withdrawer plugin;
    private final PluginSettings pluginSettings;

    public ItemPickupAndDropListener(Withdrawer plugin) {
        this.plugin = plugin;
        this.pluginSettings = plugin.getPluginSettings();
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();

        PDCWrapper pdcWrapper = new PDCWrapper(plugin, item);
        UUID withdrawbleItemUUID = pdcWrapper.getUUID("withdrawer-uuid");

        if (withdrawbleItemUUID == null) {
            return;
        }

        WithdrawableItem withdrawableItem = plugin.getWithdrawableItemsStorage().getItem(withdrawbleItemUUID);

        if (withdrawableItem == null) {
            return;
        }

        if (!pluginSettings.isDropable() && !player.hasPermission("withdrawer.bypass.drop")) {
            event.setCancelled(true);
        }

        withdrawableItem.setAmount(withdrawableItem.getAmount() - item.getAmount());

    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }
        ItemStack item = event.getItem().getItemStack();

        PDCWrapper pdcWrapper = new PDCWrapper(plugin, item);
        UUID withdrawbleItemUUID = pdcWrapper.getUUID("withdrawer-uuid");

        if (withdrawbleItemUUID == null) {
            return;
        }

        WithdrawableItem withdrawableItem = plugin.getWithdrawableItemsStorage().getItem(withdrawbleItemUUID);

        if (withdrawableItem == null) {
            return;
        }

        withdrawableItem.setAmount(withdrawableItem.getAmount() + item.getAmount());
    }
}
