package me.imlukas.withdrawer.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropListener implements Listener {
    private final Withdrawer main;

    public ItemDropListener(Withdrawer main) {
        this.main = main;

    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (main.getConfig().getBoolean("drop")) {
            return;
        }

        Material itemMaterial = event.getItemDrop().getItemStack().getType();
        NBTItem nbtitem = new NBTItem(event.getItemDrop().getItemStack());

        if (itemMaterial.equals(Material.getMaterial(main.getConfig().getString("banknote.item").toUpperCase())) && nbtitem.hasKey("banknote-value")) {
            event.setCancelled(true);
        } else if (itemMaterial.equals(Material.getMaterial(main.getConfig().getString("expbottle.item").toUpperCase())) && nbtitem.hasKey("expbottle-value")) {
            event.setCancelled(true);

        }
    }
}
