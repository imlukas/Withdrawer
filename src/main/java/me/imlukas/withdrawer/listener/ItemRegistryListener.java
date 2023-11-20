package me.imlukas.withdrawer.listener;

import me.imlukas.withdrawer.WithdrawerPlugin;
import me.imlukas.withdrawer.item.registry.WithdrawableItemInitializers;
import me.imlukas.withdrawer.item.registry.WithdrawableItemsStorage;
import me.imlukas.withdrawer.utils.pdc.PDCWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ItemRegistryListener implements Listener {

    private final WithdrawerPlugin plugin;
    private final WithdrawableItemsStorage withdrawableItemsStorage;
    private final WithdrawableItemInitializers withdrawableItemInitializers;

    public ItemRegistryListener(WithdrawerPlugin plugin) {
        this.plugin = plugin;
        this.withdrawableItemsStorage = plugin.getWithdrawableItemsStorage();
        this.withdrawableItemInitializers = plugin.getItemInitializers();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        List<ItemStack> items = Arrays.asList(event.getCursor(), event.getCurrentItem());

        for (ItemStack item : items) {
            if (item == null || item.getType().isAir()) {
                continue;
            }

            PDCWrapper pdcWrapper = new PDCWrapper(plugin, item);
            UUID itemId = pdcWrapper.getUUID("withdrawer-uuid");

            if (itemId == null) {
                continue;
            }

            if (withdrawableItemsStorage.containsItem(itemId)) {
                continue;
            }

            WithdrawableItem newWithdrawableItem = withdrawableItemInitializers.getNewItemInstance(pdcWrapper);

            if (newWithdrawableItem == null) {
                continue;
            }

            withdrawableItemsStorage.addItem(newWithdrawableItem);
        }
    }
}
