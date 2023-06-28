package me.imlukas.withdrawer.item.registry;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.item.Withdrawable;
import me.imlukas.withdrawer.item.WithdrawableItem;
import me.imlukas.withdrawer.utils.pdc.PDCWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WithdrawableItemsStorage {

    private final Withdrawer plugin;
    private final Map<UUID, WithdrawableItem> withdrawableItems = new HashMap<>();
    private final WithdrawableItemInitializers defaultWithdrawables;

    public WithdrawableItemsStorage(Withdrawer plugin) {
        this.plugin = plugin;
        this.defaultWithdrawables = plugin.getItemInitializers();
        load();
    }

    public void load() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Inventory inventory = player.getInventory();

            for (ItemStack itemStack : inventory.getContents()) {
                if (itemStack == null) {
                    continue;
                }

                PDCWrapper nbtItem = new PDCWrapper(plugin, itemStack);
                UUID itemUUID = nbtItem.getUUID("withdrawer-uuid");

                if (itemUUID == null) {
                    continue;
                }

                WithdrawableItem item = defaultWithdrawables.getNewItemInstance(nbtItem);
                addItem(item);
            }
        }
    }

    public void addItem(WithdrawableItem item) {
        withdrawableItems.put(item.getUUID(), item);
    }

    public void removeItem(UUID uuid) {
        withdrawableItems.remove(uuid);
    }

    public void removeItem(Withdrawable item) {
        withdrawableItems.remove(item.getUUID());
    }

    public WithdrawableItem getItem(UUID uuid) {
        return withdrawableItems.get(uuid);
    }

    public Map<UUID, WithdrawableItem> getItems() {
        return withdrawableItems;
    }

    public boolean containsItem(UUID uuid) {
        return withdrawableItems.containsKey(uuid);
    }
}
