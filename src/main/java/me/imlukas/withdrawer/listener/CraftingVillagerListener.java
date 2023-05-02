package me.imlukas.withdrawer.listener;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.config.PluginSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class CraftingVillagerListener implements Listener {

    private final PluginSettings settings;

    public CraftingVillagerListener(Withdrawer plugin) {
        this.settings = plugin.getPluginSettings();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        InventoryType invType = player.getOpenInventory().getTopInventory().getType();

        if (invType != InventoryType.WORKBENCH && invType != InventoryType.MERCHANT) {
            return;
        }

        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType().isAir()) {
            return;
        }

        NBTItem nbtItem = new NBTItem(item);
        UUID withdrawbleItemUUID = nbtItem.getUUID("withdrawer-uuid");

        if (withdrawbleItemUUID == null) {
            return;
        }

        if (invType == InventoryType.WORKBENCH && !settings.isCraftable()) {
            event.setCancelled(true);
        }

        if (invType == InventoryType.MERCHANT && !settings.isVillagerTrades()) {
            event.setCancelled(true);
        }
    }
}
