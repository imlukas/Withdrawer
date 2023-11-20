package me.imlukas.withdrawer.listener;

import me.imlukas.withdrawer.WithdrawerPlugin;
import me.imlukas.withdrawer.config.PluginSettings;
import me.imlukas.withdrawer.utils.pdc.PDCWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CraftingVillagerListener implements Listener {

    private final WithdrawerPlugin plugin;
    private final PluginSettings settings;

    public CraftingVillagerListener(WithdrawerPlugin plugin) {
        this.plugin = plugin;
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

        PDCWrapper pdcWrapper = new PDCWrapper(plugin, item);
        UUID withdrawbleItemUUID = pdcWrapper.getUUID("withdrawer-uuid");

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
