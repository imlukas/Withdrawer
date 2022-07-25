package me.imlukas.withdrawer.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryInteractListener implements Listener {
    private final Withdrawer main;

    public InventoryInteractListener(Withdrawer main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryInteract(InventoryDragEvent event) {
        if (main.getConfig().getBoolean("crafting")) {
            return;
        }
        InventoryType inventoryType = event.getInventory().getType();

        Material itemMaterial = event.getOldCursor().getType();
        NBTItem nbtitem = new NBTItem(event.getOldCursor());

        if (itemMaterial.equals(Material.getMaterial(main.getConfig().getString("banknote.item").toUpperCase())) && nbtitem.hasKey("money-value")) {
            if (inventoryType.equals(InventoryType.CRAFTING)) {
                event.setCancelled(true);
            }
        }
        else if (itemMaterial.equals(Material.getMaterial(main.getConfig().getString("expbottle.item").toUpperCase())) && nbtitem.hasKey("exp-value")) {
            if (inventoryType.equals(InventoryType.CRAFTING)) {
                event.setCancelled(true);
            }
        }
    }
}
