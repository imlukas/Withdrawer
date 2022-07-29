package me.imlukas.withdrawer.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    private final Withdrawer main;

    public InventoryClickListener(Withdrawer main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getOpenInventory().getTopInventory().getType()
                != InventoryType.WORKBENCH)
            return;
        ItemStack item = event.getCurrentItem();
        if (item == null) {
            return;
        }
        Material itemMaterial = item.getType();
        NBTItem nbtItem = new NBTItem(item);
        if (itemMaterial == Material.AIR){
            return;
        }
        if (itemMaterial.equals(Material.getMaterial(main.getConfig().getString("banknote.item").toUpperCase())) && nbtItem.hasKey("banknote-value")) {
            event.setCancelled(true);
        } else if (itemMaterial.equals(Material.getMaterial(main.getConfig().getString("expbottle.item").toUpperCase())) && nbtItem.hasKey("expbottle-value")) {
            event.setCancelled(true);
        }
    }
}
