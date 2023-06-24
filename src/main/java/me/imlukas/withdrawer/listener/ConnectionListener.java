package me.imlukas.withdrawer.listener;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.item.registry.WithdrawableItemInitializers;
import me.imlukas.withdrawer.item.registry.WithdrawableItemsStorage;
import me.imlukas.withdrawer.utils.pdc.PDCWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ConnectionListener implements Listener {
    private final Withdrawer plugin;
    private final WithdrawableItemInitializers defaultWithdrawables;
    private final WithdrawableItemsStorage itemsStorage;

    public ConnectionListener(Withdrawer plugin) {
        this.plugin = plugin;
        this.defaultWithdrawables = plugin.getItemInitializers();
        this.itemsStorage = plugin.getWithdrawableItemsStorage();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();

        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack == null) {
                continue;
            }

            PDCWrapper pdcWrapper = new PDCWrapper(plugin, itemStack);
            UUID itemUUID = pdcWrapper.getUUID("withdrawer-uuid");

            if (itemUUID == null) {
                continue;
            }

            WithdrawableItem item = defaultWithdrawables.getNewItemInstance(pdcWrapper);
            itemsStorage.addItem(item);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();

        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack == null) {
                continue;
            }

            PDCWrapper pdcWrapper = new PDCWrapper(plugin, itemStack);
            UUID itemUUID = pdcWrapper.getUUID("withdrawer-uuid");

            if (itemUUID == null) {
                continue;
            }

            itemsStorage.removeItem(itemUUID);
        }
    }
}
