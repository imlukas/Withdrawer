package me.imlukas.withdrawer.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.imlukas.withdrawer.Withdrawer;
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
        if (this.main.getConfig().getBoolean("drop"))
            return;
        if (event.getPlayer().hasPermission("withdrawer.bypass.drop"))
            return;
        NBTItem nbtitem = new NBTItem(event.getItemDrop().getItemStack());
        
        if (nbtitem.hasKey("banknote-value")) {
            event.setCancelled(true);
        } else if (nbtitem.hasKey("expbottle-value")) {
            event.setCancelled(true);
        } else if (nbtitem.hasKey("health-value")) {
            event.setCancelled(true);
        }
    }
}
