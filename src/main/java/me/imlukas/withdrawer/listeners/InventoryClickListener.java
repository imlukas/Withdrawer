package me.imlukas.withdrawer.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import org.bukkit.entity.Player;
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
        Player player = (Player) event.getWhoClicked();
        if (this.main.getConfig().getBoolean("crafting") || this.main
                .getConfig().getBoolean("villager-trade"))
            return;
        if (player.hasPermission("withdrawer.bypass.crafting"))
            return;
        InventoryType invType = player.getOpenInventory().getTopInventory().getType();
        if (invType != InventoryType.WORKBENCH && invType != InventoryType.MERCHANT)
            return;
        ItemStack item = event.getCurrentItem();
        if (item == null)
            return;
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasKey("banknote-value")) {
            event.setCancelled(true);
        } else if (nbtItem.hasKey("expbottle-value")) {
            event.setCancelled(true);
        } else if (nbtItem.hasKey("health-value")) {
            event.setCancelled(true);
        }
    }
}
