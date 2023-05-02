package me.imlukas.withdrawer.listener;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.config.PluginSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ItemDropListener implements Listener {

    private final PluginSettings pluginSettings;

    public ItemDropListener(Withdrawer plugin) {
        this.pluginSettings = plugin.getPluginSettings();
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("withdrawer.bypass.drop")) {
            return;
        }

        ItemStack item = event.getItemDrop().getItemStack();

        NBTItem nbtItem = new NBTItem(item);
        UUID withdrawbleItemUUID = nbtItem.getUUID("withdrawer-uuid");

        if (withdrawbleItemUUID == null) {
            return;
        }

        if (!pluginSettings.isDropable()) {
            event.setCancelled(true);
        }
    }
}
