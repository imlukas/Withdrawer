package me.imlukas.withdrawer.listener;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.economy.EconomyManager;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.item.registry.WithdrawableItemInitializers;
import me.imlukas.withdrawer.item.registry.WithdrawableItemsStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ConnectionListener implements Listener {
    private final WithdrawableItemInitializers defaultWithdrawables;
    private final WithdrawableItemsStorage itemsStorage;
    public ConnectionListener(Withdrawer plugin) {
        this.defaultWithdrawables = plugin.getDefaultWithdrawables();
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

            NBTItem nbtItem = new NBTItem(itemStack);
            UUID itemUUID = nbtItem.getUUID("withdrawer-uuid");

            if (itemUUID == null) {
                continue;
            }

            WithdrawableItem item = defaultWithdrawables.getNewItemInstance(nbtItem);
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

            NBTItem nbtItem = new NBTItem(itemStack);
            UUID itemUUID = nbtItem.getUUID("withdrawer-uuid");

            if (itemUUID == null) {
                continue;
            }

            itemsStorage.removeItem(itemUUID);
        }
    }
}
